package com.syncformatedittext

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class SyncFormatEditTextChangeEvent(
  surfaceId: Int,
  viewTag: Int,
  private val text: String,
  private val cursorPos: Int
) : Event<SyncFormatEditTextChangeEvent>(surfaceId, viewTag) {

  override fun getEventName(): String = "topSyncFormatChange"

  override fun getEventData(): WritableMap {
    return Arguments.createMap().apply {
      putString("text", text)
      putDouble("cursorPos", cursorPos.toDouble())
    }
  }
}