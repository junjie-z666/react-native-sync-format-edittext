import { useState } from 'react';
import { View, Text, StyleSheet, TextInput } from 'react-native';
import { SyncFormatEdittextView } from 'react-native-sync-format-edittext';

function formatPhone(text: string, cursorPos: number) {
  const beforeCursor = text.slice(0, cursorPos);
  const removedBeforeCursor = beforeCursor.replace(/\d/g, '').length;
  const adjustedPos = cursorPos - removedBeforeCursor;

  const digits = text.replace(/\D/g, '').slice(0, 11);
  let formatted = '';
  let newCursorPos = adjustedPos;
  const sep = '-';
  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = `${digits.slice(0, 3)}${sep}${digits.slice(3)}`;
    if (adjustedPos > 3) newCursorPos = adjustedPos + 1;
  } else {
    formatted = `${digits.slice(0, 3)}${sep}${digits.slice(3, 7)}${sep}${digits.slice(7)}`;
    if (adjustedPos > 3) newCursorPos = adjustedPos + 1;
    if (adjustedPos > 7) newCursorPos = adjustedPos + 2;
  }
  return {
    text: formatted,
    cursorPos: Math.min(newCursorPos, formatted.length),
  };
}

function formatLettersOnly(text: string, cursorPos: number) {
  const letters = text.replace(/[^a-zA-Z]/g, '');
  const beforeCursor = text.slice(0, cursorPos);
  const removedBeforeCursor = beforeCursor.replace(/[a-zA-Z]/g, '').length;
  const newPos = cursorPos - removedBeforeCursor;
  return { text: letters, cursorPos: Math.min(newPos, letters.length) };
}

function formatLettersWithTextInput(text: string) {
  return text.replace(/[^a-zA-Z]/g, '');
}

export default function App() {
  const [nativeLetters, setNativeLetters] = useState('');
  const [syncLetters, setSyncLetters] = useState('');
  const [phone, setPhone] = useState('');

  return (
    <View style={styles.container}>
      <Text style={styles.title}>格式化输入对比</Text>

      <Text style={styles.label}>TextInput 只允许字母（闪烁）</Text>
      <TextInput
        value={nativeLetters}
        onChangeText={(text) =>
          setNativeLetters(formatLettersWithTextInput(text))
        }
        placeholder="输入字母试试"
        style={styles.input}
      />
      <Text style={styles.preview}>{nativeLetters}</Text>

      <Text style={styles.label}>SyncFormatEditText 只允许字母（无闪烁）</Text>
      <SyncFormatEdittextView
        value={syncLetters}
        format={formatLettersOnly}
        onChangeText={setSyncLetters}
        placeholder="输入字母试试"
        style={styles.input}
      />
      <Text style={styles.preview}>{syncLetters}</Text>

      <Text style={styles.label}>手机号格式化 138-0013-8000</Text>
      <SyncFormatEdittextView
        value={phone}
        format={formatPhone}
        onChangeText={setPhone}
        placeholder="13800138000"
        keyboardType="number-pad"
        style={styles.input}
      />
      <Text style={styles.preview}>{phone}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    paddingTop: 60,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 24,
    textAlign: 'center',
  },
  label: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 4,
    marginTop: 16,
  },
  input: {
    width: '100%',
    height: 48,
    borderWidth: 1,
    borderColor: '#cccccc',
    borderRadius: 4,
    paddingHorizontal: 12,
    backgroundColor: '#ffffff',
  },
  preview: {
    marginTop: 4,
    fontSize: 13,
    color: '#666666',
  },
});
