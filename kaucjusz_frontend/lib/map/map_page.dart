import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_svg/svg.dart';
import 'package:kaucjusz_go/login/login_page.dart';
import 'package:kaucjusz_go/map/widgets/draggable_sheet.dart';
import 'package:kaucjusz_go/service/database_service.dart';

import 'package:latlong2/latlong.dart';
import 'package:url_launcher/url_launcher.dart';

List<LatLng> mockMarkers = [
  LatLng(53.4337, 14.5494),
  LatLng(53.4268, 14.5531),
  LatLng(53.4198, 14.5345),
];

const peekSize = 0.35;
const maxSize = 0.92;

class MapPage extends StatefulWidget {
  const MapPage({super.key, required this.onSheetVisibilityChanged});

  final ValueChanged<bool> onSheetVisibilityChanged;

  @override
  State<MapPage> createState() => _MapPageState();
}

class _MapPageState extends State<MapPage> {
  final db = DatabaseService();
  bool isOpen = false;
  bool wasOpen = false;
  DraggableScrollableController sheetController =
      DraggableScrollableController();
  ScrollController scrollController = ScrollController();

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

  @override
  void initState() {
    super.initState();
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
    return Stack(
      children: [
        FlutterMap(
          options: MapOptions(
            initialCenter: LatLng(53.4285, 14.5528),
            initialZoom: 14,
          ),
          children: [
            TileLayer(
              // Bring your own tiles
              urlTemplate:
                  'https://tile.openstreetmap.org/{z}/{x}/{y}.png', // For demonstration only
              userAgentPackageName: "com.kaucjuszgo.siema",
            ),
            FutureBuilder(
              future: db.getVendingMachines(53.4285, 14.5528, 100.0),
              builder: (context, asyncSnapshot) {
                print(asyncSnapshot.data);
                return MarkerLayer(
                  markers: mockMarkers
                      .map(
                        (marker) => Marker(
                          height: 64,
                          width: 64,
                          alignment: Alignment.topCenter,
                          point: marker,
                          child: GestureDetector(
                            onTap: () {
                              sheetController.animateTo(
                                peekSize,
                                duration: Duration(milliseconds: 100),
                                curve: Curves.easeInOut,
                              );
                            },
                            child: SvgPicture.asset(
                              "assets/pin_bottle_light.svg",
                            ),
                          ),
                        ),
                      )
                      .toList(),
                );
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
            child: SearchBar(
              elevation: WidgetStatePropertyAll(1),
              trailing: [
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 16),
                  child: Icon(Icons.search),
                ),
              ],
              hintText: "Szukaj kaucjomatu",
            ),
          ),
        ),
        Align(
          alignment: AlignmentGeometry.bottomCenter,
          child: FloatingActionButton(
            onPressed: () => Navigator.push(
              context,
              MaterialPageRoute<void>(builder: (context) => const LoginPage()),
            ),
            child: Icon(Icons.login),
          ),
        ),
        DraggableSheet(sheetController: sheetController),
      ],
    );
  }
}
