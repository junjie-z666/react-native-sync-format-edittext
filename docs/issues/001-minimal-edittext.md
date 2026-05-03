# Issue 1: 最小可渲染 EditText

## What to build

将现有的 scaffold View 替换为 AppCompatEditText，使其能在 example app 中渲染一个可交互的文本输入框。更新 codegen spec 的 NativeProps（移除 color 的特殊处理，确保 ViewProps 继承正确），ViewManager 注册为 SimpleViewManager，View 继承 AppCompatEditText。

## Acceptance criteria

- [ ] SyncFormatEditextView 继承 AppCompatEditText（不是 android.view.View）
- [ ] example app 中渲染出一个可输入的 EditText
- [ ] 支持标准 View style props（width, height 等）
- [ ] codegen 正常生成，无编译错误

## Blocked by

None - can start immediately
