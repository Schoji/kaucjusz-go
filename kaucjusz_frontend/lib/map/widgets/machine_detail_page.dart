import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:kaucjusz_go/widgets/theme_extensions.dart';

class MachineDetailPage extends StatelessWidget {
  const MachineDetailPage({super.key});

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        leading: BackButton(
          style: IconButton.styleFrom(
            backgroundColor: t.colorScheme.secondaryContainer,
            foregroundColor: t.colorScheme.onSecondaryContainer,
          ),
        ),
      ),

      extendBodyBehindAppBar: true,
      extendBody: true,
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          ShaderMask(
            blendMode: BlendMode.dstIn,
            shaderCallback: (Rect bounds) {
              return LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
                stops: [0.9, 1.0],
                colors: <Color>[Colors.black, Colors.transparent],
              ).createShader(bounds);
            },
            child: Image.asset(
              "assets/mock_machine.png",
              fit: BoxFit.cover,
              height: 340,
            ),
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
                      Text("6,7", style: t.textTheme.displayMediumEmphasized),
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
                          Text("2137 opinii", style: t.textTheme.labelMedium),
                        ],
                      ),
                    ],
                  ),
                ),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Divider(),
                    Text("Opinie", style: t.textTheme.titleMediumEmphasized),
                  ],
                ),
                Column(children: [
                  Row(children: [],)
                ],)
              ],
            ),
          ),
        ],
      ),
    );
  }
}
