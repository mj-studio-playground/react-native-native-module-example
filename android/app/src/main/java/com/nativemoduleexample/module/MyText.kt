package com.nativemoduleexample.module

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter

class MyTextPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf()
    }


    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return mutableListOf(MyTextManager())
    }
}

class MyTextManager : SimpleViewManager<MyText>() {
    override fun getName() = "MyText"

    override fun createViewInstance(reactContext: ThemedReactContext) = MyText(reactContext)

    // region Direct manipulation with ref
    override fun getCommandsMap(): MutableMap<String, Int> {
        return mutableMapOf(COMMAND_SET_TEXT to COMMAND_SET_TEXT_ID)
    }

    override fun receiveCommand(root: MyText, commandId: Int, args: ReadableArray?) {
        if (commandId == COMMAND_SET_TEXT_ID) root.textProp = args!!.getString(0)!!
    }
    // endregion

    /** props of custom native component */
    @ReactProp(name = "textProp")
    fun MyText.setText(value: String = "") {
        textProp = value
    }


    // region Native -> JS prop event
    override fun getExportedCustomDirectEventTypeConstants(): Map<String?, Any?>? {
        return createExportedCustomDirectEventTypeConstants()
    }

    private fun createExportedCustomDirectEventTypeConstants(): Map<String?, Any?>? {
        return MapBuilder.builder<String?, Any?>()
            .put(EVENT_ON_TEXT_CHANGED, MapBuilder.of("registrationName", EVENT_ON_TEXT_CHANGED)).build()
    }
    // endregion


    companion object {
        private const val COMMAND_SET_TEXT = "setText"
        private const val COMMAND_SET_TEXT_ID = 1

        const val EVENT_ON_TEXT_CHANGED = "onTextChanged"
    }
}

class MyText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {

    init {
        textSize = 48f
        gravity = Gravity.CENTER
    }

    var textProp = ""
        set(value) {
            field = value
            text = value
            emitTextChangedEvent()
        }

    private fun emitTextChangedEvent() {
        val reactContext = context as ReactContext
        reactContext.getJSModule(RCTEventEmitter::class.java)
            .receiveEvent(id, MyTextManager.EVENT_ON_TEXT_CHANGED, Arguments.createMap().apply {
                putString("text", textProp)
            })
    }
}