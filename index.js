import { NativeModules } from 'react-native';

const { BugbattleSdk } = NativeModules;

BugbattleSdk.NONE = "NONE";
BugbattleSdk.SHAKE = "SHAKE";

export default BugbattleSdk;