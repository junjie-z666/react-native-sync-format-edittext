# ADR 0003: 继承 ReactEditText + ReactTextInputManager

**状态**: 已接受

## 背景

SyncFormatEdittextView 原继承 AppCompatEditText，只支持 value/placeholder 两个 prop。用户期望无缝替换 TextInput，需要全量 props 兼容。

## 决策

View 继承 `ReactEditText`，ViewManager 继承 `ReactTextInputManager`。

### 关键子决策

1. **value prop**: 自定义 `@ReactProp(name="value")` 调 `maybeSetTextFromJS()`，不使用标准 TextInput state 机制（避免写 C++ ComponentDescriptor）
2. **防闪烁**: 移除 isReverting/lastFormattedText 回退机制，接受短暂闪现
3. **事件流**: 保留自定义 `onSyncFormatChange`，与标准 `topChange` 并存。JS 层吞掉 topChange，用 onSyncFormatChange 驱动 onChange/onChangeText
4. **cursorPos**: 删除 native 侧 computeFormattedCursorPos，发格式化文本中的光标位置，由 JS format 函数完全控制
5. **架构合并**: ViewManager 合并到 src/main/，Package 仍分 newarch/oldarch

## 理由

- 继承路线让用户获得完整 TextInputProps，无需逐个手动转发
- ReactTextInputManager 新老架构共用（内部 stateWrapper 区分），可合并 ViewManager
- 自定义 value prop 避免了 C++ ComponentDescriptor 的复杂度和维护成本
- onSyncFormatChange 独立于标准事件，不修改标准事件结构，RN 升级更安全

## 后果

- 依赖 ReactEditText/ReactTextInputManager 内部 API，RN 大版本升级可能需要适配
- maybeSetTextFromJS 设置文本时不触发 TextWatcher（isSettingTextFromJS flag），这对格式化流程是好事（无二次触发），但如果未来 RN 改变此行为需注意
- 16-50ms 未格式化文本可能短暂闪现
- Fabric 侧 value 通过自定义 @ReactProp 而非 state 机制流转，与标准 TextInput 的 value 流转路径不同，可能产生边缘 case
