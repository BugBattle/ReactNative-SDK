import { NativeModules } from 'react-native';

const { RNBugBattle } = NativeModules;

RNBugBattle.NONE = "NONE";
RNBugBattle.SHAKE = "SHAKE";
RNBugBattle.THREE_FINGER_DOUBLE_TAB = "THREE_FINGER_DOUBLE_TAB";

export default RNBugBattle;
