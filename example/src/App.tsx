import * as React from 'react';
import { Image, StyleSheet, TouchableOpacity, View } from 'react-native';
import BugBattle from 'react-native-bugbattle-sdk';

const bugbattleLogo = require('./bugbattleLogo.png');

export default function App() {
  React.useEffect(() => {
    BugBattle.autoConfigure('J4ADFNfzzCdYWr8NBO4rozcb6NFeyyES');

    BugBattle.setCustomData('no', 'asdf');
    BugBattle.setCustomData('email', 'luki@asdf.de');
    BugBattle.removeCustomData('no');

    BugBattle.logEvent('teest', {});
    BugBattle.logEvent('teest', {
      username: 'Franz',
      age: 18,
    });

    BugBattle.registerCustomAction((data) => {
      console.log(data.name);
    });
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

              BugBattle.sendSilentBugReport(
                'hello@bugbattle.io',
                'Awesome bug description.',
                'LOW'
              );
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
