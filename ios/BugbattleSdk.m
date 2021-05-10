#import "BugbattleSdk.h"

#import <React/RCTEventDispatcher.h>
#import <React/RCTLog.h>
#import <React/RCTUtils.h>

static NSString *const RCTShowDevMenuNotification = @"RCTShowDevMenuNotification";

#if !RCT_DEV

@implementation UIWindow (RNShakeEvent)

- (void)handleShakeEvent:(__unused UIEventSubtype)motion withEvent:(UIEvent *)event
{
    if (event.subtype == UIEventSubtypeMotionShake) {
        [[NSNotificationCenter defaultCenter] postNotificationName: RCTShowDevMenuNotification object:nil];
    }
}

@end

#endif

@implementation BugbattleSdk
{
  BOOL _hasListeners;
}

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(initialize:(NSString *)token andActivationMethod:(NSString *)activationMethod)
{
    // Initialize the SDK
    if ([activationMethod isEqualToString: @"SCREENSHOT"]) {
        [BugBattle initWithToken: token andActivationMethod: SCREENSHOT];
    } else {
        [BugBattle initWithToken: token andActivationMethod: NONE];
    }
    
    if ([activationMethod isEqualToString: @"SHAKE"]) {
        [[NSNotificationCenter defaultCenter] addObserver: self
                                                     selector: @selector(motionEnded:)
                                                         name: RCTShowDevMenuNotification
                                                    object: nil];
        
        #if !RCT_DEV
            RCTSwapInstanceMethods([UIWindow class], @selector(motionEnded:withEvent:), @selector(handleShakeEvent:withEvent:));
        #endif
    }
    
    if ([activationMethod isEqualToString: @"THREE_FINGER_DOUBLE_TAB"]) {
        [self initializeGestureRecognizer];
    }
    
    BugBattle.sharedInstance.delegate = self;
}

RCT_EXPORT_METHOD(initializeMany:(NSString *)token andActivationMethods:(NSArray *)activationMethods)
{
    // Initialize the SDK
    if ([self activationMethods: activationMethods contain: @"SCREENSHOT"]) {
        [BugBattle initWithToken: token andActivationMethod: SCREENSHOT];
    } else {
        [BugBattle initWithToken: token andActivationMethod: NONE];
    }
    
    if ([self activationMethods: activationMethods contain: @"SHAKE"]) {
        [[NSNotificationCenter defaultCenter] addObserver: self
                                                     selector: @selector(motionEnded:)
                                                         name: RCTShowDevMenuNotification
                                                    object: nil];
        
        #if !RCT_DEV
            RCTSwapInstanceMethods([UIWindow class], @selector(motionEnded:withEvent:), @selector(handleShakeEvent:withEvent:));
        #endif
    }
    
    if ([self activationMethods: activationMethods contain: @"THREE_FINGER_DOUBLE_TAB"]) {
        [self initializeGestureRecognizer];
    }
}

- (BOOL)activationMethods: (NSArray *)activationMethods contain: (NSString *)activationMethod {
    for (int i = 0; i < activationMethods.count; i++) {
        if ([activationMethod isEqualToString: [activationMethods objectAtIndex: i]]) {
            return true;
        }
    }
    return false;
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

- (void)bugWillBeSent {
    if (_hasListeners) {
        [self sendEventWithName:@"bugWillBeSent" body:@{}];
    }
}

- (void)startObserving
{
  _hasListeners = YES;
}

- (void)stopObserving
{
  _hasListeners = NO;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"bugWillBeSent"];
}

RCT_EXPORT_METHOD(sendSilentBugReport:(NSString *)senderEmail andDescription:(NSString *)description andPriority:(NSString *)priority)
{
    BugBattleBugPriority prio = MEDIUM;
    if ([priority isEqualToString: @"LOW"]) {
        prio = LOW;
    }
    if ([priority isEqualToString: @"HIGH"]) {
        prio = HIGH;
    }
    [BugBattle sendSilentBugReportWith: senderEmail andDescription: description andPriority: prio];
}

RCT_EXPORT_METHOD(attachNetworkLog:(NSArray *)networkLogs)
{
    [BugBattle attachData: @{ @"networkLogs": networkLogs }];
}

RCT_EXPORT_METHOD(startBugReporting)
{
    [BugBattle startBugReporting];
}

RCT_EXPORT_METHOD(setLanguage:(NSString *)language)
{
    [BugBattle setLanguage: language];
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

RCT_EXPORT_METHOD(enableReplays:(BOOL)enable)
{
    [BugBattle enableReplays: enable];
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
