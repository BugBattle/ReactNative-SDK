# react-native-bug-battle

## Getting started

`$ npm install react-native-bug-battle --save`

### Mostly automatic installation

`$ react-native link react-native-bug-battle`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-bug-battle` and add `RNBugBattle.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNBugBattle.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.RNBugBattlePackage;` to the imports at the top of the file
  - Add `new RNBugBattlePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-bug-battle'
  	project(':react-native-bug-battle').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-bug-battle/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-bug-battle')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNBugBattle.sln` in `node_modules/react-native-bug-battle/windows/RNBugBattle.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Bug.Battle.RNBugBattle;` to the usings at the top of the file
  - Add `new RNBugBattlePackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNBugBattle from 'react-native-bug-battle';

// TODO: What to do with the module?
RNBugBattle;
```
  