package com.nativemoduleexample

import android.app.Application
import android.content.Context
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactNativeHost
import com.facebook.soloader.SoLoader
import com.nativemoduleexample.module.CalculatorPackage
import com.nativemoduleexample.module.MyTextPackage
import java.lang.reflect.InvocationTargetException

class MainApplication : Application(), ReactApplication {
    override fun getReactNativeHost() = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport() = BuildConfig.DEBUG

        override fun getPackages() = PackageList(this).packages.apply {
            add(MyTextPackage())
            add(CalculatorPackage())
        }

        override fun getJSMainModuleName() = "index"
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this,  /* native exopackage */false)
        initializeFlipper(this, reactNativeHost.reactInstanceManager)
    }

    companion object {
        private fun initializeFlipper(
            context: Context, reactInstanceManager: ReactInstanceManager
        ) {
            if (BuildConfig.DEBUG) {
                try {
                    val aClass = Class.forName("com.nativemoduleexample.ReactNativeFlipper")
                    aClass.getMethod("initializeFlipper", Context::class.java, ReactInstanceManager::class.java)
                        .invoke(null, context, reactInstanceManager)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
            }
        }
    }
}