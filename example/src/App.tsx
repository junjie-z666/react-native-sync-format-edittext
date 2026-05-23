import { useState, useCallback } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SyncFormatEdittextView } from 'react-native-sync-format-edittext';

function formatPhone(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '').slice(0, 11);
  let formatted = '';
  let newCursorPos = cursorPos;
  const connectSignal = '-';
  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = `${digits.slice(0, 3)}${connectSignal}${digits.slice(3)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
  } else {
    formatted = `${digits.slice(0, 3)}${connectSignal}${digits.slice(3, 7)}${connectSignal}${digits.slice(7)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
    if (cursorPos > 7) newCursorPos = cursorPos + 1;
  }

  return {
    text: formatted,
    cursorPos: Math.min(newCursorPos, formatted.length),
  };
}

function formatLettersOnly(text: string, cursorPos: number) {
  const letters = text.replace(/[^a-zA-Z]/g, '').slice(0, 10);
  let lettersBefore = 0;
  for (let i = 0; i < Math.min(cursorPos, text.length); i++) {
    if (/[a-zA-Z]/.test(text[i]!)) lettersBefore++;
  }
  return {
    text: letters,
    cursorPos: Math.min(lettersBefore, letters.length),
  };
}

export default function App() {
  const [phone, setPhone] = useState('');
  const [letters, setLetters] = useState('');

  const handlePhoneChange = useCallback((text: string, _cursorPos: number) => {
    setPhone(text);
  }, []);

  const handleLettersChange = useCallback(
    (text: string, _cursorPos: number) => {
      setLetters(text);
    },
    []
  );

  return (
    <View style={styles.container}>
      <Text style={styles.title}>电话号码格式化</Text>
      <SyncFormatEdittextView
        value={phone}
        format={formatPhone}
        onChange={handlePhoneChange}
        placeholder="请输入电话号码"
        style={styles.input}
      />
      <Text style={styles.preview}>原始值: {phone}</Text>

      <Text style={[styles.title, { marginTop: 24 }]}>仅字母输入</Text>
      <SyncFormatEdittextView
        value={letters}
        format={formatLettersOnly}
        onChange={handleLettersChange}
        placeholder="请输入字母"
        style={styles.input}
      />
      <Text style={styles.preview}>原始值: {letters}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
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
    marginTop: 16,
    fontSize: 14,
    color: '#666666',
  },
});
