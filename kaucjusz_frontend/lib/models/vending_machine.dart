import 'package:json_annotation/json_annotation.dart';

part 'vending_machine.g.dart';

@JsonEnum(fieldRename: FieldRename.screamingSnake)
enum MachineStatus { active, outOfService }

@JsonSerializable()
class VendingMachine {
  final String id;
  final String name;
  final String address;
  final double latitude;
  final double longitude;
  final double distance;
  final double averageRating;
  final String? photoUrl;
  final int reviewsCount;
  final MachineStatus status;

  VendingMachine({
    required this.id,
    required this.name,
    required this.address,
    required this.latitude,
    required this.longitude,
    required this.distance,
    required this.averageRating,
    required this.photoUrl,
    required this.reviewsCount,
    required this.status,
  });

  factory VendingMachine.fromJson(Map<String, dynamic> json) =>
      _$VendingMachineFromJson(json);

  Map<String, dynamic> toJson() => _$VendingMachineToJson(this);
}
