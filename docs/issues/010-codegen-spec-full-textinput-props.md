## What to build

在 `SyncFormatEdittextViewNativeComponent.ts` 中全量声明 TextInput props + 自定义 `onSyncFormatChange`。

具体变更：
- 从 RN 的 `AndroidTextInputNativeComponent` 复制全部 props，转为 TypeScript codegen 格式
- 包括标准 TextInput props：autoCapitalize, keyboardType, maxLength, multiline, secureTextEntry, onChange, onChangeText, onFocus, onBlur, onSubmitEditing, onSelectionChange, onContentSizeChange, onKeyPress, onScroll, value, placeholder, mostRecentEventCount, selection, text, editable, style 等
- 自定义事件只保留 `onSyncFormatChange`（携带 `{ text: string, cursorPos: Double }`）
- 删除旧的单独声明的 `value`、`placeholder`（已包含在 TextInput props 中）

## Acceptance criteria

- [ ] codegen spec 包含 TextInput 的全部 props
- [ ] 自定义 props 只有 onSyncFormatChange
- [ ] `npx react-native codegen` 能生成对应的 native interface
- [ ] TypeScript 类型无报错

## Blocked by

None - can start immediately
