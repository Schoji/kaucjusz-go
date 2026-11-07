import 'package:flutter/material.dart';

extension EmphasizedText on TextTheme {
  TextStyle? get headlineLargeEmphasized =>
      headlineLarge?.copyWith(fontWeight: FontWeight.w500);

  TextStyle? get headlineMediumEmphasized =>
      headlineMedium?.copyWith(fontWeight: FontWeight.w500);

  TextStyle? get headlineSmallEmphasized =>
      headlineSmall?.copyWith(fontWeight: FontWeight.w500);

  TextStyle? get displayMediumEmphasized =>
      displayMedium?.copyWith(fontWeight: FontWeight.w500);

  TextStyle? get titleMediumEmphasized =>
      titleMedium?.copyWith(fontWeight: FontWeight.w500);
}
