import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';

enum Mood {
  happy,
  neutral,
  hungry,
  sleeping;

  static Mood random() => values[Random().nextInt(values.length)];
}

enum Age { baby, junior, adult }

class KaucjuszService extends ChangeNotifier {
  static final _kaucjuszService = KaucjuszService._internal();

  factory KaucjuszService() {
    return _kaucjuszService;
  }

  KaucjuszService._internal();

  bool _busy =
      false; // określa czy mozna wykonac jakąś animacje czy jest coś co ją blokuje

  Mood mood = Mood.random();

  void loop() {
    final delay = Duration(seconds: 1);

    Timer(delay, () {
      mood = Mood.random();
      notifyListeners();
      loop();
    });
  }
}
