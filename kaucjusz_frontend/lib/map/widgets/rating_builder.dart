import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';

class RatingBuilder extends StatelessWidget {
  const RatingBuilder({super.key, required this.rating})
    : iconSize = 36,
      iconSpacing = 12;

  const RatingBuilder.small({super.key, required this.rating})
    : iconSize = 20,
      iconSpacing = 4;

  final double rating;

  final double iconSize;
  final double iconSpacing;

  @override
  Widget build(BuildContext context) {
    final roundedRating = rating.floor();
    return Row(
      spacing: iconSpacing,
      children: [
        for (int i = 0; i < roundedRating; i++)
          SvgPicture.asset(
            "assets/rating_bottle_full.svg",
            width: iconSize,
            height: iconSize,
          ),

        for (int i = 0; i < 5 - roundedRating; i++)
          SvgPicture.asset(
            "assets/rating_bottle_empty.svg",
            width: iconSize,
            height: iconSize,
          ),
      ],
    );
  }
}
