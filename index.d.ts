declare module "react-native-bugbattle-sdk" {
  export namespace BugBattle {
    const NONE: "NONE";
    const SHAKE: "SHAKE";
    function initialize(
      key: string,
      activationMethod: "NONE" | "SHAKE"
    ): void;
    function startBugReporting(): void;
    function setCustomerEmail(email: string): void;
    function attachCustomData(customData: any): void;
    function enablePrivacyPolicy(enable: boolean): void;
    function setPrivacyPolicyUrl(privacyUrl: string): void;
    function setApiUrl(apiUrl: string): void;
  }
  export default BugBattle;
}