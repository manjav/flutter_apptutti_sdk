import 'dart:async';

import 'package:flutter/services.dart';

class Apptutti {

  static const METHOD_INIT = "init";
  static const LISTENER_INIT = "initListener";

  static const INIT_EVENT = "initEvent";
  static const INIT_DATA = "initData";



  static const MethodChannel _channel = const MethodChannel('app_tutti');

  static void init() {
    _channel.invokeMethod(METHOD_INIT, {});
  }
}
