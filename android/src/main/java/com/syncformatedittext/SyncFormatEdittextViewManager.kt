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
class SyncFormatEdittextViewManager : SimpleViewManager<SyncFormatEditextView>(),
  SyncFormatEdittextViewManagerInterface<SyncFormatEditextView> {
  private val mDelegate: ViewManagerDelegate<SyncFormatEditextView>

  init {
    mDelegate = SyncFormatEdittextViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<SyncFormatEditextView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): SyncFormatEditextView {
    val view = SyncFormatEditextView(context)
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
  override fun setValue(view: SyncFormatEditextView?, value: String?) {
    view?.setFormattedText(value ?: "", (value ?: "").length)
  }

  @ReactProp(name = "placeholder")
  override fun setPlaceholder(view: SyncFormatEditextView?, placeholder: String?) {
    view?.hint = placeholder
  }

  @ReactProp(name = "color")
  override fun setColor(view: SyncFormatEditextView?, color: Int?) {
    view?.setBackgroundColor(color ?: Color.TRANSPARENT)
  }

  companion object {
    const val NAME = "SyncFormatEditextView"
  }
}
