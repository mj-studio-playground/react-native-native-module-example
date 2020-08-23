#import "NativeModuleExample-Bridging-Header.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>

@interface RCT_EXTERN_MODULE(Calculator, NSObject)
RCT_EXTERN_METHOD(
                  addWithPromise: (int)first
                  n2: (int)second
                  resolve: (RCTPromiseResolveBlock)resolve
                  reject: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(
                  addWithCallback: (int)first
                  n2: (int)second
                  onSuccess: (RCTResponseSenderBlock)onSuccess
                  onFail: (RCTResponseSenderBlock)onFail
                  )

RCT_EXTERN_METHOD(
                  addWithListener: (int)first
                  n2: (int)second
                  )
@end

@interface RCT_EXTERN_MODULE(MyTextViewManager, RCTViewManager)
RCT_EXPORT_VIEW_PROPERTY(textProp, NSString)

RCT_EXPORT_VIEW_PROPERTY(onTextChanged, RCTDirectEventBlock)

RCT_EXTERN_METHOD(
                  setText: (nonnull NSNumber *)node
                  text: (NSString)text
                  )
@end
