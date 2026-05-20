# Context

## 领域术语

- **SyncFormatEditText**: 受控组件，用户输入时通过 JS 格式化函数格式化后显示
- **格式化函数**: `(text: string, cursorPos: number) => { text: string, cursorPos: number }` — 纯同步函数，输入文本+光标，返回格式化后文本+新光标位置
- **输入回退**: TextWatcher 检测到输入后，立刻用 `Editable.replace()` 回退到上次格式化值，防止未格式化文本闪现

## 架构决策

1. **组件模式**: 受控组件（value + onChange prop），format 函数在 JS 侧执行
2. **防闪烁**: TextWatcher 中 `isReverting` flag + `Editable.replace()` 回退到上次格式化值
3. **Props 最小集**: value, onChange, color, placeholder
4. **线程模型**: 异步 — 原生发 onChange 事件到 JS，JS 格式化后通过 value prop 回写
5. **顺序保证**: RN FIFO 事件队列保证快速连续输入的顺序正确性
6. **Android 架构兼容**: Gradle source set 分离（main/newarch/oldarch），根据宿主 app 的 `newArchEnabled` 属性切换编译。JS 侧 `codegenNativeComponent` 自带老架构兼容，不需改动
