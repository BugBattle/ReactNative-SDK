import * as React from 'react';
import { Image, StyleSheet, TouchableOpacity, View } from 'react-native';
import BugBattle from 'react-native-bugbattle-sdk';
const axios = require('axios');
const bugbattleLogo = require('./bugbattleLogo.png');

export default function App() {
  React.useEffect(() => {
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

    BugBattle.setCustomerName('Niklas');
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={styles.buttonContainer}
        onPress={() => {
          axios
            .get(
              'https://run.mocky.io/v3/0e875619-73d1-4c96-8778-6ccc63152ca2xxx'
            )
            .then(function (response: any) {
              // handle success
              console.log(response.data);
            })
            .catch(function (error: any) {
              // handle error
              console.log(error);
            });

          fetch(
            'https://run.mocky.io/v3/995e3220-ee20-42dd-aba9-3ece59cb6c13'
          ).then(() => {
            console.log('asdf');
          });

          axios
            .get('https://run.mocky.io/v3/995e3220-ee20-42dd-aba9-3ece59cb6c13')
            .then(function (response: any) {
              // handle success
              console.log(response.data);
            })
            .catch(function (error: any) {
              // handle error
              console.log(error);
            });

          function reqListener() {
            // @ts-ignore
            console.log(this.responseText);
          }

          var oReq = new XMLHttpRequest();
          oReq.addEventListener('load', reqListener);
          oReq.open(
            'GET',
            'https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg'
          );
          oReq.send();

          var oReq = new XMLHttpRequest();
          oReq.addEventListener('load', reqListener);
          oReq.open(
            'GET',
            'https://run.mocky.io/v3/995e3220-ee20-42dd-aba9-3ece59cb6c13'
          );
          oReq.send();
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
