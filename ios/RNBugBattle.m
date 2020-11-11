#import "RNBugBattle.h"
#import <BugBattle/BugBattle.h>

#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTLog.h>
#import <React/RCTUtils.h>

static NSString* const BB_NONE = @"NONE";
static NSString* const BB_SHAKE = @"SHAKE";
static NSString* const BB_THREE_FINGER_DOUBLE_TAB = @"THREE_FINGER_DOUBLE_TAB";
static NSString *const RCTShowDevMenuNotification = @"RCTShowDevMenuNotification";

#if !RCT_DEV

@implementation UIWindow (RNShakeEvent)

- (void)handleShakeEvent:(__unused UIEventSubtype)motion withEvent:(UIEvent *)event
{
    if (event.subtype == UIEventSubtypeMotionShake) {
        [[NSNotificationCenter defaultCenter] postNotificationName:RCTShowDevMenuNotification object:nil];
    }
}

@end

#endif

@implementation RNBugBattle

static int initialized = 0;

+ (int) initialized { return initialized; }
+ (void) setInitialized:(int)value { initialized = value; }

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(initWithToken:(NSString *)token andActivationMethod:(NSString *)activationMethod)
{
    if (RNBugBattle.initialized != 0) {
        NSLog(@"Already initialized Bugbattle SDK.");
        return;
    }
    RNBugBattle.initialized = 1;
    
    BugBattleActivationMethod activationMethodEnum = SHAKE;
    if ([activationMethod isEqualToString: BB_NONE]) {
        activationMethodEnum = NONE;
    }
    if ([activationMethod isEqualToString: BB_THREE_FINGER_DOUBLE_TAB]) {
        activationMethodEnum = THREE_FINGER_DOUBLE_TAB;
    }
    
    [BugBattle initWithToken: token andActivationMethod: activationMethodEnum];

    [[NSNotificationCenter defaultCenter] addObserver: self
                                                 selector: @selector(motionEnded:)
                                                     name: RCTShowDevMenuNotification
                                                object: nil];
    
    #if !RCT_DEV
        RCTSwapInstanceMethods([UIWindow class], @selector(motionEnded:withEvent:), @selector(handleShakeEvent:withEvent:));
    #endif
}

- (void)motionEnded:(NSNotification *)notification
{
    [BugBattle startBugReporting];
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
