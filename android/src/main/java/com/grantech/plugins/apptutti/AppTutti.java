package com.grantech.plugins.apptutti;

import androidx.annotation.NonNull;

import android.util.Log;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.apptutti.sdk.ApptuttiSDK;
import com.apptutti.sdk.IAdsListener;
import com.apptutti.sdk.IInitListener;
import com.apptutti.sdk.UserInfo;


/**
 * apptutti Mansour Djawadi 2021
 */
public class AppTutti implements FlutterPlugin, MethodCallHandler, ActivityAware {

    final String METHOD_INIT = "init";
    final String LISTENER_INIT = "initListener";

    final String INIT_EVENT = "initEvent";
    final String INIT_DATA = "initData";
    
    final String METHOD_AD = "ad";
    final String LISTENER_AD = "adListener";

    final String ADEVENT = "adEvent";
    final String ADEVENT_READY = "adEventReady";
    final String ADEVENT_SHOW = "adEventShow";
    final String ADEVENT_LOADED = "adEventLoaded";
    final String ADEVENT_COMPLETE = "adEventComplete";

    final String ADTYPE = "adType";
    final String ADTYPE_BANNER = "bannerAd";
    final String ADTYPE_REWARDED = "rewardedAd";
    final String ADTYPE_INTERSTITIAL = "interstitialAd";

    final String TAG = "tutti";
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private UserInfo userInfo;
    private MethodChannel channel;
    private FlutterActivity activity;
    private BinaryMessenger messenger;
    private final Map<String, MethodChannel> methodChannels = new HashMap<>();

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "app_tutti");
        channel.setMethodCallHandler(this);
        messenger = flutterPluginBinding.getBinaryMessenger();
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        //here we have access to activity
        activity = (FlutterActivity) binding.getActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    }

    @Override
    public void onDetachedFromActivity() {
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals(METHOD_INIT)) {
            //Initialize ApptuttiSDK
            ApptuttiSDK.getInstance().init(activity, new IInitListener() {
                @Override
                public void onInitialized(final UserInfo info) {
                    //Set init status to true
                    Map<String, Object> arguments = new HashMap<>();
                    arguments.put(INIT_EVENT, true);
                    arguments.put(INIT_DATA, info.toString());
                    invokeMethod(LISTENER_INIT, arguments);
                    log("Initialized with userInfo: " + userInfo);
                }

                @Override
                public void onInitializeFailed(final String message) {
                    //Print message or do proper thing according to initialize failed event.
                    Map<String, Object> arguments = new HashMap<>();
                    arguments.put(INIT_EVENT, false);
                    arguments.put(INIT_DATA, message);
                    invokeMethod(LISTENER_INIT, arguments);
                    log("Initialize failed with message: " + message);
                }
            });

            result.success(true);
            return;
        }
        if (call.method.equals(METHOD_AD)) {
            final String adEvent = (String) call.argument(ADEVENT);

            if (adEvent.equals(ADEVENT_READY)) {
                result.success(ApptuttiSDK.getInstance().isAdsEnabled());
            } else if (adEvent.equals(ADEVENT_SHOW)) {

                final String adType = (String) call.argument(ADTYPE);
                if (adType.equals(ADTYPE_BANNER)) {
                    ApptuttiSDK.getInstance().bannerAd();
                } else if (adType.equals(ADTYPE_INTERSTITIAL)) {
                    ApptuttiSDK.getInstance().interstitialAd();
                } else {
                    showVideoAd();
                }

                result.success(true);
            }
            return;
        }

        result.notImplemented();
    }

    private void showVideoAd() {
        ApptuttiSDK.getInstance().rewardedVideoAd(new IAdsListener() {
            @Override
            // Call back when the ad is loaded successfully
            public void onAdsLoaded() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put(ADTYPE, ADTYPE_REWARDED);
                arguments.put(ADEVENT, ADEVENT_LOADED);
                invokeMethod(LISTENER_AD, arguments);
                log("onAdsLoaded");
            }

            @Override
            // call back when player complete watching the video ad
            public void onAdsComplete() {
                Map<String, Object> arguments = new HashMap<>();
                arguments.put(ADTYPE, ADTYPE_REWARDED);
                arguments.put(ADEVENT, ADEVENT_COMPLETE);
                invokeMethod(LISTENER_AD, arguments);
                //this is just sample function, please replace with your
                log("onAdsComplete");
            }
        });
    }

    private void invokeMethod(String methodName, Map<String, Object> arguments) {
        channel.invokeMethod(methodName, arguments);
        findChannel(methodName).invokeMethod(methodName, arguments);
    }

    private MethodChannel findChannel(String methodName) {
        if (methodChannels.containsKey(methodName)) {
            return methodChannels.get(methodName);
        }
        MethodChannel methodChannel = new MethodChannel(messenger, methodName);
        methodChannels.put(methodName, methodChannel);
        return methodChannel;
    }

    private void log(String text) {
        Log.d(TAG, text);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
