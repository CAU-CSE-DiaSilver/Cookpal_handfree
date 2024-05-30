// replace with your package
package com.cookpal

import android.util.Log
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactPropGroup
import androidx.navigation.fragment.NavHostFragment
import android.content.pm.PackageManager

import android.Manifest
import androidx.core.content.ContextCompat

import com.cookpal.R 
//import com.cookpal.handlandmarker.fragment.PermissionsFragment
import com.cookpal.handlandmarker.fragment.CameraFragment
import com.cookpal.Global

class CameraViewManager(
    private val reactContext: ReactApplicationContext
) : ViewGroupManager<FrameLayout>() {
  private var propWidth: Int? = null
  private var propHeight: Int? = null
  lateinit var activity : FragmentActivity
  //val myFragment = PermissionsFragment()
  val camera = CameraFragment()
  var id = 0

  override fun getName() = REACT_CLASS

  /**
   * Return a FrameLayout which will later hold the Fragment
   */
  override fun createViewInstance(reactContext: ThemedReactContext) =
      FrameLayout(reactContext)

  /**
   * Map the "create" command to an integer
   */
  override fun getCommandsMap() = mapOf("create" to COMMAND_CREATE, "remove" to COMMAND_DELETE)
    //mapOf("remove" to COMMAND_DELETE)
    

  /**
   * Handle "create" command (called from JS) and call createFragment method
   */
  override fun receiveCommand(
      root: FrameLayout,
      commandId: String,
      args: ReadableArray?
  ) {
    super.receiveCommand(root, commandId, args)
    val reactNativeViewId = requireNotNull(args).getInt(0)

    Global.check = commandId.toInt();
    when (commandId.toInt()) {
      COMMAND_CREATE -> createFragment(root, reactNativeViewId)
      COMMAND_DELETE -> removeFragment(reactNativeViewId)
    }
  }

  @ReactPropGroup(names = ["width", "height"], customType = "Style")
  fun setStyle(view: FrameLayout, index: Int, value: Int) {
    if (index == 0) propWidth = value
    if (index == 1) propHeight = value
  }

  fun removeFragment(reactNativeViewId: Int) {
    //camera.onDestroyView()
    //camera.onPause()
    
    val activity = reactContext.currentActivity as FragmentActivity
    val fragmentManager = activity.supportFragmentManager

    val fragment = fragmentManager.findFragmentById(reactNativeViewId)
    if (fragment != null) {
        fragmentManager.beginTransaction().remove(fragment).commit()
    }
     
}
  /**
   * Replace your React Native view with a custom fragment
   */
  fun createFragment(root: FrameLayout, reactNativeViewId: Int) {
    val parentView = root.findViewById<ViewGroup>(reactNativeViewId)
    setupLayout(parentView)
    activity = reactContext.currentActivity as FragmentActivity
    println("test")
    id = reactNativeViewId
    /*
    activity.supportFragmentManager
        .beginTransaction()
        .replace(reactNativeViewId, myFragment, reactNativeViewId.toString())
        .commit() 
     */
    activity.supportFragmentManager
      .beginTransaction()
      .replace(id, camera, id.toString())
      .commit() 
  }


  fun setupLayout(view: View) {
    Choreographer.getInstance().postFrameCallback(object: Choreographer.FrameCallback {
      override fun doFrame(frameTimeNanos: Long) {
        manuallyLayoutChildren(view)
        view.viewTreeObserver.dispatchOnGlobalLayout()
        Choreographer.getInstance().postFrameCallback(this)
      }
    })
  }

  /**
   * Layout all children properly
   */
  private fun manuallyLayoutChildren(view: View) {
    // propWidth and propHeight coming from react-native props
    val width = requireNotNull(propWidth)
    val height = requireNotNull(propHeight)

    view.measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))

    view.layout(0, 0, width, height)
  }

  companion object {
    private const val REACT_CLASS = "CameraViewManager"
    private const val COMMAND_CREATE = 1
    private const val COMMAND_DELETE= 2
  }
}