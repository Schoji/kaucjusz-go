import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_svg/svg.dart';
import 'package:kaucjusz_go/login/login_page.dart';
import 'package:kaucjusz_go/map/widgets/machine_detail_page.dart';
import 'package:latlong2/latlong.dart';
import 'package:url_launcher/url_launcher.dart';

List<LatLng> mockMarkers = [
  LatLng(53.4337, 14.5494),
  LatLng(53.4268, 14.5531),
  LatLng(53.4198, 14.5345),
];

class MapPage extends StatelessWidget {
  const MapPage({super.key});

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
            MarkerLayer(
              markers: mockMarkers
                  .map(
                    (marker) => Marker(
                      height: 64,
                      width: 64,
                      alignment: Alignment.topCenter,
                      point: marker,
                      child: GestureDetector(
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute<void>(
                              builder: (context) => const MachineDetailPage(),
                            ),
                          );
                        },
                        child: SvgPicture.asset("assets/pin_bottle_light.svg"),
                      ),
                    ),
                  )
                  .toList(),
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
      ],
    );
  }
}
