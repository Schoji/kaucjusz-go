import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:kaucjusz_go/widgets/theme_extensions.dart';

class ReceiptBottomSheet extends StatelessWidget {
  const ReceiptBottomSheet({super.key});

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return SizedBox(
      width: double.infinity,
      child: Padding(
        padding: const EdgeInsets.only(bottom: 32),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          spacing: 4,
          children: [
            Text(
              "Paragon zeskanowany!",
              style: t.textTheme.headlineSmallEmphasized,
            ),
            Text("90 opakowań • 9 zł kaucji", style: t.textTheme.labelLarge),
            SizedBox(
              width: 141,
              height: 141,
              child: Container(
                decoration: ShapeDecoration(
                  color: t.colorScheme.primaryContainer,
                  shape: StarBorder(
                    points: 8,
                    innerRadiusRatio: 0.8,
                    pointRounding: 0.3,
                    valleyRounding: 0.2,
                  ),
                ),
                child: Center(
                  child: FittedBox(
                    fit: BoxFit.scaleDown,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      child: Text(
                        "+67 pkt",
                        style: t.textTheme.headlineLargeEmphasized,
                      ),
                    ),
                  ),
                ),
              ),
            ),
            SvgPicture.asset(
              "assets/happy.svg",
              semanticsLabel: 'My SVG Image',
              height: 100,
              width: 150,
            ),
          ],
        ),
      ),
    );
  }
}
