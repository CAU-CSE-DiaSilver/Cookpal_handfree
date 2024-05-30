// replace with your package
package com.cookpal

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

import com.facebook.react.bridge.NativeModule

class CameraPackage : ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        val modules = ArrayList<NativeModule>()
        modules.add(CameraViewManager(reactContext))
        return modules
    }

    override fun createViewManagers(
        reactContext: ReactApplicationContext
    ) = listOf(CameraViewManager(reactContext))
}
