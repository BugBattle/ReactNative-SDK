package com.reactnativebugbattlesdk;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import bugbattle.io.bugbattle.APPLICATIONTYPE;
import bugbattle.io.bugbattle.BugBattle;
import bugbattle.io.bugbattle.BugBattleActivationMethod;
import bugbattle.io.bugbattle.BugBattleNotInitialisedException;
import bugbattle.io.bugbattle.BugSentCallback;
import bugbattle.io.bugbattle.BugWillBeSentCallback;
import bugbattle.io.bugbattle.CustomActionCallback;
import bugbattle.io.bugbattle.RequestType;

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
     *
     * @param sdkKey            sdk key from your project
     * @param activationMethods e.g. SHAKE
     */
    @ReactMethod
    public void initializeMany(String sdkKey, ReadableArray activationMethods) {
        BugBattle.getInstance().registerCustomAction(new CustomActionCallback() {
            @Override
            public void invoke(String message) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("customActionTriggered", obj.toString());
            }
        });
        try {
            BugBattle.getInstance().setApplicationType(APPLICATIONTYPE.REACTNATIVE);
            List<BugBattleActivationMethod> activationMethodsList = new LinkedList<>();
            for (Object activationMethod : activationMethods.toArrayList()) {
                if (activationMethod.equals("SHAKE")) {
                    activationMethodsList.add(BugBattleActivationMethod.SHAKE);
                    BugBattle.getInstance().setBugSentCallback(new BugSentCallback() {
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
            if (activity != null) {
                BugBattleActivationMethod[] bugBattleActivationMethods = activationMethodsList.toArray(new BugBattleActivationMethod[activationMethodsList.size()]);
                BugBattle.initWithToken(sdkKey, bugBattleActivationMethods, activity.getApplication(), activity);
            }

        } catch (Exception ex) {
        }
    }

    /**
     * Initialize Bugbattle with SDK Key and Activation Methode
     * For more information see https://docs.bugbattle.io
     *
     * @param sdkKey           sdk key from your project
     * @param activationMethod e.g. SHAKE
     */
    @ReactMethod
    public void initialize(String sdkKey, String activationMethod) {
        try {
            getReactApplicationContext().addLifecycleEventListener(new LifecycleEventListener() {
                @Override
                public void onHostResume() {
                    try {
                        Activity activity = getReactApplicationContext()
                                .getCurrentActivity();
                        if (activity != null) {
                            BugBattle.getInstance().setApplicationType(APPLICATIONTYPE.REACTNATIVE);
                            BugBattle.getInstance().setBugWillBeSentCallback(new BugWillBeSentCallback() {
                                @Override
                                public void flowInvoced() {
                                    getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("bugWillBeSent", null);
                                }
                            });
                            if (activationMethod.equals("SHAKE")) {
                                BugBattle.initWithToken(sdkKey, BugBattleActivationMethod.SHAKE, activity.getApplication());
                                BugBattle.getInstance().setBugSentCallback(new BugSentCallback() {
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
                                BugBattle.initWithToken(sdkKey, BugBattleActivationMethod.SCREENSHOT, activity.getApplication());
                            } else {
                                BugBattle.initWithToken(sdkKey, BugBattleActivationMethod.NONE, activity.getApplication());
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

            BugBattle.getInstance().startBugReporting();
            BugBattle.getInstance().setBugSentCallback(new BugSentCallback() {
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
     *
     * @param base64 Base64 encoded image
     */
    @ReactMethod
    public void startBugReportingWithImage(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        try {
            BugBattle.getInstance().startBugReporting(decodedByte);
        } catch (BugBattleNotInitialisedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manually start a silent bug reporting workflow.
     */
    @ReactMethod
    public void sendSilentBugReport(
            String senderEmail,
            String description,
            String priority
    ) {
        BugBattle.SEVERITY severity = BugBattle.SEVERITY.LOW;
        if (priority == "MEDIUM") {
            severity = BugBattle.SEVERITY.MIDDLE;
        }
        if (priority == "HIGH") {
            severity = BugBattle.SEVERITY.HIGH;
        }
        BugBattle.getInstance().sendSilentBugReport(senderEmail, description, severity);
    }

    /**
     * Used for dedicated server. Set the url, where bugs are reported to.
     *
     * @param apiUrl Url to the dedicated server.
     */
    @ReactMethod
    public void setApiUrl(String apiUrl) {
        try {
            BugBattle.getInstance().setApiUrl(apiUrl);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Set default email address filled in the send dialog.
     *
     * @param email email you want to be set
     */
    @ReactMethod
    public void setCustomerEmail(String email) {
        BugBattle.getInstance().setCustomerEmail(email);
    }

    /**
     * Change the color of the appearance of the UI.
     *
     * @param color hexcode for color
     */
    @ReactMethod
    public void setColor(String color) {
        BugBattle.getInstance().setNavigationTint(color);
    }

    /**
     * Set language of the bugbattle dialog. Available languages are "en", "fr", "de", "it", "nl". You
     * can override in the strings.xml and use your own language. For more informations see:
     * https://developer.android.com/training/basics/supporting-devices/languages
     *
     * @param language available are "en", "fr", "de", "it", "nl"
     */
    @ReactMethod
    public void setLanguage(String language) {
        BugBattle.getInstance().setLanguage(language);
    }

    /**
     * Enable Replay function. Can be set to true, but is only available in certain plans.
     * Please check https://www.bugbattle.io/pricing/
     *
     * @param enable enable replay function
     */
    @ReactMethod
    public void enableReplays(boolean enable) {
        if (enable) {
            try {
                Thread.sleep(1000);
                BugBattle.getInstance().enableReplays(enable);
            } catch (Exception ex) {

            }
        }
    }

    /**
     * Attaches custom data, which can be viewed in the BugBattle dashboard. New data will be merged with existing custom data.
     *
     * @param customData The data to attach to a bug report.
     * @author BugBattle
     */
    @ReactMethod
    public void appendCustomData(JSONObject customData) {
        try {
            BugBattle.getInstance().appendCustomData(customData);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Attach one key value pair to existing custom data.
     *
     * @param value The value you want to add
     * @param key   The key of the attribute
     * @author BugBattle
     */
    @ReactMethod
    public void setCustomData(String key, String value) {
        BugBattle.getInstance().setCustomData(key, value);
    }

    /**
     * Attach Data to the request. The Data will be merged into the body sent with the bugreport.
     * !!Existing keys can be overriten
     *
     * @param data Data, which is added
     */
    @ReactMethod
    public void attachData(JSONObject data) {
        try {
            BugBattle.getInstance().attachData(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Removes one key from existing custom data.
     *
     * @param key The key of the attribute
     * @author BugBattle
     */
    @ReactMethod
    public void removeCustomData(String key) {
        BugBattle.getInstance().removeCustomDataForKey(key);
    }

    /**
     * Clears all custom data.
     */
    @ReactMethod
    public void clearCustomData() {
        BugBattle.getInstance().clearCustomData();
    }

    /**
     * Log network traffic by logging it manually.
     *
     * @param networkLog Logs collected by rn
     */
    @ReactMethod
    public void attachNetworkLog(String networkLog) {
        try {
            JSONArray object = new JSONArray(networkLog);
            for (int i = 0; i < object.length(); i++) {
                JSONObject currentRequest = (JSONObject) object.get(i);
                JSONObject response = (JSONObject) currentRequest.get("response");
                JSONObject request = (JSONObject) currentRequest.get("request");
                BugBattle.getInstance().logNetwork(currentRequest.getString("url"), RequestType.valueOf(currentRequest.getString("type")), response.getInt("status"), currentRequest.getInt("duration"), request, response);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Enable privacy policies shown in the send dialog. The policies url can be
     * edited with {@methode setPrivacyPolicyUrl}
     *
     * @param enable if the value is true the privacy policies must be accepted by the user
     */
    @ReactMethod
    public void enablePrivacyPolicy(boolean enable) {
        BugBattle.getInstance().enablePrivacyPolicy(enable);
    }

    /**
     * Customize the url shown when clicked on the link. Highly recommended to add this url,
     * when {@methode enablePrivacyPolicy} is set to true;
     *
     * @param privacyUrl URL to your privacy policies
     */
    @ReactMethod
    public void setPrivacyPolicyUrl(String privacyUrl) {
        BugBattle.getInstance().setPrivacyPolicyUrl(privacyUrl);
    }

    /**
     * Enables or disables the powered by Bugbattle logo.
     * @author BugBattle
     *
     * @param enabled Enablesor disable the powered by Bugbattle logo.
     */
    @ReactMethod
    public void enablePoweredByBugbattle(boolean enabled) {
        BugBattle.getInstance().enablePoweredByBugbattle(enabled);
    }

    /**
     * Sets the main logo url.
     * @author BugBattle
     *
     * @param logoUrl The main logo url.
     */
    @ReactMethod
    public  void setLogoUrl(String logoUrl){
        BugBattle.getInstance().setLogoUrl(logoUrl);
    }

}