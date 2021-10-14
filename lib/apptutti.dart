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
}
