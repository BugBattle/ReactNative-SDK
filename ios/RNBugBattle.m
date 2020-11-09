#import "RNBugBattle.h"
#import <BugBattle/BugBattle.h>

static NSString* const ACTIVATION_NONE = @"NONE";
static NSString* const ACTIVATION_SHAKE = @"SHAKE";

@implementation RNBugBattle

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(initWithToken:(NSString *)token andActivationMethod:(NSString *)activationMethod)
{
    BugBattleActivationMethod activationMethodEnum = SHAKE;
    if ([activationMethod isEqualToString: ACTIVATION_NONE]) {
        activationMethodEnum = NONE;
    }
    [BugBattle initWithToken: token andActivationMethod: activationMethodEnum];
}

RCT_EXPORT_METHOD(startBugReporting)
{
    [BugBattle startBugReporting];
}

RCT_EXPORT_METHOD(setCustomerEmail:(NSString *)email)
{
    [BugBattle setCustomerEmail: email];
}

RCT_EXPORT_METHOD(attachCustomData:(NSDictionary *)customData)
{
    [BugBattle attachCustomData: customData];
}

RCT_EXPORT_METHOD(enablePrivacyPolicy:(BOOL)enable)
{
    [BugBattle enablePrivacyPolicy: enable];
}

RCT_EXPORT_METHOD(setPrivacyPolicyUrl: (NSString *)privacyPolicyUrl)
{
    [BugBattle setPrivacyPolicyUrl: (NSString *)privacyPolicyUrl];
}

RCT_EXPORT_METHOD(setApiUrl: (NSString *)apiUrl)
{
    [BugBattle setApiUrl: apiUrl];
}

@end
