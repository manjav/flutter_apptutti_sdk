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


/**
 * apptutti Mansour Djawadi 2021
 */
public class AppTutti implements FlutterPlugin, MethodCallHandler, ActivityAware {

    final String TAG = "tutti";
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
        result.notImplemented();
    }


    private void invokeMethod(String methodName, Map<String, Object> arguments) {
        channel.invokeMethod(methodName, arguments);
        findChannel(methodName).invokeMethod(methodName, arguments);
    }

    private MethodChannel findChannel(String methodName) {
        if (placementChannels.containsKey(methodName)) {
            return placementChannels.get(methodName);
        }
        MethodChannel methodChannel = new MethodChannel(messenger, methodName);
        placementChannels.put(methodName, methodChannel);
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
