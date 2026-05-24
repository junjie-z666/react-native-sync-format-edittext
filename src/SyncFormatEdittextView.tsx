import { View, type TextInputProps } from 'react-native';

type FormatFn = (
  text: string,
  cursorPos: number
) => { text: string; cursorPos: number };

type Props = TextInputProps & {
  format?: FormatFn;
};

export function SyncFormatEdittextView({ style, ...rest }: Props) {
  return <View {...rest} style={style} />;
}
