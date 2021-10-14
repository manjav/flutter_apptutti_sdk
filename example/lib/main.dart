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
            ]))));
  }

}
