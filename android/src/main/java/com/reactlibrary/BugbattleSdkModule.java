package com.reactlibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONObject;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;

public class BugbattleSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public BugbattleSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "BugbattleSdk";
    }

     @ReactMethod
    public void initialise(String sdkKey, String activationMethod) {
        if(activationMethod.equals("SHAKE")) {
            BugBattle.initialise(sdkKey, BugBattleActivationMethod.SHAKE, getCurrentActivity());
        } else if(activationMethod.equals("THREE_FINGER_DOUBLE_TAB")) {
            BugBattle.initialise(sdkKey, BugBattleActivationMethod.THREE_FINGER_DOUBLE_TAB, getCurrentActivity());
        } else {
            BugBattle.initialise(sdkKey, BugBattleActivationMethod.NONE, getCurrentActivity());
        }
    }

    @ReactMethod
    public void startBugReporting() {
        try {
            BugBattle.startBugReporting();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @ReactMethod
    public void startBugReporting(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            BugBattle.startBugReporting(decodedByte);
    }

    @ReactMethod
    public void trackStep(String type, String data) {
        BugBattle.trackStep(type, data);
    }

    @ReactMethod
    public void attachCustomData(JSONObject jsonObject) {
        try {
            BugBattle.attachCustomData(jsonObject);
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    @ReactMethod
    public void setCustomerEmail(String email) {
        BugBattle.setCustomerEmail(email);
    }

    @ReactMethod
    public void enablePrivacyPolicy(boolean enable){
        BugBattle.enablePrivacyPolicy(enable);
    }

    @ReactMethod
    public void setPrivacyPolicyUrl(String privacyUrl){
        BugBattle.setPrivacyPolicyUrl(privacyUrl);
    }

    @ReactMethod
    public void setApiURL(String apiUrl){
        try {
            BugBattle.setApiURL(apiUrl);
        }catch (Exception e) {
            System.out.println(e);
        }
    }

}
