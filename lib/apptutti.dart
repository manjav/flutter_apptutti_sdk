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

  static const MethodChannel _channel = const MethodChannel('app_tutti');

  static void init() {
    _channel.invokeMethod(METHOD_INIT, {});
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
    if (listener != null) {
      _channels
          .putIfAbsent(LISTENER_AD, () => MethodChannel(LISTENER_AD))
          .setMethodCallHandler((call) => _methodCall(call, listener));
    }
    final result = await _channel.invokeMethod(
        METHOD_AD, <String, dynamic>{ADTYPE: type, ADEVENT: ADEVENT_SHOW});
    return result;
  }

}
