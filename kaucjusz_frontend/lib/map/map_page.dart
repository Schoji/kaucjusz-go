import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_svg/svg.dart';
import 'package:kaucjusz_go/login/login_page.dart';
import 'package:kaucjusz_go/map/widgets/draggable_sheet.dart';
import 'package:kaucjusz_go/models/reviews.dart';
import 'package:kaucjusz_go/models/vending_machine.dart';
import 'package:kaucjusz_go/models/vending_machine_details.dart';
import 'package:kaucjusz_go/service/database_service.dart';
import 'package:kaucjusz_go/service/recent_search_service.dart';
import 'package:kaucjusz_go/settings/settings_page.dart';

import 'package:latlong2/latlong.dart';
import 'package:url_launcher/url_launcher.dart';

const maxSize = 0.92;
const initialZoom = 14.0;
const selectZoom = 16.0;

class MapPage extends StatefulWidget {
  const MapPage({super.key, required this.onSheetVisibilityChanged});

  final ValueChanged<bool> onSheetVisibilityChanged;

  @override
  State<MapPage> createState() => _MapPageState();
}

class _MapPageState extends State<MapPage> {
  double peekSize = 0.35;
  final db = DatabaseService();
  bool isOpen = false;
  bool wasOpen = false;
  DraggableScrollableController sheetController =
      DraggableScrollableController();
  ScrollController scrollController = ScrollController();

  VendingMachine? selectedMachine;

  late final Future<List<VendingMachine>> _vendingMachineFuture;
  Future<VendingMachineDetails>? _detailsFuture;
  Future<Reviews>? _reviewsFuture;
  List<VendingMachine> _vendingMachines = [];

  final MapController mapController = MapController();

  void onSheetChanged() {
    if (!sheetController.isAttached) {
      return;
    }

    wasOpen = isOpen;
    isOpen = sheetController.size > 0.01;

    if (wasOpen != isOpen) {
      widget.onSheetVisibilityChanged(isOpen);
    }
  }

  Future<void> selectMachine(VendingMachine machine) async {
    if (selectedMachine != machine) {
      await sheetController.animateTo(
        0.0,
        duration: Duration(milliseconds: 250),
        curve: Curves.easeInOut,
      );
    }
    setState(() {
      selectedMachine = machine;
      _detailsFuture = db.getVendingMachine(machine.id);
      _reviewsFuture = db.getReviews(machine.id);
    });

    mapController.move(LatLng(machine.latitude, machine.longitude), selectZoom);

    sheetController.animateTo(
      peekSize,
      duration: Duration(milliseconds: 100),
      curve: Curves.easeInOut,
    );
  }

  String formatDistance(double distance) {
    if (distance < 1000) return "${distance.toStringAsFixed(0)} m";
    return "${(distance / 1000).toStringAsFixed(1)} km";
  }

  Future<void> onSearchResultTap(
    SearchController controller,
    VendingMachine machine,
  ) async {
    controller.closeView(machine.name);
    await RecentSearchService().add(machine.id);
    await selectMachine(machine);
  }

  @override
  void initState() {
    super.initState();
    _vendingMachineFuture = db
        .getVendingMachines(LatLng(53.4285, 14.5528), 10000)
        .then((value) => _vendingMachines = value);
    sheetController.addListener(onSheetChanged);
  }

  @override
  void dispose() {
    sheetController.removeListener(onSheetChanged);
    sheetController.dispose();
    scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Stack(
      children: [
        FlutterMap(
          mapController: mapController,
          options: MapOptions(
            initialCenter: LatLng(53.4285, 14.5528),
            initialZoom: initialZoom,
          ),
          children: [
            TileLayer(
              // Bring your own tiles
              urlTemplate:
                  'https://tile.openstreetmap.org/{z}/{x}/{y}.png', // For demonstration only
              userAgentPackageName: "com.kaucjuszgo.siema",
            ),
            FutureBuilder(
              future: _vendingMachineFuture,
              builder: (context, asyncSnapshot) {
                if (asyncSnapshot.hasError) {
                  print(asyncSnapshot.error);
                }

                if (asyncSnapshot.hasData) {
                  final vendingMachines = asyncSnapshot.data ?? [];

                  return MarkerLayer(
                    markers: [
                      ...vendingMachines.map(
                        (machine) => Marker(
                          height: 64,
                          width: 64,
                          alignment: Alignment.topCenter,
                          point: LatLng(machine.latitude, machine.longitude),
                          child: GestureDetector(
                            onTap: () async => await selectMachine(machine),
                            child: AnimatedScale(
                              scale: selectedMachine == machine ? 1.3 : 1.0,
                              duration: Duration(milliseconds: 150),
                              curve: Curves.easeOutBack,
                              child: SvgPicture.asset(
                                "assets/pin_bottle_light.svg",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  );
                }
                return Container();
              },
            ),
            RichAttributionWidget(
              // Include a stylish prebuilt attribution widget that meets all requirments
              attributions: [
                TextSourceAttribution(
                  'OpenStreetMap contributors',
                  onTap: () => launchUrl(
                    Uri.parse('https://openstreetmap.org/copyright'),
                  ), // (external)
                ),
                // Also add images...
              ],
            ),
          ],
        ),
        SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: SearchAnchor.bar(
              barElevation: WidgetStatePropertyAll(1),
              barHintText: "Szukaj kaucjomatu",
              barTrailing: [
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 16),
                  child: Icon(Icons.search),
                ),
                GestureDetector(
                  onTap: () => Navigator.push(
                    context,
                    MaterialPageRoute<void>(
                      builder: (context) => const SettingsPage(),
                    ),
                  ),
                  child: CircleAvatar(
                    radius: 16,
                    backgroundColor: t.colorScheme.primary,
                    foregroundColor: t.colorScheme.onPrimary,
                    child: Text("A"),
                  ),
                ),
              ],
              barLeading: SizedBox.shrink(),
              suggestionsBuilder:
                  (BuildContext context, SearchController controller) {
                    final userInput = controller.text.trim().toLowerCase();

                    final recentMachinesId = RecentSearchService().load();
                    final recentMachines = recentMachinesId
                        .map(
                          (id) => _vendingMachines
                              .where((machine) => machine.id == id)
                              .firstOrNull,
                        )
                        .nonNulls;

                    if (userInput.isEmpty) {
                      if (recentMachines.isNotEmpty) {
                        return [
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 24),
                            child: Text(
                              "Ostatnie",
                              style: t.textTheme.titleSmall,
                            ),
                          ),
                          ...recentMachines.map(
                            (machine) => ListTile(
                              leading: Icon(Icons.history),
                              title: Text(machine.name),
                              subtitle: Text(machine.address),
                              trailing: Icon(Icons.arrow_right),
                              onTap: () =>
                                  onSearchResultTap(controller, machine),
                            ),
                          ),
                        ];
                      } else {
                        return [
                          Padding(
                            padding: const EdgeInsets.all(24.0),
                            child: Text(
                              "Brak ostatnio wyszukiwanych kaucjomatów",
                            ),
                          ),
                        ];
                      }
                    } else {
                      final searchedVendingMachines = _vendingMachines.where(
                        (machine) =>
                            machine.name.toLowerCase().contains(userInput) ||
                            machine.address.toLowerCase().contains(userInput),
                      );
                      if (searchedVendingMachines.isNotEmpty) {
                        return searchedVendingMachines.map(
                          (machine) => ListTile(
                            leading: SizedBox(
                              width: 40,
                              child: Column(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Icon(Icons.location_on_outlined),
                                  Text(
                                    formatDistance(machine.distance),
                                    style: t.textTheme.labelSmall,
                                  ),
                                ],
                              ),
                            ),
                            title: Text(machine.name),
                            subtitle: Text(machine.address),
                            onTap: () => onSearchResultTap(controller, machine),
                          ),
                        );
                      } else {
                        return [
                          ListTile(
                            leading: Icon(Icons.search_off),
                            title: Text(
                              "Nie znaleziono kaucjomatu",
                              style: t.textTheme.bodyLarge,
                            ),
                          ),
                        ];
                      }
                    }
                  },
            ),
          ),
        ),
        Align(
          alignment: AlignmentGeometry.centerRight,
          child: FloatingActionButton(
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute<void>(builder: (context) => const LoginPage()),
            ),
            child: Icon(Icons.login),
          ),
        ),
        DraggableSheet(
          vendingMachine: selectedMachine,
          detailsFuture: _detailsFuture,
          sheetController: sheetController,
          reviewsFuture: _reviewsFuture,
          onPeekSizeChanged: (value) async {
            setState(() {
              peekSize = value;
            });
            if (selectedMachine != null) {
              await sheetController.animateTo(
                value,
                duration: Duration(milliseconds: 100),
                curve: Curves.easeInOut,
              );
            }
          },
        ),
      ],
    );
  }
}
