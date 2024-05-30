package com.cookpal

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Callback

class SaveModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName():String = "SaveModule"

    @ReactMethod
    fun getFist(callback:Callback){
        callback.invoke(Global.isFist)
    }

    @ReactMethod
    fun getVictory(callback:Callback){
        callback.invoke(Global.isVictory)
    }

    @ReactMethod
    fun getBackOrFront(callback:Callback){
        callback.invoke(Global.backOrFront)
    }

    @ReactMethod
    fun getTest1(callback:Callback){
        callback.invoke(Global.test1)
    }

    @ReactMethod
    fun getTest2(callback:Callback){
        callback.invoke(Global.test2)
    }

    @ReactMethod
    fun getTest3(callback:Callback){
        callback.invoke(Global.test3)
    }

    @ReactMethod
    fun getTest4(callback:Callback){
        callback.invoke(Global.test4)
    }

    @ReactMethod
    fun getTest5(callback:Callback){
        callback.invoke(Global.test5)
    }

    @ReactMethod
    fun getCheck(callback:Callback){
        callback.invoke(Global.check)
    }
}