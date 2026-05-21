# Context

## 领域术语

- **SyncFormatEditText**: 受控组件，用户输入时通过 JS 格式化函数格式化后显示
- **格式化函数**: `(text: string, cursorPos: number) => { text: string, cursorPos: number }` — 纯同步函数，输入文本+光标，返回格式化后文本+新光标位置
- **输入回退**: TextWatcher 检测到输入后，立刻用 `Editable.replace()` 回退到上次格式化值，防止未格式化文本闪现

## 架构决策

1. **组件模式**: 受控组件（value + onChange prop），format 函数在 JS 侧执行
2. **防闪烁**: TextWatcher 中 `isReverting` flag + `Editable.replace()` 回退到上次格式化值
3. **Props 最小集**: value, onChange, color, placeholder
4. **线程模型（旧）**: 异步 — 原生发 onChange 事件到 JS，JS 格式化后通过 value prop 回写
5. **线程模型（新）**: JSI 同步 — TextWatcher 通过 JNI → C++ HostObject → `invokeSync` 直接调用 JS format 函数，结果同步返回
6. **顺序保证**: 同步模式下无需队列保证；异步模式依赖 RN FIFO 事件队列
7. **Android 架构兼容**: Gradle source set 分离（main/newarch/oldarch），根据宿主 app 的 `newArchEnabled` 属性切换编译。JS 侧 `codegenNativeComponent` 自带老架构兼容，不需改动
8. **格式化函数注册**: JS 侧 `useEffect` 后通过 `global.__formatModule.setFormat(viewTag, fn)` 注册，viewTag 关联到具体 View 实例
9. **错误处理**: invokeSync 失败时回退到不格式化，直接显示原始输入
10. **受控模型**: 保留 value prop，TextWatcher 同步更新后跳过重复的 value prop 写入
11. **TextInput 继承（JS）**: 组件接受完整 TextInputProps，code gen spec 从 RN 导入 TextInput 原生 props 类型。JS 封装层对外暴露 onChange（兼容 TextInput 签名）、onChangeText、onSyncFormatChange 三个事件
12. **TextInput 继承（Android）**: View 继承 ReactEditText 替代 AppCompatEditText；ViewManager 继承 ReactTextInputManager 替代 SimpleViewManager，自动获得全部 TextInput 原生 props
13. **TextWatcher 协作**: 我们的 TextWatcher 在 afterTextChanged 中格式化（Editable.replace），React 的 ReactTextInputTextWatcher 自动把格式化后文本作为事件发到 JS，不需额外事件分发
14. **组件名**: 保持 SyncFormatEdittextView，不改为 TextInput
