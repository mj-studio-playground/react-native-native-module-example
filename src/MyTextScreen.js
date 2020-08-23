import React, {useState, useRef} from 'react';
import {SafeAreaView} from 'react-native-safe-area-context';
import MyText from './nativeViewModules/MyText';
import {Alert, Button} from 'react-native';

const MyTextScreen = ({}: MyTextScreenProps) => {
  const textRef = useRef(null);

  const [text, setText] = useState('Hello Native Component!');

  const changeTextWithRef = () => {
    textRef.current?.setText(['Hello', 'Bye'][Math.floor(Math.random() + 0.5)]);
  };

  return (
    <SafeAreaView>
      <Button
        title="Change text with direct manipulation"
        onPress={changeTextWithRef}
      />
      <MyText
        ref={textRef}
        text={text}
        onTextChanged={(text) => {
          setText(text);
          Alert.alert(text);
        }}
      />
    </SafeAreaView>
  );
};

export default MyTextScreen;
