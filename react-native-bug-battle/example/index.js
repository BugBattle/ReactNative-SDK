/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import RNBugBattle from 'react-native-bug-battle';

RNBugBattle.initWithToken("XXXXX", "SHAKE");
AppRegistry.registerComponent(appName, () => App);
