import UIKit

@objc(MyTextViewManager)
class MyTextViewManager: RCTViewManager{
  override func view() -> UIView! {
    return MyTextView()
  }
  
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  override func constantsToExport() -> [AnyHashable : Any]! {
    return [:]
  }
  
  @objc
  func setText(_ node: NSNumber, text: String){
    DispatchQueue.main.async {
      let component = self.bridge.uiManager.view(forReactTag: node) as! MyTextView
      component.textProp = text
    }
  }
}

fileprivate class MyTextView: UILabel {
  @objc
  var textProp: String = "" {
    didSet {
      self.text = self.textProp
      self.onTextChanged?(["text": self.textProp])
    }
  }
  
  @objc
  var onTextChanged: RCTDirectEventBlock?

  required init?(coder: NSCoder) {
    fatalError("Not Implemented")
  }
  
  override init(frame: CGRect) {
    super.init(frame: frame)
    
    self.font = UIFont.systemFont(ofSize: 48)
    self.textAlignment = .center
    self.numberOfLines = 0
  }
}
