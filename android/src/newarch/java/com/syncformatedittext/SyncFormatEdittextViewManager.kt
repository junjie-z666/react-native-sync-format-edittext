package com.syncformatedittext

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
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
    val view = SyncFormatEdittextView(context)
    view.setOnChangeListener { text, cursorPos ->
      val dispatcher = UIManagerHelper.getEventDispatcherForReactTag(context, view.id)
      dispatcher?.dispatchEvent(
        SyncFormatEditTextChangeEvent(
          UIManagerHelper.getSurfaceId(view),
          view.id,
          text,
          cursorPos
        )
      )
    }
    return view
  }

  @ReactProp(name = "value")
  override fun setValue(view: SyncFormatEdittextView?, value: String?) {
    SyncFormatEdittextHelper.setValue(view, value)
  }

  @ReactProp(name = "placeholder")
  override fun setPlaceholder(view: SyncFormatEdittextView?, placeholder: String?) {
    SyncFormatEdittextHelper.setPlaceholder(view, placeholder)
  }

  companion object {
    const val NAME = "SyncFormatEdittextView"
  }
}
