import { View, type ViewProps } from 'react-native';

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

export function SyncFormatEdittextView({ style, ...rest }: Props) {
  return <View {...rest} style={style} />;
}
