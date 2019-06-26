#import "BBBugBattle.h"
#import <BugBattle/BugBattle.h>

@implementation BBBugBattle

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(sampleMethod:(NSString *)stringArgument numberParameter:(nonnull NSNumber *)numberArgument callback:(RCTResponseSenderBlock)callback)
{
    // TODO: Implement some real useful functionality
    callback(@[[NSString stringWithFormat: @"numberArgument: %@ stringArgument: %@", numberArgument, stringArgument]]);
}

RCT_EXPORT_METHOD(init:(NSString *)apiKey activationMethod:(NSString *)activationMethod)
{
    
}

@end
