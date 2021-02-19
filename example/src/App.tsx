import * as React from 'react';
import { Image, StyleSheet, View } from 'react-native';
import BugBattle from 'react-native-bugbattle-sdk';

const bugbattleLogo = require('./bugbattleLogo.png');

export default function App() {
  React.useEffect(() => {
    BugBattle.initialize('YOUR_API_KEY', BugBattle.SHAKE);

    // BugBattle.enableReplays(true);
    // BugBattle.setCustomerEmail("niklas@customemail.at");
    // BugBattle.attachCustomData();
    // BugBattle.enablePrivacyPolicy(true);
    // BugBattle.setPrivacyPolicyUrl("MYURL.com");
    // BugBattle.setApiUrl(apiUrl: string): void;
    // BugBattle.setLanguage('it');
  }, []);

  return (
    <View style={styles.container}>
      <Image style={styles.logo} source={bugbattleLogo} resizeMode="contain" />
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
