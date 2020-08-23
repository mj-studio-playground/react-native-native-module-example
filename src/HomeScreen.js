import React from 'react';
import {SafeAreaView} from 'react-native-safe-area-context';
import {Button} from 'react-native';

const HomeScreen = ({navigation: {navigate}}) => {
  const navigateCalculator = () => navigate('CalculatorScreen');
  const navigateMyText = () => navigate('MyTextScreen');

  return (
    <SafeAreaView style={{flex: 1}}>
      <Button title="Calculator (Native Module)" onPress={navigateCalculator} />
      <Button title="MyText (Native Component)" onPress={navigateMyText} />
    </SafeAreaView>
  );
};

export default HomeScreen;
