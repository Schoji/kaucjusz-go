import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_svg/svg.dart';
import 'package:kaucjusz_go/login/login_page.dart';
import 'package:kaucjusz_go/map/widgets/drag_handle.dart';
import 'package:latlong2/latlong.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:kaucjusz_go/widgets/theme_extensions.dart';

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
                          sheetController.animateTo(
                            peekSize,
                            duration: Duration(milliseconds: 100),
                            curve: Curves.easeInOut,
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
        DraggableSheet(sheetController: sheetController),
      ],
    );
  }
}

class DraggableSheet extends StatelessWidget {
  const DraggableSheet({super.key, required this.sheetController});

  final DraggableScrollableController sheetController;

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return DraggableScrollableSheet(
      initialChildSize: 0,
      minChildSize: 0.0,
      maxChildSize: maxSize,
      snap: true,
      controller: sheetController,
      snapAnimationDuration: Duration(milliseconds: 150),

      snapSizes: const [peekSize],
      builder: (context, scrollController) {
        return Material(
          elevation: 1,
          clipBehavior: Clip.antiAlias,
          color: t.colorScheme.surfaceContainerLow,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(28),
              topRight: Radius.circular(28),
            ),
          ),
          child: CustomScrollView(
            controller: scrollController,
            slivers: [
              PinnedHeaderSliver(
                child: ColoredBox(
                  color: t.colorScheme.surfaceContainerLow,
                  child: DragHandle(),
                ),
              ),
              SliverToBoxAdapter(
                child: Column(
                  children: [
                    ListenableBuilder(
                      listenable: sheetController,
                      builder: (context, child) {
                        if (sheetController.isAttached) {
                          final progress =
                              ((sheetController.size - peekSize) /
                                      (maxSize - peekSize))
                                  .clamp(0.0, 1.0);
                          return ShaderMask(
                            blendMode: BlendMode.dstIn,
                            shaderCallback: (Rect bounds) {
                              return LinearGradient(
                                begin: Alignment.topCenter,
                                end: Alignment.bottomCenter,
                                stops: [0.9, 1.0],
                                colors: <Color>[
                                  Colors.black,
                                  Colors.transparent,
                                ],
                              ).createShader(bounds);
                            },
                            child: Opacity(
                              opacity: progress,
                              child: ClipRect(
                                child: Align(
                                  alignment: Alignment.bottomCenter,
                                  heightFactor: progress,
                                  child: Image.asset(
                                    "assets/mock_machine.png",
                                    fit: BoxFit.cover,
                                  ),
                                ),
                              ),
                            ),
                          );
                        }
                        return Container();
                      },
                    ),

                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        spacing: 16,
                        children: [
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            spacing: 8,
                            children: [
                              Text(
                                "Kaucjomat Lidl",
                                style: t.textTheme.headlineMediumEmphasized,
                              ),
                              Text(
                                "ul. Bez Znaczenia 2 • 123m • Otwarte całą dobę",
                                style: t.textTheme.labelLarge,
                              ),
                            ],
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Row(
                              spacing: 8,
                              children: [
                                Text(
                                  "6,7",
                                  style: t.textTheme.displayMediumEmphasized,
                                ),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Row(
                                      spacing: 12,
                                      children: [
                                        SvgPicture.asset(
                                          "assets/rating_bottle_full.svg",
                                          width: 36,
                                          height: 36,
                                        ),
                                        SvgPicture.asset(
                                          "assets/rating_bottle_full.svg",
                                          width: 36,
                                          height: 36,
                                        ),
                                        SvgPicture.asset(
                                          "assets/rating_bottle_full.svg",
                                          width: 36,
                                          height: 36,
                                        ),
                                        SvgPicture.asset(
                                          "assets/rating_bottle_full.svg",
                                          width: 36,
                                          height: 36,
                                        ),
                                        SvgPicture.asset(
                                          "assets/rating_bottle_empty.svg",
                                          width: 36,
                                          height: 36,
                                        ),
                                      ],
                                    ),
                                    Text(
                                      "2137 opinii",
                                      style: t.textTheme.labelMedium,
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),

                          Row(
                            spacing: 10,
                            children: [
                              FilledButton.icon(
                                onPressed: () {},
                                icon: Icon(Icons.navigation),
                                label: Text("Nawiguj"),
                                style: FilledButton.styleFrom(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadiusGeometry.circular(
                                      12,
                                    ),
                                  ),
                                ),
                              ),
                              FilledButton.icon(
                                onPressed: () {},
                                icon: Icon(Icons.edit),
                                label: Text("Dodaj opinię"),
                                style: FilledButton.styleFrom(
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadiusGeometry.circular(
                                      12,
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}
