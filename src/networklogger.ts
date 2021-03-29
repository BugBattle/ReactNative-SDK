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
            url: params[0],
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
    });
  }

  interceptNetworkRequests(callback: any) {
    // eslint-disable-next-line consistent-this
    var self = this;

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
