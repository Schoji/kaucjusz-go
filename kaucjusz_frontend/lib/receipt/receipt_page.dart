import 'package:flutter/material.dart';
import 'package:camera/camera.dart';

Future<String> initCamera() async {
  final availableCams = await availableCameras();

  if (availableCams.isEmpty) {
    print("No cameras found");
  }

  final cam = availableCams.where(
    (camera) => camera.lensDirection == CameraLensDirection.back,
  );

  print(cam);

  return "lol";
}

late Future<String> camFuture;

class ReceiptPage extends StatefulWidget {
  const ReceiptPage({super.key});

  @override
  State<ReceiptPage> createState() => _ReceiptPageState();
}

class _ReceiptPageState extends State<ReceiptPage> {
  @override
  void initState() {
    camFuture = initCamera();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: FutureBuilder(
        future: camFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState != ConnectionState.done) {
            return const CircularProgressIndicator(); // jeszcze czekamy
          }
          if (snapshot.hasError) {
            return Text('Błąd: ${snapshot.error}');
          }
          return Text(snapshot.data!); // gotowe
        },
      ),
    );
  }
}
