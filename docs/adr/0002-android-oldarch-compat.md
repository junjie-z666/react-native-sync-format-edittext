# 0002 - Android 老架构兼容

## 状态

已接受

## 背景

项目当前仅支持 React Native 新架构（Fabric）。需要兼容老架构（Bridge/Paper），且不改动现有新架构实现。

## 决策

采用 Gradle source set 分离方案：

- `android/src/main/` — 共享代码（`SyncFormatEdittextView.kt`，纯 Android UI）
- `android/src/newarch/` — 新架构专用（ViewManager + Event + Package，使用 codegen delegate/UIManagerHelper）
- `android/src/oldarch/` — 老架构专用（ViewManager + Event + Package，使用手动 @ReactProp/EventDispatcher）

`build.gradle` 根据宿主 app 的 `newArchEnabled` 属性选择 source set。JS 侧不改动（`codegenNativeComponent` 自带兼容层）。

## 原因

1. **零侵入** — 新架构代码原封不动，避免回归风险
2. **标准模式** — React Native 社区库普遍采用 source set 分离
3. **编译时切换** — 不存在运行时开销，老架构 app 不会编译新架构代码

## 备选方案

- 运行时兼容层：增加复杂度，有性能开销
- 条件编译（if/else）：代码混乱，维护困难
