package com.reactnativebugbattlesdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.CloseCallback;
import bugbattle.io.bugbattle.controller.BugBattleActivationMethod;
import bugbattle.io.bugbattle.model.APPLICATIONTYPE;

public class BugbattleSdkModule extends ReactContextBaseJavaModule {


    public BugbattleSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "BugbattleSdk";
    }


    @ReactMethod
    public void initializeMany(String sdkKey, ReadableArray activationMethods) {
        try {
            getReactApplicationContext().addLifecycleEventListener(new LifecycleEventListener() {
                @Override
                public void onHostResume() {
                    BugBattle.setApplicationType(APPLICATIONTYPE.REACTNATIVE);
                    List<BugBattleActivationMethod> activationMethodsList = new LinkedList<>();
                    for (Object activationMethod : activationMethods.toArrayList()) {
                        if (activationMethod.equals("SHAKE")) {
                            activationMethodsList.add(BugBattleActivationMethod.SHAKE);
                            BugBattle.setCloseCallback(new CloseCallback() {
                                @Override
                                public void close() {
                                    new java.util.Timer().schedule(
                                            new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                    showDevMenu();
                                                }
                                            },
                                            500
                                    );
                                }
                            });
                        } else if (activationMethod.equals("SCREENSHOT")) {
                            activationMethodsList.add(BugBattleActivationMethod.SCREENSHOT);
                        }
                    }
                    BugBattle.initialise(sdkKey, activationMethodsList.toArray(new BugBattleActivationMethod[activationMethodsList.size()]), getReactApplicationContext().getCurrentActivity().getApplication());
                }

                @Override
                public void onHostPause() {

                }

                @Override
                public void onHostDestroy() {

                }
            });
            }catch (Exception ex) {}
    }

    @ReactMethod
    public void initialize(String sdkKey, String activationMethod) {
        try {
            getReactApplicationContext().addLifecycleEventListener(new LifecycleEventListener() {
                @Override
                public void onHostResume() {
                    BugBattle.setApplicationType(APPLICATIONTYPE.REACTNATIVE);
                    if (activationMethod.equals("SHAKE")) {
                        BugBattle.initialise(sdkKey, BugBattleActivationMethod.SHAKE, getReactApplicationContext()
                                .getCurrentActivity()
                                .getApplication());
                        BugBattle.setCloseCallback(new CloseCallback() {
                            @Override
                            public void close() {
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                showDevMenu();
                                            }
                                        },
                                        500
                                );
                            }
                        });
                    } else if (activationMethod.equals("SCREENSHOT")) {
                        BugBattle.initialise(sdkKey, BugBattleActivationMethod.SCREENSHOT, getReactApplicationContext()
                                .getCurrentActivity()
                                .getApplication());
                    } else {
                        BugBattle.initialise(sdkKey, BugBattleActivationMethod.NONE, getReactApplicationContext()
                                .getCurrentActivity()
                                .getApplication());
                    }
                }

                @Override
                public void onHostPause() {

                }

                @Override
                public void onHostDestroy() {

                }
            });

        } catch (Exception err) {

        }
    }

    private void showDevMenu() {
        final ReactApplication application = (ReactApplication) getReactApplicationContext()
                .getCurrentActivity()
                .getApplication();
        Handler mainHandler = new Handler(this.getReactApplicationContext().getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    application
                            .getReactNativeHost()
                            .getReactInstanceManager()
                            .getDevSupportManager()
                            .showDevOptionsDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    @ReactMethod
    public void startBugReporting() {
        try {

            BugBattle.setCloseCallback(new CloseCallback() {
                @Override
                public void close() {
                    //Dont open dev menu
                }
            });
            BugBattle.startBugReporting();
            BugBattle.setCloseCallback(new CloseCallback() {
                @Override
                public void close() {
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    showDevMenu();
                                }
                            },
                            500
                    );
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @ReactMethod
    public void startBugReportingWithImage(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        BugBattle.startBugReporting(decodedByte);
    }

    @ReactMethod
    public void attachCustomData(JSONObject jsonObject) {
        try {
            BugBattle.attachCustomData(jsonObject);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @ReactMethod
    public void setCustomerEmail(String email) {
        BugBattle.setCustomerEmail(email);
    }

    @ReactMethod
    public void enablePrivacyPolicy(boolean enable) {
        BugBattle.enablePrivacyPolicy(enable);
    }

    @ReactMethod
    public void setPrivacyPolicyUrl(String privacyUrl) {
        BugBattle.setPrivacyPolicyUrl(privacyUrl);
    }

    @ReactMethod
    public void setApiURL(String apiUrl) {
        try {
            BugBattle.setApiURL(apiUrl);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @ReactMethod
    public void enableReplays(boolean enable) {
        if (enable) {
            BugBattle.enableReplay();
        }
    }

    @ReactMethod
    public void setLanguage(String language) {
        BugBattle.setLanguage(language);
    }
}