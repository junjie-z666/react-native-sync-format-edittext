#pragma once

#include <jsi/jsi.h>
#include <ReactCommon/CallInvoker.h>
#include <unordered_map>
#include <string>
#include <mutex>
#include <memory>

namespace facebook::react {

class FormatHostObject : public jsi::HostObject,
                         public std::enable_shared_from_this<FormatHostObject> {
public:
  FormatHostObject(std::shared_ptr<CallInvoker> callInvoker);
  ~FormatHostObject() override { if (instance_ == this) instance_ = nullptr; }

  jsi::Value get(jsi::Runtime& rt, const jsi::PropNameID& name) override;
  void set(jsi::Runtime& rt, const jsi::PropNameID& name, const jsi::Value& value) override;

  // Called from JNI (UI thread)
  std::string formatText(int viewTag, const std::string& text, int cursorPos);
  void removeFormat(int viewTag);
  bool hasFormat(int viewTag);

private:
  std::shared_ptr<CallInvoker> callInvoker_;
  // Use shared_ptr so we can copy out without holding lock during invokeSync
  std::unordered_map<int, std::shared_ptr<jsi::Function>> formatFns_;
  std::mutex mutex_;

  static FormatHostObject* instance_;
public:
  static void setInstance(FormatHostObject* inst) { instance_ = inst; }
  static FormatHostObject* getInstance() { return instance_; }
};

} // namespace facebook::react
