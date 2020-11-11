#import <React/RCTBridgeModule.h>

@interface RNBugBattle : NSObject <RCTBridgeModule>

+ (int) initialized;
+ (void) setInitialized:(int)value;

@end
