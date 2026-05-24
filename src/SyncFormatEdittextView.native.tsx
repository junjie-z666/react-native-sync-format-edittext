import { useCallback } from 'react';
import type { TextInputProps } from 'react-native';
import SyncFormatEdittextViewNativeComponent from './SyncFormatEdittextViewNativeComponent';

type FormatFn = (
  text: string,
  cursorPos: number
) => { text: string; cursorPos: number };

type SyncFormatEditTextProps = TextInputProps & {
  format?: FormatFn;
  onSyncFormatChange?: (text: string, cursorPos: number) => void;
};

export function SyncFormatEdittextView({
  value,
  format,
  onChange,
  onChangeText,
  onSyncFormatChange,
  ...rest
}: SyncFormatEditTextProps) {
  const isControlled = value !== undefined;

  const handleNativeChange = useCallback(
    (event: {
      nativeEvent: { text: string; cursorPos: number; target: number };
    }) => {
      const { text, cursorPos, target } = event.nativeEvent;

      if (format) {
        const result = format(text, cursorPos);
        onSyncFormatChange?.(result.text, result.cursorPos);
        onChange?.({ nativeEvent: { text: result.text, target } } as any);
        onChangeText?.(result.text);
      } else {
        onSyncFormatChange?.(text, cursorPos);
        onChange?.({ nativeEvent: { text, target } } as any);
        onChangeText?.(text);
      }
    },
    [format, onSyncFormatChange, onChange, onChangeText]
  );

  return (
    <SyncFormatEdittextViewNativeComponent
      {...(rest as any)}
      value={isControlled ? value : undefined}
      onSyncFormatChange={handleNativeChange}
    />
  );
}
