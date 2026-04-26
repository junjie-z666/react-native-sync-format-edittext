package com.syncformatedittext

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.SyncFormatEdittextViewManagerInterface
import com.facebook.react.viewmanagers.SyncFormatEdittextViewManagerDelegate

@ReactModule(name = SyncFormatEdittextViewManager.NAME)
class SyncFormatEdittextViewManager : SimpleViewManager<SyncFormatEdittextView>(),
  SyncFormatEdittextViewManagerInterface<SyncFormatEdittextView> {
  private val mDelegate: ViewManagerDelegate<SyncFormatEdittextView>

  init {
    mDelegate = SyncFormatEdittextViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<SyncFormatEdittextView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): SyncFormatEdittextView {
    return SyncFormatEdittextView(context)
  }

  @ReactProp(name = "color")
  override fun setColor(view: SyncFormatEdittextView?, color: Int?) {
    view?.setBackgroundColor(color ?: Color.TRANSPARENT)
  }

  companion object {
    const val NAME = "SyncFormatEdittextView"
  }
}
