## What to build

改造 JS 封装层，接受 `TextInputProps & { format?, onSyncFormatChange? }`，用 `onSyncFormatChange` 的格式化结果驱动 `onChange`/`onChangeText`。

具体变更：
- Props 改为 `TextInputProps & { format?: FormatFn, onSyncFormatChange?: (text: string, cursorPos: number) => void }`
- 全部 TextInput props 透传到 NativeComponent
- 吞掉标准 `onChange`/`onChangeText`，不在 NativeComponent 上注册
- `handleNativeChange`：收到 `onSyncFormatChange` 事件后：
  - 调 `format(text, cursorPos)` 得格式化结果
  - 调用户的 `onSyncFormatChange(formattedText, formattedCursorPos)`
  - 构造 `onChange({ nativeEvent: { text: formattedText, target } })` 调用户回调
  - 构造 `onChangeText(formattedText)` 调用户回调
  - 如果有 `value` prop（受控模式），更新内部 state 回写格式化文本
  - 如果无 `value` prop（非受控模式），等同标准 TextInput，格式化不生效
- onChange event 不带 eventCount，只填 text + target

## Acceptance criteria

- [ ] 组件接受完整 TextInputProps，TypeScript 类型正确
- [ ] onChange 返回格式化后文本（NativeSyntheticEvent 格式，无 eventCount）
- [ ] onChangeText 返回格式化后文本字符串
- [ ] onSyncFormatChange 返回格式化后 text + cursorPos
- [ ] 受控模式：传 value + format，格式化生效
- [ ] 非受控模式：不传 value，等同标准 TextInput，格式化不生效
- [ ] 标准 onChange/onChangeText 不暴露给 NativeComponent

## Blocked by

- Issue 010 (Codegen spec 全量声明)
