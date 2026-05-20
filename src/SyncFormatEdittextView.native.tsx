import { useCallback, useEffect, useRef } from 'react';
import { findNodeHandle, TurboModuleRegistry, type ViewProps } from 'react-native';
import SyncFormatEdittextViewNativeComponent from './SyncFormatEdittextViewNativeComponent';

type FormatFn = (
  text: string,
  cursorPos: number
) => { text: string; cursorPos: number };

type Props = ViewProps & {
  value?: string;
  placeholder?: string;
  format?: FormatFn;
  onChange?: (text: string, cursorPos: number) => void;
};

// Install JSI bindings synchronously via @ReactMethod(isBlockingSynchronousMethod=true)
try {
  const fmtModule = TurboModuleRegistry.getEnforcing<{ install(): void }>('FormatModule');
  fmtModule.install();
} catch {}

const formatModule: {
  setFormat(viewTag: number, fn: FormatFn): void;
  removeFormat(viewTag: number): void;
} | undefined = (global as any).__formatModule;

export function SyncFormatEdittextView({
  value,
  placeholder,
  format,
  onChange,
  style,
  ...rest
}: Props) {
  const viewRef = useRef(null);

  // Register format function with native via JSI
  useEffect(() => {
    if (!format || !formatModule) return;
    const tag = findNodeHandle(viewRef.current);
    if (tag) {
      formatModule.setFormat(tag, format);
    }
    return () => {
      if (tag) {
        formatModule.removeFormat(tag);
      }
    };
  }, [format]);

  const handleNativeChange = useCallback(
    (event: { nativeEvent: { text: string; cursorPos: number } }) => {
      const { text, cursorPos } = event.nativeEvent;
      if (formatModule) {
        // JSI sync path: native already called format, text is formatted
        onChange?.(text, cursorPos);
      } else if (format) {
        // Fallback: format on JS side
        const result = format(text, cursorPos);
        onChange?.(result.text, result.cursorPos);
      } else {
        onChange?.(text, cursorPos);
      }
    },
    [format, onChange]
  );

  return (
    <SyncFormatEdittextViewNativeComponent
      ref={viewRef}
      {...rest}
      style={style}
      value={value}
      placeholder={placeholder}
      onSyncFormatChange={handleNativeChange}
    />
  );
}
