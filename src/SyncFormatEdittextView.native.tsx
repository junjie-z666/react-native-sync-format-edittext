import { useCallback } from 'react';
import type { ViewProps } from 'react-native';
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

export function SyncFormatEdittextView({
  value,
  placeholder,
  format,
  onChange,
  style,
  ...rest
}: Props) {
  const handleNativeChange = useCallback(
    (event: { nativeEvent: { text: string; cursorPos: number } }) => {
      const { text, cursorPos } = event.nativeEvent;
      if (format) {
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
      {...rest}
      style={style}
      value={value}
      placeholder={placeholder}
      onSyncFormatChange={handleNativeChange}
    />
  );
}
