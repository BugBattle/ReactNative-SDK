import { NativeModules, NativeEventEmitter, Platform } from 'react-native';
import BugBattleNetworkIntercepter from './networklogger';

type BugbattleSdkType = {
  NONE: 'NONE';
  SHAKE: 'SHAKE';
  SCREENSHOT: 'SCREENSHOT';

  initialize(
    key: string,
    activationMethod: 'NONE' | 'SHAKE' | 'SCREENSHOT'
  ): void;
  initializeMany(
    key: string,
    activationMethods: ('NONE' | 'SHAKE' | 'SCREENSHOT')[]
  ): void;
  autoConfigure(token: string): void;
  startBugReporting(): void;
  setCustomerEmail(email: string): void;
  attachCustomData(customData: any): void;
  setCustomData(key: string, value: string): void;
  removeCustomData(key: string): void;
  clearCustomData(): void;
  enablePrivacyPolicy(enable: boolean): void;
  enableReplays(enable: boolean): void;
  sendSilentBugReport(
    senderEmail: string,
    description: string,
    priority: 'LOW' | 'MEDIUM' | 'HIGH'
  ): void;
  setPrivacyPolicyUrl(privacyUrl: string): void;
  setApiUrl(apiUrl: string): void;
  setLanguage(language: string): void;
  startNetworkLogging(): void;
  setColor(hexColor: string): void;
  enablePoweredByBugbattle(enable: boolean): void;
  setLogoUrl(logoUrl: string): void;
  registerCustomAction(
    customActionCallback: (data: { name: string }) => void
  ): void;
};

const { BugbattleSdk } = NativeModules;
const networkLogger = new BugBattleNetworkIntercepter();

if (BugbattleSdk) {
  BugbattleSdk.NONE = 'NONE';
  BugbattleSdk.SHAKE = 'SHAKE';
  BugbattleSdk.SCREENSHOT = 'SCREENSHOT';

  BugbattleSdk.startNetworkLogging = () => {
    networkLogger.start();
  };

  BugbattleSdk.stopNetworkLogging = () => {
    networkLogger.setStopped(true);
  };

  BugbattleSdk.autoConfigure = (token: string) => {
    fetch(`https://widget.bugbattle.io/appwidget/${token}/config?s=reactnative`)
      .then((response) => response.json())
      .then((config) => {
        if (typeof config.color !== 'undefined' && config.color.length > 0) {
          BugbattleSdk.setColor(config.color);
        }

        if (typeof config.hideBugBattleLogo !== 'undefined') {
          BugbattleSdk.enablePoweredByBugbattle(config.hideBugBattleLogo);
        }

        if (typeof config.logo !== 'undefined' && config.logo.length > 0) {
          BugbattleSdk.setLogoUrl(config.logo);
        }

        if (
          typeof config.enableNetworkLogs !== 'undefined' &&
          config.enableNetworkLogs === true
        ) {
          BugbattleSdk.startNetworkLogging();
        }

        if (typeof config.enableReplays !== 'undefined') {
          BugbattleSdk.enableReplays(config.enableReplays);
        }

        var activationMethods = [];

        if (
          typeof config.activationMethodShake !== 'undefined' &&
          config.activationMethodShake === true
        ) {
          activationMethods.push(BugbattleSdk.SHAKE);
        }
        if (
          typeof config.activationMethodScreenshotGesture !== 'undefined' &&
          config.activationMethodScreenshotGesture === true
        ) {
          activationMethods.push(BugbattleSdk.SCREENSHOT);
        }

        BugbattleSdk.initializeMany(token, activationMethods);
      })
      .catch(() => {
        console.warn('Bugbattle: Your SDK key is invalid.');
      });
  };

  var callbacks: any[] = [];
  BugbattleSdk.registerCustomAction = (customActionCallback: any) => {
    callbacks.push(customActionCallback);
  };
  const bugBattleEmitter = new NativeEventEmitter(BugbattleSdk);
  bugBattleEmitter.addListener('bugWillBeSent', () => {
    // Push the network log to the native SDK.
    if (Platform.OS === 'android') {
      BugbattleSdk.attachNetworkLog(
        JSON.stringify(networkLogger.getRequests())
      );
    } else {
      BugbattleSdk.attachNetworkLog(networkLogger.getRequests());
    }
  });
  bugBattleEmitter.addListener('customActionTriggered', (data) => {
    if(isJsonString(data)) {
      data = JSON.parse(data);
    }
    const { name } = data;
    if (name && callbacks.length > 0) {
      for (var i = 0; i < callbacks.length; i++) {
        if (callbacks[i]) {
          callbacks[i]({
            name,
          });
        }
      }
    }
  });
}

function isJsonString(str: string) {
  try {
      JSON.parse(str);
  } catch (e) {
      return false;
  }
  return true;
}

export default BugbattleSdk as BugbattleSdkType;
