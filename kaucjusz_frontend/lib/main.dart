import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:kaucjusz_go/pet/pet_page.dart';
import 'package:kaucjusz_go/map/map_page.dart';
import 'package:kaucjusz_go/rank/rank_page.dart';
import 'package:kaucjusz_go/receipt/receipt_page.dart';
import 'package:kaucjusz_go/widgets/navigation_bar.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(const MainApp());
}

class MainApp extends StatefulWidget {
  const MainApp({super.key});

  @override
  State<MainApp> createState() => _MainAppState();
}

class _MainAppState extends State<MainApp> {
  int _selectedIndex = 0;

  // tabiki uwu
  final List<Widget> _screens = [
    MapPage(),
    PetPage(),
    RankPage(),
    ReceiptPage(),
  ];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Color(0xFF376A3E)),
      ),
      home: Scaffold(
        appBar: _selectedIndex != 3 && _selectedIndex != 0 ? AppBar() : null,
        extendBodyBehindAppBar: _selectedIndex == 3 ? true : false,
        body: _screens[_selectedIndex],
        bottomNavigationBar: NavBar(
          selectedIndex: _selectedIndex,
          onTap: (value) => setState(() {
            _selectedIndex = value;
          }),
        ),
      ),
    );
  }
}
