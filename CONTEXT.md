# Context

## 领域术语

- **SyncFormatEditText**: 受控组件，用户输入时同步调用 JS 格式化函数，格式化后回写 EditText
- **格式化函数**: `(text: string, cursorPos: number) => { text: string, cursorPos: number }` — 纯函数，输入文本+光标，返回格式化后文本+新光标位置
- **JSI HostFunction**: C++ 层存储的 JS 函数引用，可通过 JNI 从 Kotlin 同步调用

## 架构决策

1. **同步调用方式**: C++ TurboModule + JSI，原生通过 JNI → C++ → jsi::Function 调用 JS 格式化函数
2. **组件模式**: 受控组件（value + onChange prop）
3. **TurboModule**: 独立模块，暴露 setFormatFunction(fn) 和 format(text, cursorPos) 方法
4. **线程模型**: UI 线程 → invokeSync 阻塞调用 JS 线程（接受阻塞，假设格式化函数 <5ms）
5. **Props 最小集**: value, onChange, color, placeholder
