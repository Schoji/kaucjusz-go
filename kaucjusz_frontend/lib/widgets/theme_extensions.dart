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

// na wypadek gdyby feliks zmienił zdanie z kolorami w ciemnym motywie
extension PodiumColors on ColorScheme {
  Color get podiumGold =>
      brightness == .light ? Color(0xFFEED382) : Color(0xFFEED382);

  Color get podiumSilver =>
      brightness == .light ? Color(0xFFC8CFC6) : Color(0xFFC8CFC6);

  Color get podiumBronze =>
      brightness == .light ? Color(0xFFD8B194) : Color(0xFFD8B194);
}
