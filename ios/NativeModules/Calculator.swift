import Foundation

@objc(Calculator)
class Calculator: RCTEventEmitter{
  static let EVENT_ADD_SUCCESS = "event_add_success"
  
  override func supportedEvents() -> [String]! {
    return [Calculator.EVENT_ADD_SUCCESS]
  }
  
  @objc
  override func constantsToExport() -> [AnyHashable: Any]!{
    return ["EVENT_ADD_SUCCESS": Calculator.EVENT_ADD_SUCCESS]
  }
  
  @objc
  static override func requiresMainQueueSetup() -> Bool{
    return true;
  }
  
  @objc
  func addWithPromise(_ first: Int, n2 second: Int, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock){
    resolve(first + second)
  }
  
  @objc
  func addWithCallback(_ first: Int, n2 second: Int, onSuccess: RCTResponseSenderBlock, onFail: RCTResponseSenderBlock){
    onSuccess([first + second])
  }
  
  @objc
  func addWithListener(_ first: Int, n2 second: Int){
    self.sendEvent(withName: Calculator.EVENT_ADD_SUCCESS, body: first + second)
  }
}
