#import "RNBugBattle.h"
#import "BugBattleCore.h"
#import "UIWindow+BugBattleShakeRecognizer.h"

@implementation RNBugBattle

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(initWithToken:(NSString *)token andActivationMethod:(NSString *)activationMethod) {
    BugBattleActivationMethod activationMethodToUse = SHAKE;
    if ([activationMethod isEqualToString: @"NONE"]) {
        activationMethodToUse = NONE;
    }
    [BugBattle initWithToken:token andActivationMethod: activationMethodToUse];
}

@end
