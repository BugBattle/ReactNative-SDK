/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import BugBattle from 'react-native-bugbattle';

AppRegistry.registerComponent(appName, () => App);

// Initialize the SDK
BugBattle.initWithToken('YOUR_API_KEY', BugBattle.THREE_FINGER_DOUBLE_TAB);
