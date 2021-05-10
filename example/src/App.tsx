import * as React from 'react';
import { Image, StyleSheet, TouchableOpacity, View } from 'react-native';
import BugBattle from 'react-native-bugbattle-sdk';

const bugbattleLogo = require('./bugbattleLogo.png');

export default function App() {
  React.useEffect(() => {
    BugBattle.initialize('J4ADFNfzzCdYWr8NBO4rozcb6NFeyyES', BugBattle.SHAKE);

    // BugBattle.startNetworkLogging();
    // BugBattle.enableReplays(true);
    // BugBattle.setCustomerEmail("niklas@customemail.at");
    // BugBattle.attachCustomData();
    // BugBattle.enablePrivacyPolicy(true);
    // BugBattle.setPrivacyPolicyUrl("MYURL.com");
    // BugBattle.setApiUrl(apiUrl: string): void;
    // BugBattle.setLanguage('it');
    // BugBattle.sendSilentBugReport('hello@bugbattle.io', 'Awesome bug description.', 'LOW');
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.buttonContainer}
        onPress={() => {
          fetch(
            'https://run.mocky.io/v3/4ce4f0ac-4cbd-4ef7-93fc-5a5726e3a147'
          ).then((data) => {
            data.text().then((dataText) => {
              console.log(dataText);
            });
          });
        }}
      >
        <Image
          style={styles.logo}
          source={bugbattleLogo}
          resizeMode="contain"
        />
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
  buttonContainer: {
    width: '100%',
    height: 100,
    justifyContent: 'center',
    alignItems: 'center',
  },
  logo: {
    width: '65%',
    height: 100,
  },
});
