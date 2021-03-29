#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <BugBattle/BugBattle.h>

@interface BugbattleSdk : RCTEventEmitter <RCTBridgeModule, BugBattleDelegate>

@end
