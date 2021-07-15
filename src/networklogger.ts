class BugBattleNetworkIntercepter {
  requestId = 0;
  requests: any = {};
  maxRequests = 10;
  stopped = false;

  getRequests() {
    return Object.values(this.requests);
  }

  setMaxRequests(maxRequests: number) {
    this.maxRequests = maxRequests;
  }

  setStopped(stopped: boolean) {
    this.stopped = stopped;
  }

  cleanRequests() {
    var keys = Object.keys(this.requests);
    if (keys.length > this.maxRequests) {
      var keysToRemove = keys.slice(0, keys.length - this.maxRequests);
      for (var i = 0; i < keysToRemove.length; i++) {
        delete this.requests[keysToRemove[i]];
      }
    }
  }

  calcRequestTime(bbRequestId: string | number) {
    if (!this.requests[bbRequestId]) {
      return;
    }

    var startDate = this.requests[bbRequestId].date;
    if (startDate) {
      this.requests[bbRequestId].duration =
        new Date().getTime() - startDate.getTime();
      this.requests[bbRequestId].date = this.requests[
        bbRequestId
      ].date.toString();
    }
  }

  start() {
    this.setStopped(false);
    this.interceptNetworkRequests({
      onFetch: (params: any, bbRequestId: any) => {
        if (this.stopped) {
          return;
        }

        if (params.length >= 2) {
          var method = params[1].method ? params[1].method : 'GET';
          this.requests[bbRequestId] = {
            request: {
              payload: params[1].body,
              headers: params[1].headers,
            },
            type: method,
            url: params[0],
            date: new Date(),
          };
        } else {
          this.requests[bbRequestId] = {
            request: {},
            url: params[0],
            type: 'GET',
            date: new Date(),
          };
        }

        this.cleanRequests();
      },
      onFetchLoad: (req: any, bbRequestId: any) => {
        if (this.stopped) {
          return;
        }

        req.text().then((responseText: any) => {
          this.requests[bbRequestId].success = true;
          this.requests[bbRequestId].response = {
            status: req.status,
            statusText: req.statusText,
            responseText: responseText,
          };

          this.calcRequestTime(bbRequestId);

          this.cleanRequests();
        });
      },
      onFetchFailed: (_err: any, bbRequestId: any) => {
        if (this.stopped) {
          return;
        }

        this.requests[bbRequestId].success = false;
        this.calcRequestTime(bbRequestId);

        this.cleanRequests();
      },
      onOpen: (request: any, args: string | any[]) => {
        if (this.stopped) {
          return;
        }

        if (
          request &&
          request.bbRequestId &&
          args.length >= 2 &&
          this.requests
        ) {
          this.requests[request.bbRequestId] = {
            type: args[0],
            url: args[1],
            date: new Date(),
          };
        }

        this.cleanRequests();
      },
      onSend: (request: any, args: string | any[]) => {
        if (this.stopped) {
          return;
        }

        if (
          request &&
          request.bbRequestId &&
          args.length > 0 &&
          this.requests &&
          this.requests[request.bbRequestId]
        ) {
          this.requests[request.bbRequestId].request = {
            payload: args[0],
            headers: request.requestHeaders,
          };
          this.calcRequestTime(request.bbRequestId);
        }

        this.cleanRequests();
      },
      onError: (request: any) => {
        if (
          !this.stopped &&
          this.requests &&
          request &&
          request.currentTarget &&
          request.currentTarget.bbRequestId &&
          this.requests[request.currentTarget.bbRequestId]
        ) {
          var target = request.currentTarget;
          this.requests[target.bbRequestId].success = false;
          this.calcRequestTime(request.bbRequestId);
        }

        this.cleanRequests();
      },
      onLoad: (request: any) => {
        if (this.stopped) {
          return;
        }

        if (
          request &&
          request.currentTarget &&
          request.currentTarget.bbRequestId &&
          this.requests &&
          this.requests[request.currentTarget.bbRequestId]
        ) {
          var target = request.currentTarget;
          this.requests[target.bbRequestId].success = true;
          this.requests[target.bbRequestId].response = {
            status: target.status,
            statusText: target.statusText,
            responseText:
              target.responseType === 'text'
                ? target.responseText
                : '<' + target.responseType + '>',
          };
        }

        this.cleanRequests();
      },
    });
  }

  interceptNetworkRequests(callback: any) {
    // eslint-disable-next-line consistent-this
    var self = this;

    // XMLHttpRequest
    const open = XMLHttpRequest.prototype.open;
    const send = XMLHttpRequest.prototype.send;
    const wrappedSetRequestHeader = XMLHttpRequest.prototype.setRequestHeader;

    XMLHttpRequest.prototype.setRequestHeader = function (header, value) {
      wrappedSetRequestHeader(header, value);

      var reqSelf = this as any;
      if (!reqSelf.requestHeaders) {
        reqSelf.requestHeaders = {};
      }

      if (!reqSelf.requestHeaders[header]) {
        reqSelf.requestHeaders[header] = [];
      }

      reqSelf.requestHeaders[header].push(value);
    };

    XMLHttpRequest.prototype.open = function () {
      var reqSelf = this as any;
      reqSelf.bbRequestId = ++self.requestId;
      callback.onOpen && callback.onOpen(reqSelf, arguments);
      if (callback.onLoad) {
        reqSelf.addEventListener('load', callback.onLoad.bind(callback));
      }
      if (callback.onError) {
        reqSelf.addEventListener('error', callback.onError.bind(callback));
      }
      // @ts-ignore
      return open.apply(reqSelf, arguments);
    };
    XMLHttpRequest.prototype.send = function () {
      callback.onSend && callback.onSend(this, arguments);
      // @ts-ignore
      return send.apply(this, arguments);
    };

    // Fetch
    if (global) {
      (function () {
        var originalFetch = global.fetch;
        global.fetch = function () {
          var bbRequestId = ++self.requestId;
          callback.onFetch(arguments, bbRequestId);

          return (
            originalFetch
              // @ts-ignore
              .apply(this, arguments)
              .then(function (data) {
                return data.text().then((textData) => {
                  data.text = function () {
                    return Promise.resolve(textData);
                  };

                  data.json = function () {
                    return Promise.resolve(JSON.parse(textData));
                  };

                  callback.onFetchLoad(data, bbRequestId);

                  return data;
                });
              })
              .catch((err) => {
                callback.onFetchFailed(err, bbRequestId);
                throw err;
              })
          );
        };
      })();
    }

    return callback;
  }
}

export default BugBattleNetworkIntercepter;
