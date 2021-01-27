import * as React from 'react';
import { Image, StyleSheet, TouchableOpacity, View, Text } from 'react-native';
import BugbattleSdk from 'react-native-bugbattle-sdk';

const bugbattleLogo = require('./bugbattleLogo.png');

export default function App() {
  React.useEffect(() => {
    // BugBattle.initialize('NF0AbayNnRfrT6QsB3uGAi6ANdd8WeX4', BugBattle.SCREENSHOT);
    BugbattleSdk.initializeMany('U1FeTUrxnzbtB8ebJj2unNweR6pzgIWg', [BugbattleSdk.SCREENSHOT, BugbattleSdk.SHAKE]);
    BugbattleSdk.enableReplays(true);
    BugbattleSdk.setCustomerEmail("niklas@");
    BugbattleSdk.enablePrivacyPolicy(true);
  }, []);

  return (
    <View style={styles.container}>
      <Image style={styles.logo} source={bugbattleLogo} resizeMode="contain" />
      <TouchableOpacity onPress={()=> {
        BugbattleSdk.startBugReporting();
      }}>
        <Text>HEY</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#70B9DA',
  },
  logo: {
    width: '65%',
    height: 100,
  },
});
