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

- (void)initSDK {
    BugBattle.sharedInstance.delegate = self;
    [BugBattle setApplicationType: REACTNATIVE];
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
    
    [self initSDK];
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

    [self initSDK];
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

- (void)customActionCalled:(NSString *)customAction {
    if (_hasListeners) {
        [self sendEventWithName:@"customActionTriggered" body:@{
            @"name": customAction
        }];
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

-(UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1];
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"bugWillBeSent", @"customActionTriggered"];
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

RCT_EXPORT_METHOD(setColor:(NSString *)hexColor)
{
    UIColor * color = [self colorFromHexString: hexColor];
    [BugBattle setColor: color];
}
RCT_EXPORT_METHOD(setLanguage:(NSString *)language)
{
    [BugBattle setLanguage: language];
}

RCT_EXPORT_METHOD(setCustomerName:(NSString *)name)
{
    [BugBattle setCustomerName: name];
}

RCT_EXPORT_METHOD(setCustomerEmail:(NSString *)email)
{
    [BugBattle setCustomerEmail: email];
}

RCT_EXPORT_METHOD(attachCustomData:(NSDictionary *)customData)
{
    [BugBattle attachCustomData: customData];
}

RCT_EXPORT_METHOD(setCustomData:(NSString *)key andData:(NSString *)value)
{
    [BugBattle setCustomData: value forKey: key];
}

RCT_EXPORT_METHOD(removeCustomData:(NSString *)key)
{
    [BugBattle removeCustomDataForKey: key];
}

RCT_EXPORT_METHOD(setLogoUrl:(NSString *)key)
{
    [BugBattle setLogoUrl: key];
}

RCT_EXPORT_METHOD(enablePoweredByBugbattle:(BOOL)enable)
{
    [BugBattle enablePoweredByBugbattle: enable];
}

RCT_EXPORT_METHOD(clearCustomData)
{
    [BugBattle clearCustomData];
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

RCT_EXPORT_METHOD(logEvent:(NSString *)name andData:(NSDictionary *)data)
{
    [BugBattle logEvent: name withData: data];
}

@end
