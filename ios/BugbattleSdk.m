#import "BugbattleSdk.h"
#import <BugBattle/BugBattle.h>

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

@implementation BugbattleSdk

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(initWithToken:(NSString *)token andActivationMethod:(NSString *)activationMethod)
{
    // Initialize the SDK
    [BugBattle initWithToken: token andActivationMethod: NONE];

    
    if ([activationMethod isEqualToString: BB_SHAKE]) {
        [[NSNotificationCenter defaultCenter] addObserver: self
                                                     selector: @selector(motionEnded:)
                                                         name: RCTShowDevMenuNotification
                                                    object: nil];
        
        #if !RCT_DEV
            RCTSwapInstanceMethods([UIWindow class], @selector(motionEnded:withEvent:), @selector(handleShakeEvent:withEvent:));
        #endif
    }
    
    if ([activationMethod isEqualToString: BB_THREE_FINGER_DOUBLE_TAB]) {
        [self initializeGestureRecognizer];
    }
}

- (void)initializeGestureRecognizer {
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget: self action: @selector(handleTapGestureActivation:)];
    tapGestureRecognizer.numberOfTapsRequired = 2;
    tapGestureRecognizer.numberOfTouchesRequired = 3;
    tapGestureRecognizer.cancelsTouchesInView = false;
    
    [[[[UIApplication sharedApplication] delegate] window] addGestureRecognizer: tapGestureRecognizer];
}

- (void)handleTapGestureActivation: (UITapGestureRecognizer *)recognizer
{
    [BugBattle startBugReporting];
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
