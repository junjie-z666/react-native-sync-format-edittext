# 0001: 使用 JSI 实现同步格式化调用

## 状态

已接受

## 背景

用户输入时需要同步调用 JS 格式化函数，格式化后回写 EditText。RN bridge 本质异步，标准 TurboModule Callback 无法从 sync 方法调用（死锁）。

## 决策

使用 C++ TurboModule + JSI HostFunction 实现真正的同步调用：

- C++ 层存储 `jsi::Function` 引用
- Kotlin 通过 JNI 调用 C++，C++ 通过 `invokeSync` 在 JS 线程执行函数
- 接受 UI 线程阻塞（假设格式化函数 <5ms）

## 后果

- 真同步，无延迟
- 需要写 C++ 代码（JNI + JSI）
- UI 线程阻塞风险（格式化函数慢会 ANR）
- 调试复杂度增加
