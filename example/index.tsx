import { AppRegistry } from 'react-native';
import BugBattle from 'react-native-bugbattle-sdk';
import App from './src/App';
import { name as appName } from './app.json';

BugBattle.autoConfigure('J4ADFNfzzCdYWr8NBO4rozcb6NFeyyES');

AppRegistry.registerComponent(appName, () => App);
