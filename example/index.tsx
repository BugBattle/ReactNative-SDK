import { AppRegistry } from 'react-native';
import BugBattle from 'react-native-bugbattle-sdk';
import App from './src/App';
import { name as appName } from './app.json';

BugBattle.autoConfigure('7qnF4SaW8daomwcBLdXAd8ahlIYJtxos');

AppRegistry.registerComponent(appName, () => App);
