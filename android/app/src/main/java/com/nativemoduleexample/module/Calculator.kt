package com.nativemoduleexample.module

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

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