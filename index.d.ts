declare module "react-native-bugbattle-sdk" {
    export namespace BugBattleSdk {
        const NONE: "NONE";
        const SHAKE: "SHAKE";
        const THREE_FINGER_DOUBLE_TAB: "THREE_FINGER_DOUBLE_TAB";

        function initialize(key: string, activationMethod: "NONE" | "SHAKE" | "THREE_FINGER_DOUBLE_TAB"): void;
        function startBugReporting(): void;
        function setCustomerEmail(email: string): void;
        function attachCustomData(customData: any): void;
        function enablePrivacyPolicy(enable: boolean): void;
        function setPrivacyPolicyUrl(privacyUrl: string): void;
        function setApiUrl(apiUrl: string): void;
    }

    export default BugBattle;
}