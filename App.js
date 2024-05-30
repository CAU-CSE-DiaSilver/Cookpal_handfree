// App.js

import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Button, View, Text } from 'react-native';

import Recipe_view from './HandDetect/MyView'
import TEST from './HandDetect/test'

// Home Screen Component
function HomeScreen({ navigation }) {
  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>Home Screen</Text>
      <Button
        title="Go to Recipe_view"
        onPress={() => navigation.navigate('Recipe_view')}
      />

      <Button
        title="Go to Recipe_view"
        onPress={() => navigation.navigate('TEST')}
      />
    </View>
  );
}

const Stack = createNativeStackNavigator();

function App() {
  
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="Recipe_view" component={Recipe_view} />
        <Stack.Screen name="TEST" component={TEST} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
