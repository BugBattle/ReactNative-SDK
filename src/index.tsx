import { NativeModules } from 'react-native';

type BugbattleSdkType = {
  NONE: "NONE";
  SHAKE: "SHAKE";
  SCREENSHOT: "SCREENSHOT";
  
  initialize(
    key: string,
    activationMethod: "NONE" | "SHAKE" | "SCREENSHOT"
  ): void;
  startBugReporting(): void;
  setCustomerEmail(email: string): void;
  attachCustomData(customData: any): void;
  enablePrivacyPolicy(enable: boolean): void;
  setPrivacyPolicyUrl(privacyUrl: string): void;
  setApiUrl(apiUrl: string): void;
};

const { BugbattleSdk } = NativeModules;

if (BugbattleSdk) {
  BugbattleSdk.NONE = "NONE";
  BugbattleSdk.SHAKE = "SHAKE";
  BugbattleSdk.SCREENSHOT = "SCREENSHOT";
}

export default BugbattleSdk as BugbattleSdkType;
