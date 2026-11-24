import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:kaucjusz_go/pet/pet_page.dart';
import 'package:kaucjusz_go/map/map_page.dart';
import 'package:kaucjusz_go/rank/rank_page.dart';
import 'package:kaucjusz_go/receipt/receipt_page.dart';
import 'package:kaucjusz_go/service/kaucjusz_service.dart';
import 'package:kaucjusz_go/service/recent_search_service.dart';
import 'package:kaucjusz_go/widgets/navigation_bar.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  await Firebase.initializeApp(options: DefaultFirebaseOptions.currentPlatform);
  await RecentSearchService.init();
  final kaucjusz = KaucjuszService();
  kaucjusz.loop();
  runApp(const MainApp());
}

class MainApp extends StatefulWidget {
  const MainApp({super.key});

  @override
  State<MainApp> createState() => _MainAppState();
}

class _MainAppState extends State<MainApp> {
  int _selectedIndex = 0;
  bool navbarVisible = true;

  // tabiki uwu
  late final List<Widget> _screens = [
    MapPage(
      onSheetVisibilityChanged: (value) => {
        setState(() {
          navbarVisible = !value;
        }),
      },
    ),
    PetPage(),
    RankPage(),
    ReceiptPage(),
  ];

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return MaterialApp(
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Color(0xFF376A3E)),
        outlinedButtonTheme: OutlinedButtonThemeData(
          style: OutlinedButton.styleFrom(
            minimumSize: Size.fromHeight(56),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
              side: BorderSide(color: t.colorScheme.outlineVariant),
            ),
            textStyle: t.textTheme.titleMedium,
            foregroundColor: t.colorScheme.onSurfaceVariant,
            padding: EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          ),
        ),
      ),
      home: Scaffold(
        appBar: _selectedIndex != 3 && _selectedIndex != 0 ? AppBar() : null,
        extendBodyBehindAppBar: _selectedIndex == 3 ? true : false,
        body: _screens[_selectedIndex],
        extendBody: _selectedIndex == 0,
        bottomNavigationBar: AnimatedSlide(
          offset: navbarVisible ? Offset.zero : const Offset(0, 1),
          duration: Duration(milliseconds: 100),
          child: NavBar(
            selectedIndex: _selectedIndex,
            onTap: (value) => setState(() {
              _selectedIndex = value;
            }),
          ),
        ),
      ),
    );
  }
}
