import React, {useState, useEffect} from 'react';

import {Text, TextInput, Button, ScrollView} from 'react-native';
import Calculator from './nativeModules/Calculator';

const CalculatorScreen = () => {
  const [first, setFirst] = useState('0');
  const [second, setSecond] = useState('0');

  const [resultWithPromise, setResultWithPromise] = useState(0);
  const [resultWithCallback, setResultWithCallback] = useState(0);
  const [resultWithListener, setResultWithListener] = useState(0);

  const addWithPromise = async () => {
    const result = await Calculator.addWithPromise(+first, +second);
    setResultWithPromise(result);
  };

  const addWithCallback = () => {
    Calculator.addWithCallback(+first, +second, setResultWithCallback);
  };

  const addWithListener = () => {
    Calculator.addWithListener(+first, +second);
  };

  useEffect(() => {
    Calculator.addResultListener(setResultWithListener);

    return Calculator.removeResultListener;
  }, []);

  return (
    <ScrollView style={{flex: 1}} contentContainerStyle={{flexGrow: 1}}>
      <TextInput value={first + ''} onChangeText={setFirst} />
      <TextInput value={second + ''} onChangeText={setSecond} />
      <Button title="Add with promise" onPress={addWithPromise} />
      <Button title="Add with callback" onPress={addWithCallback} />
      <Button title="Add with listener" onPress={addWithListener} />
      <Text>{`Promise result : ${resultWithPromise}`}</Text>
      <Text>{`Callback result : ${resultWithCallback}`}</Text>
      <Text>{`Listener result : ${resultWithListener}`}</Text>
    </ScrollView>
  );
};

export default CalculatorScreen;
