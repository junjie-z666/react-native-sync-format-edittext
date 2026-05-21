#include "FormatHostObject.h"
#include <sstream>
#include <vector>

namespace facebook::react {

FormatHostObject* FormatHostObject::instance_ = nullptr;

FormatHostObject::FormatHostObject(std::shared_ptr<CallInvoker> callInvoker)
    : callInvoker_(std::move(callInvoker)) {}

jsi::Value FormatHostObject::get(jsi::Runtime& rt, const jsi::PropNameID& name) {
  auto methodName = name.utf8(rt);

  if (methodName == "setFormat") {
    auto self = shared_from_this();
    return jsi::Function::createFromHostFunction(
        rt, name, 2,
        [self](jsi::Runtime& rt, const jsi::Value&, const jsi::Value* args, size_t count) -> jsi::Value {
          if (count < 2) {
            throw jsi::JSError(rt, "setFormat requires 2 arguments: viewTag, formatFn");
          }
          int viewTag = static_cast<int>(args[0].asNumber());
          auto fn = std::make_shared<jsi::Function>(args[1].asObject(rt).asFunction(rt));

          std::lock_guard<std::mutex> lock(self->mutex_);
          self->formatFns_[viewTag] = std::move(fn);
          return jsi::Value::undefined();
        });
  }

  if (methodName == "removeFormat") {
    auto self = shared_from_this();
    return jsi::Function::createFromHostFunction(
        rt, name, 1,
        [self](jsi::Runtime& rt, const jsi::Value&, const jsi::Value* args, size_t count) -> jsi::Value {
          if (count < 1) {
            throw jsi::JSError(rt, "removeFormat requires 1 argument: viewTag");
          }
          int viewTag = static_cast<int>(args[0].asNumber());
          self->removeFormat(viewTag);
          return jsi::Value::undefined();
        });
  }

  return jsi::Value::undefined();
}

void FormatHostObject::set(jsi::Runtime&, const jsi::PropNameID&, const jsi::Value&) {}

void FormatHostObject::removeFormat(int viewTag) {
  std::lock_guard<std::mutex> lock(mutex_);
  formatFns_.erase(viewTag);
}

bool FormatHostObject::hasFormat(int viewTag) {
  std::lock_guard<std::mutex> lock(mutex_);
  return formatFns_.find(viewTag) != formatFns_.end();
}

std::string FormatHostObject::formatText(int viewTag, const std::string& text, int cursorPos) {
  // Copy shared_ptr OUTSIDE invokeSync to avoid deadlock
  std::shared_ptr<jsi::Function> fnPtr;
  {
    std::lock_guard<std::mutex> lock(mutex_);
    auto it = formatFns_.find(viewTag);
    if (it != formatFns_.end()) {
      fnPtr = it->second;
    }
  }
  // mutex_ released here — JS thread won't block

  if (!fnPtr) {
    std::ostringstream oss;
    oss << "{\"text\":\"" << text << "\",\"cursorPos\":" << cursorPos << "}";
    return oss.str();
  }

  std::string resultJson;

  callInvoker_->invokeSync([&resultJson, &text, cursorPos, fnPtr](jsi::Runtime& runtime) {
    try {
      auto result = fnPtr->call(runtime, {
          jsi::String::createFromUtf8(runtime, text),
          jsi::Value(cursorPos)});

      if (result.isObject()) {
        auto obj = result.asObject(runtime);
        auto resultText = obj.getProperty(runtime, "text").asString(runtime).utf8(runtime);
        auto resultCursor = static_cast<int>(obj.getProperty(runtime, "cursorPos").asNumber());

        std::ostringstream oss;
        oss << "{\"text\":\"";
        for (char c : resultText) {
          switch (c) {
            case '"': oss << "\\\""; break;
            case '\\': oss << "\\\\"; break;
            case '\n': oss << "\\n"; break;
            case '\r': oss << "\\r"; break;
            case '\t': oss << "\\t"; break;
            default: oss << c;
          }
        }
        oss << "\",\"cursorPos\":" << resultCursor << "}";
        resultJson = oss.str();
      } else {
        resultJson = "{\"text\":\"" + text + "\",\"cursorPos\":" + std::to_string(cursorPos) + "}";
      }
    } catch (...) {
      resultJson = "{\"text\":\"" + text + "\",\"cursorPos\":" + std::to_string(cursorPos) + "}";
    }
  });

  return resultJson;
}

} // namespace facebook::react
