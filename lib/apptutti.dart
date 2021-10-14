import 'dart:async';

import 'package:flutter/services.dart';

class Apptutti {

  static const METHOD_INIT = "init";
  static const LISTENER_INIT = "initListener";

  static const METHOD_AD = "ad";
  static const LISTENER_AD = "adListener";

  static const INIT_EVENT = "initEvent";
  static const INIT_DATA = "initData";

  static const ADEVENT = "adEvent";
  static const ADEVENT_READY = "adEventReady";
  static const ADEVENT_SHOW = "adEventShow";
  static const ADEVENT_LOADED = "adEventLoaded";
  static const ADEVENT_COMPLETE = "adEventComplete";

  static const ADTYPE = "adType";
  static const ADTYPE_BANNER = "bannerAd";
  static const ADTYPE_REWARDED = "rewardedAd";
  static const ADTYPE_INTERSTITIAL = "interstitialAd";

  static final Map<String, MethodChannel> _channels = {};

  static const MethodChannel _channel = const MethodChannel('app_tutti');

  static void init({Function(Map<dynamic, dynamic>)? listener}) {
    try {
      if (listener != null) {
        _channels
            .putIfAbsent(LISTENER_INIT, () => MethodChannel(LISTENER_INIT))
            .setMethodCallHandler((cm) => _methodCall(cm, listener));
      }
      _channel.invokeMethod(METHOD_INIT, {});
    } on PlatformException {
      return false;
    }
  }

  /// Check if sdk is ready to show ads
  ///
  ///  If true, sdk is ready to show ads
  static Future<bool?> isAdReady() async {
    final result = await _channel
        .invokeMethod(METHOD_AD, <String, dynamic>{ADEVENT: ADEVENT_READY});

    return result;
  }

  /// Show video ad, if ready.
  ///
  /// [type] select BANNER, REWARDED, INTERSTITIAL ad
  /// If true, placement are shown
  static Future<bool?> showAd(String type,
      {Function(Map<dynamic, dynamic>)? listener}) async {
    try {
    if (listener != null) {
      _channels
          .putIfAbsent(LISTENER_AD, () => MethodChannel(LISTENER_AD))
          .setMethodCallHandler((call) => _methodCall(call, listener));
    }
    final result = await _channel.invokeMethod(
        METHOD_AD, <String, dynamic>{ADTYPE: type, ADEVENT: ADEVENT_SHOW});
    return result;
    } on PlatformException {
      return false;
    }
  }

  static _methodCall(
      MethodCall methodCall, Function(Map<dynamic, dynamic> args)? listener) {
    listener?.call(methodCall.arguments);
    print("${methodCall.method} ${methodCall.arguments}");
  }
}
