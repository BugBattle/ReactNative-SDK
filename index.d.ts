declare module "bugbattle" {
    export interface MyLib {
        NONE: string;
        SHAKE: string;
        THREE_FINGER_DOUBLE_TAB: string;

        initWithToken(key: string, activationMethod: "NONE" | "SHAKE" | "THREE_FINGER_DOUBLE_TAB"): void;
        startBugReporting(): void;
        setCustomerEmail(email: string): void;
        attachCustomData(customData: any): void;
        enablePrivacyPolicy(enable: boolean): void;
        setPrivacyPolicyUrl(privacyUrl: string): void;
        setApiUrl(apiUrl: string): void;
    }

    export let bugbattle: BugBattle
}