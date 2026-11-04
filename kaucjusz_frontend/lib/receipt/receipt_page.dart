import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:kaucjusz_go/receipt/widgets/receipt_bottom_sheet.dart';

Future<CameraController> initCamera() async {
  final availableCams = await availableCameras();

  if (availableCams.isEmpty) {
    throw Exception("No camera");
  }

  final cam = availableCams
      .where((camera) => camera.lensDirection == CameraLensDirection.back)
      .firstOrNull;

  if (cam == null) throw Exception("No valid back camera found");

  final controller = CameraController(
    cam,
    ResolutionPreset.high,
    enableAudio: false,
  );
  await controller.initialize();
  await controller.lockCaptureOrientation(DeviceOrientation.portraitUp);
  await controller.setFocusMode(FocusMode.auto);
  return controller;
}

class ReceiptPage extends StatefulWidget {
  const ReceiptPage({super.key});

  @override
  State<ReceiptPage> createState() => _ReceiptPageState();
}

class _ReceiptPageState extends State<ReceiptPage> {
  late Future<CameraController> controllerFuture;

  @override
  void initState() {
    controllerFuture = initCamera();
    super.initState();
  }

  @override
  void dispose() {
    controllerFuture.then((c) => c.dispose());
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: controllerFuture,
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) {
          return const Center(
            child: CircularProgressIndicator(),
          ); // jeszcze czekamy
        }
        if (snapshot.hasError) {
          return Center(child: Text('Błąd: ${snapshot.error}'));
        }
        final controller = snapshot.data!;
        final size = controller.value.previewSize!;
        return AnnotatedRegion<SystemUiOverlayStyle>(
          value: SystemUiOverlayStyle.light,
          child: Stack(
            children: [
              SizedBox.expand(
                child: FittedBox(
                  fit: BoxFit.cover,
                  child: SizedBox(
                    height: size.width,
                    width: size.height,
                    child: CameraPreview(controller),
                  ),
                ),
              ),
              Center(
                child: SvgPicture.asset(
                  "assets/scan.svg",
                  semanticsLabel: 'My SVG Image',
                  colorFilter: ColorFilter.mode(Colors.black, BlendMode.srcIn),
                  height: 200,
                  width: 200,
                ),
              ),
              Align(
                alignment: AlignmentGeometry.bottomCenter,
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 24),
                  child: SizedBox(
                    width: 78,
                    height: 78,
                    child: FloatingActionButton.large(
                      elevation: 1,
                      shape: const CircleBorder(),
                      onPressed: () => showModalBottomSheet(
                        showDragHandle: true,
                        context: context,
                        builder: (context) => ReceiptBottomSheet(),
                      ),
                      child: Icon(Icons.photo_camera_outlined, size: 36),
                    ),
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}
