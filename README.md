# React Native Native Module, Native Component example

## Screenshots

<img src="./screenshot/1.jpg" width="500"/>

<img src="./screenshot/2.jpg" width="500"/>

## Overview

The React Native can use native features with React Native bridge. 
This native feature includes Native modules and Native components. 
The React Native Re-architecture with JSI is ongoing but knowing how to working native modules is valuable at this time.

I created two native modules `Calculator` and `MyText`. `Calculator` is just a simple calculator class and `MyText` is a simple `AppCompatTextView`(Android) and `UILabel`(iOS) wrapper view.

This sample is written with languages `JavaScript`, `Kotlin`(Android), and `Swift`(iOS).

----

## 0. Basic settings

### Android

#### 1. Configure Kotlin plugin

*app/build.gradle* (module level)

When you create Kotlin file in React Native project first, `configure Kotlin` button is shown. Click Yes.

```groovy
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android' // add kotlin-android plugin

...
```

*build.gradle* (project level)

```groovy
buildscript {
    ext.kotlin_version = '1.4.0' // this is added automatically
    ext {
        buildToolsVersion = "29.0.2"
        minSdkVersion = 16
        compileSdkVersion = 29
        targetSdkVersion = 29
    }
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version" // this is added automatically
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
    ...
}
```

### iOS

#### 1. Create swift, obj-c bridging header

*{{Your-Project-Name}}-Bridging-Header.h*

When you create Swift file in React Native project first, the bridge header configure dialog is shown. Click Yes.

```h
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
```

You can check your bridging header file path in XCode.

<img src="./screenshot/bridging-header.jpg" width="500"/>

#### 2. Create Objective-C extern bridge file

*NativeModules.m*

```objective-c
#import "{{Your-Project-Name}}-Bridging-Header.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>

@interface RCT_EXTERN_MODULE(Calculator, NSObject) // inspect it later!
...
@end
```

----

## 1. Native Module(Calculator)

The `Calculator` module has three different way to add two integer.
- Promise
- Callback
- Event(Native -> JS)

### React(JavaScript)

*Calculator.js*

```js
import {NativeModules, NativeEventEmitter} from 'react-native';

const NativeCalculator = NativeModules.Calculator;
const CalculatorEmitter = new NativeEventEmitter(NativeCalculator);

const EventName = NativeCalculator.EVENT_ADD_SUCCESS;

class Calculator {
  native;
  subscription;

  constructor(native) {
    this.native = native;
  }

  async addWithPromise(n1, n2) {
    return await this.native.addWithPromise(n1, n2);
  }

  async addWithCallback(n1, n2, callback) {
    this.native.addWithCallback(n1, n2, callback, (e) => {});
  }

  addWithListener(n1, n2) {
    this.native.addWithListener(n1, n2);
  }

  addResultListener(listener) {
    this.subscription = CalculatorEmitter.addListener(EventName, listener);
  }

  removeResultListener() {
    this.subscription && this.subscription.remove();
  }
}

export default new Calculator(NativeCalculator);

```

### Android(Kotlin)

*Calculator.kt*

```kotlin
class CalculatorPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(Calculator(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}

class Calculator(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = "Calculator"

    override fun getConstants() = mapOf(
        "EVENT_ADD_SUCCESS" to EVENT_ADD_SUCCESS
    )

    @ReactMethod
    fun addWithPromise(n1: Int, n2: Int, promise: Promise) = promise.resolve(n1 + n2)

    @ReactMethod
    fun addWithCallback(n1: Int, n2: Int, successCallback: Callback, failCallback: Callback) {
        successCallback(n1 + n2)
    }

    @ReactMethod
    fun addWithListener(n1: Int, n2: Int) =
        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(EVENT_ADD_SUCCESS, n1 + n2)

    companion object {
        const val EVENT_ADD_SUCCESS = "event_add_success"
    }
}
```

*MainApplication.kt*

Add our `ReactPackage` in `getPackages()` method.

```kotlin
class MainApplication : Application(), ReactApplication {
    override fun getReactNativeHost() = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport() = BuildConfig.DEBUG

        override fun getPackages() = PackageList(this).packages.apply {
            add(MyTextPackage())
            add(CalculatorPackage()) // Here
        }

        override fun getJSMainModuleName() = "index"
    }
    ...
}
```

### iOS(Swift, Obj-C)

*Calculator.swift*

```swift
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

```

*NativeModules.m*

```objective-c
#import "{{Your-Project-Name}}-Bridging-Header.h"
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
```

----
## 2. Native Component(MyText)

### Declare modules

### Prop `text`

### Native -> JS component event(direct event) `onTextChanged`

### Direct manipluation with ref `setText`

### React Usage
