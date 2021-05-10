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
  startBugReporting(): void;
  setCustomerEmail(email: string): void;
  attachCustomData(customData: any): void;
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
}

export default BugbattleSdk as BugbattleSdkType;
