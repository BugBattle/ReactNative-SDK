/**
 * Sample React Native App
 *
 * adapted from App.js generated by the following command:
 *
 * react-native init example
 *
 * https://github.com/facebook/react-native
 */

import React, { Component } from 'react';
import {  Image, StyleSheet, Text, View } from 'react-native';
import BugBattle from 'react-native-bugbattle';

import bugbattleLogo from './bugbattleLogo.png';

export default class App extends Component<{}> {
  componentDidMount() {
    console.log(BugBattle);
    BugBattle.initWithToken('YOUR_API_KEY', BugBattle.THREE_FINGER_DOUBLE_TAB);
  }

  render() {
    return (
      <View style={styles.container}>
        <Image style={styles.logo} source={bugbattleLogo} resizeMode="contain" />
      </View>
    );
  }
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
