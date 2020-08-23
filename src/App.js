import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import HomeScreen from './HomeScreen';
import CalculatorScreen from './CalculatorScreen';
import MyTextScreen from './MyTextScreen';

const Stack = createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="HomeScreen" component={HomeScreen} />
        <Stack.Screen name="CalculatorScreen" component={CalculatorScreen} />
        <Stack.Screen name="MyTextScreen" component={MyTextScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
