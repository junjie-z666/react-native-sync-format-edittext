import { useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SyncFormatEdittextView } from 'react-native-sync-format-edittext';

function formatPhone(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '').slice(0, 11);
  let formatted = '';
  let newCursorPos = cursorPos;
  const sep = '-';
  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = `${digits.slice(0, 3)}${sep}${digits.slice(3)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
  } else {
    formatted = `${digits.slice(0, 3)}${sep}${digits.slice(3, 7)}${sep}${digits.slice(7)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
    if (cursorPos > 7) newCursorPos = cursorPos + 1;
  }
  return {
    text: formatted,
    cursorPos: Math.min(newCursorPos, formatted.length),
  };
}

function formatCreditCard(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '').slice(0, 16);
  return { text: digits, cursorPos: Math.min(cursorPos, digits.length) };
}

export default function App() {
  const [phone, setPhone] = useState('');
  const [card, setCard] = useState('');

  return (
    <View style={styles.container}>
      <Text style={styles.title}>多输入框格式化测试</Text>

      <Text style={styles.label}>电话号码</Text>
      <SyncFormatEdittextView
        value={phone}
        format={formatPhone}
        onChangeText={setPhone}
        placeholder="13800138000"
        style={styles.input}
      />
      <Text style={styles.preview}>{phone}</Text>
      <Text style={styles.label}>只能输入数字</Text>
      <SyncFormatEdittextView
        value={card}
        format={formatCreditCard}
        onChangeText={setCard}
        placeholder="4242424242424242"
        style={styles.input}
      />
      <Text style={styles.preview}>{card}</Text>
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
