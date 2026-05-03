# 0001: 使用异步事件 + 输入回退实现格式化

## 状态

已接受（替代了最初提议的 JSI 同步方案）

## 背景

用户输入时需要调用 JS 格式化函数，格式化后回写 EditText。最初计划用 C++ TurboModule + JSI 实现同步调用，但发现：

1. TurboModule 的 `Callback.invoke()` 是异步调度，无法从 sync 方法调用（死锁）
2. `CallInvoker::invokeSync()` 从 UI 线程调用有死锁风险（函数名本身标注 `CAN_DEADLOCK`）

## 决策

采用异步事件 + 输入回退方案：

- 原生 TextWatcher 检测输入后，立刻用 `Editable.replace()` 回退到上次格式化值
- 同时发 onChange 事件到 JS
- JS 执行格式化函数，通过 value prop 回写原生
- `isReverting` flag 防止 TextWatcher 递归

## 后果

- 无 C++ 代码，纯 Kotlin + TS 实现
- 输入后有 16-50ms 延迟才显示格式化结果（用户基本感知不到）
- 安全可靠，无 ANR/死锁风险
- 依赖 RN FIFO 事件队列保证顺序正确性
