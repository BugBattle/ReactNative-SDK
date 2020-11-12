import { NativeModules } from 'react-native';

const { BugbattleSdk } = NativeModules;

BugbattleSdk.NONE = "NONE";
BugbattleSdk.SHAKE = "SHAKE";
BugbattleSdk.THREE_FINGER_DOUBLE_TAB = "THREE_FINGER_DOUBLE_TAB";

export default BugbattleSdk;