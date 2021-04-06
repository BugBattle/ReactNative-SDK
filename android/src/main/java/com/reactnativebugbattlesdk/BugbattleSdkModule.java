package com.reactnativebugbattlesdk;

import android.app.Activity;
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
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.BugSentCallback;
import bugbattle.io.bugbattle.BugWillBeSentCallback;
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

    /**
     * Initialize Bugbattle with SDK Key and Activation Methode
     * For more information see https://docs.bugbattle.io
     * @param sdkKey sdk key from your project
     * @param activationMethods e.g. SHAKE
     */
    @ReactMethod
    public void initializeMany(String sdkKey, ReadableArray activationMethods) {
        try {
            BugBattle.setApplicationType(APPLICATIONTYPE.REACTNATIVE);
            List<BugBattleActivationMethod> activationMethodsList = new LinkedList<>();
            for (Object activationMethod : activationMethods.toArrayList()) {
                if (activationMethod.equals("SHAKE")) {
                    activationMethodsList.add(BugBattleActivationMethod.SHAKE);
                    BugBattle.setBugSentCallback(new BugSentCallback() {
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
            Activity activity = getReactApplicationContext().getCurrentActivity();
            if(activity != null ) {
                BugBattle.initialise(sdkKey, activationMethodsList.toArray(new BugBattleActivationMethod[activationMethodsList.size()]), activity.getApplication());
            }

        } catch (Exception ex) {
        }
    }

    /**
     * Initialize Bugbattle with SDK Key and Activation Methode
     * For more information see https://docs.bugbattle.io
     * @param sdkKey sdk key from your project
     * @param activationMethod e.g. SHAKE
     */
    @ReactMethod
    public void initialize(String sdkKey, String activationMethod) {
        System.out.println("HEY DU");
        try {
            getReactApplicationContext().addLifecycleEventListener(new LifecycleEventListener() {
                @Override
                public void onHostResume() {
                    try {
                        Activity activity = getReactApplicationContext()
                                .getCurrentActivity();
                        if (activity != null) {
                            BugBattle.setApplicationType(APPLICATIONTYPE.REACTNATIVE);
                            BugBattle.setBugWillBeSentCallback(new BugWillBeSentCallback() {
                                @Override
                                public void flowInvoced() {
                                    getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("bugWillBeSent", null);
                                }
                            });
                            if (activationMethod.equals("SHAKE")) {
                                BugBattle.initialise(sdkKey, BugBattleActivationMethod.SHAKE, activity.getApplication());
                                BugBattle.setBugSentCallback(new BugSentCallback() {
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
                                BugBattle.initialise(sdkKey, BugBattleActivationMethod.SCREENSHOT, activity.getApplication());
                            } else {
                                BugBattle.initialise(sdkKey, BugBattleActivationMethod.NONE, activity.getApplication());
                            }
                        }
                    } catch (Exception ex) {
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

    /**
     * Show dev menu after shaking the phone.
     */
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

    /**
     * Start bug report manually by calling this function.
     */
    @ReactMethod
    public void startBugReporting() {
        try {

            BugBattle.setBugSentCallback(new BugSentCallback() {
                @Override
                public void close() {
                    //Dont open dev menu
                }
            });
            BugBattle.startBugReporting();
            BugBattle.setBugSentCallback(new BugSentCallback() {
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

    /**
     * Start bug report with a custom image.
     * @param base64 Base64 encoded image
     */
    @ReactMethod
    public void startBugReportingWithImage(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        BugBattle.startBugReporting(decodedByte);
    }

    /**
     * Not implemented for now on Android
     * @param jsonObject
     */
    @ReactMethod
    public void attachCustomData(ReadableMap jsonObject) {
 /*      try {
            BugBattle.attachCustomData(jsonObject);
        } catch (Exception e) {
            System.out.println(e);
        }*/
    }

    /**
     * Set default email address filled in the send dialog.
     * @param email email you want to be set
     */
    @ReactMethod
    public void setCustomerEmail(String email) {
        BugBattle.setCustomerEmail(email);
    }

    /**
     * Enable privacy policies shown in the send dialog. The policies url can be
     * edited with {@methode setPrivacyPolicyUrl}
     * @param enable if the value is true the privacy policies must be accepted by the user
     */
    @ReactMethod
    public void enablePrivacyPolicy(boolean enable) {
        BugBattle.enablePrivacyPolicy(enable);
    }

    /**
     * Customize the url shown when clicked on the link. Highly recommended to add this url,
     * when {@methode enablePrivacyPolicy} is set to true;
     * @param privacyUrl URL to your privacy policies
     */
    @ReactMethod
    public void setPrivacyPolicyUrl(String privacyUrl) {
        BugBattle.setPrivacyPolicyUrl(privacyUrl);
    }

    /**
     * Used for dedicated server. Set the url, where bugs are reported to.
     * @param apiUrl Url to the dedicated server.
     */
    @ReactMethod
    public void setApiUrl(String apiUrl) {
        try {
            BugBattle.setApiURL(apiUrl);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Enable Replay function. Can be set to true, but is only available in certain plans.
     * Please check https://www.bugbattle.io/pricing/
     * @param enable enable replay function
     */
    @ReactMethod
    public void enableReplays(boolean enable) {
        if (enable) {
            try {
                Thread.sleep(1000);
                BugBattle.enableReplay();
            } catch (Exception ex) {

            }
        }
    }

    /**
     * Set language of the bugbattle dialog. Available languages are "en", "fr", "de", "it". You
     * can override in the strings.xml and use your own language. For more informations see:
     * https://developer.android.com/training/basics/supporting-devices/languages
     * @param language available are "en", "fr", "de", "it"
     */
    @ReactMethod
    public void setLanguage(String language) {
        BugBattle.setLanguage(language);
    }

    /**
     * Add network data to bugbattle.
     * @param networkLog network log
     */
    @ReactMethod
    public void attachNetworkLog(String networkLog) {
        try {
            JSONArray object = new JSONArray(networkLog);
            System.out.println(object);
            JSONObject networkObj = new JSONObject();
            networkObj.put("networkLogs", object);
            BugBattle.attachData(networkObj);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}