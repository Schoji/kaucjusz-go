import 'package:json_annotation/json_annotation.dart';

part 'vending_machine_details.g.dart';

@JsonSerializable(explicitToJson: true)
class VendingMachineDetails {
  final String id;
  final OpeningHours? openingHours;
  final int reviewsCount;

  VendingMachineDetails({
    required this.id,
    required this.openingHours,
    required this.reviewsCount,
  });

  factory VendingMachineDetails.fromJson(Map<String, dynamic> json) =>
      _$VendingMachineDetailsFromJson(json);

  Map<String, dynamic> toJson() => _$VendingMachineDetailsToJson(this);
}

@JsonSerializable()
class OpeningHours {
  final String? monday;
  final String? tuesday;
  final String? wednesday;
  final String? thursday;
  final String? friday;
  final String? saturday;
  final String? sunday;

  OpeningHours({
    this.monday,
    this.tuesday,
    this.wednesday,
    this.thursday,
    this.friday,
    this.saturday,
    this.sunday,
  });

  factory OpeningHours.fromJson(Map<String, dynamic> json) =>
      _$OpeningHoursFromJson(json);

  Map<String, dynamic> toJson() => _$OpeningHoursToJson(this);
}
