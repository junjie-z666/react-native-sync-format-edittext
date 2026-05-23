# Context

## 领域术语

- **SyncFormatEditText**: 受控组件，用户输入时通过 JS 格式化函数格式化后显示
- **格式化函数**: `(text: string, cursorPos: number) => { text: string; cursorPos: number }` — 纯同步函数，输入文本+光标，返回格式化后文本+新光标位置
- **输入回退**: TextWatcher 检测到输入后，立刻用 `Editable.replace()` 回退到上次格式化值，防止未格式化文本闪现（继承 TextInput 后将移除此机制）
- **onSyncFormatChange**: 自定义事件，携带 `{ text, cursorPos }`，cursorPos 为格式化文本中的光标位置（`Selection.getSelectionStart()`），format 函数负责从格式化位置推算

## 架构决策

1. **组件模式**: 受控组件（value + onChange prop），format 函数在 JS 侧执行。同时支持非受控模式（不传 value 时等同标准 TextInput，格式化不生效）
2. **防闪烁**: 继承 ReactEditText 后移除 isReverting/lastFormattedText 回退机制，接受 16-50ms 未格式化文本短暂闪现
3. **Props 全量兼容**: 继承 ReactEditText + ReactTextInputManager，Codegen spec 全量声明 TextInput props + onSyncFormatChange，用户获得完整 TextInputProps 类型提示
4. **线程模型**: 异步 — 原生发 onSyncFormatChange 事件到 JS，JS 格式化后通过 value prop 回写，maybeSetTextFromJS() 更新显示（isSettingTextFromJS flag 抑制二次 TextWatcher 触发）
5. **事件流**: ReactTextInputTextWatcher 发标准 topChange（eventCount 协调），自定义 TextWatcher 发 onSyncFormatChange。JS 封装层吞掉标准 topChange，用 onSyncFormatChange 的格式化结果驱动 onChange/onChangeText
6. **onChange/onChangeText**: JS 封装层构造，返回格式化后文本。onChange event 不带 eventCount，只填 text + target
7. **value prop**: 自定义 @ReactProp(name="value") 调 maybeSetTextFromJS()，绕过标准 TextInput state 机制
8. **cursorPos 映射**: 删除 native 侧 computeFormattedCursorPos/significantCharPredicate，cursorPos 完全由 JS 侧 format 函数控制
9. **format prop 时机**: 只影响后续输入，format 变化不重新格式化已显示文本
10. **Android 架构兼容**: View + ViewManager 合并到 src/main/，Package 类仍分 newarch/oldarch source set。ReactTextInputManager 新老架构共用（内部通过 stateWrapper 区分）
11. **组件注册名**: getName() = "SyncFormatEdittextView" 不变
12. **iOS/Web**: 暂不改动，iOS 仍 stub，Web 仍空 View
13. **SyncFormatEdittextHelper**: 删除，功能由 ReactTextInputManager 基类 + 自定义 @ReactProp 替代
