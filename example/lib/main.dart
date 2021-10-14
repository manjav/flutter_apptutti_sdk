import 'package:app_tutti/apptutti.dart';
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
            appBar: AppBar(title: const Text('App-tutti Example')),
            body: Center(
                child: Column(mainAxisSize: MainAxisSize.min, children: [
              ElevatedButton(
                  child: Text("Init"),
                  onPressed: () =>
                      Apptutti.init(listener: _tuttiInitListerner)),
              ElevatedButton(child: Text("Adready"), onPressed: _isAdReady),
              ElevatedButton(
                  child: Text("Ad-Banner"),
                  onPressed: () => Apptutti.showAd(Apptutti.ADTYPE_BANNER)),
              ElevatedButton(
                  child: Text("Ad-Interstitial"),
                  onPressed: () =>
                      Apptutti.showAd(Apptutti.ADTYPE_INTERSTITIAL)),
              ElevatedButton(
                  child: Text("Ad-Rewarded"),
                  onPressed: () => Apptutti.showAd(Apptutti.ADTYPE_REWARDED,
                      listener: _tuttiAdsListener)),
            ]))));
  }


  Future<void> _isAdReady() async {
    var result = await Apptutti.isAdReady();
    print("isAdReady $result");
  }

}
