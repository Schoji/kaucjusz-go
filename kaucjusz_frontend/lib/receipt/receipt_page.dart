import 'package:flutter/material.dart';
import 'package:camera/camera.dart';
import 'package:flutter/services.dart';

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
        return SizedBox.expand(
          child: FittedBox(
            fit: BoxFit.cover,
            child: SizedBox(
              height: size.width,
              width: size.height,
              child: CameraPreview(controller),
            ),
          ),
        ); // gotowe
      },
    );
  }
}
