import 'package:flutter/material.dart';
import 'package:kaucjusz_go/pet/pet_page.dart';
import 'package:kaucjusz_go/map/map_page.dart';
import 'package:kaucjusz_go/receipt/receipt_page.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
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
  final List<Widget> _screens = [MapPage(), PetPage(), ReceiptPage()];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(),
        body: _screens[_selectedIndex],
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: _selectedIndex, // Highlight the selected tab
          onTap: (index) {
            setState(() {
              _selectedIndex = index; // Update selected index
            });
          },
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.map), label: 'Map'),
            BottomNavigationBarItem(icon: Icon(Icons.pets), label: 'Pet'),
            BottomNavigationBarItem(icon: Icon(Icons.scanner), label: 'Scan'),
          ],
        ),
      ),
    );
  }
}
