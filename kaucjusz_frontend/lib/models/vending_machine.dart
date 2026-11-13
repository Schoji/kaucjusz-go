import 'package:json_annotation/json_annotation.dart';

part 'vending_machine.g.dart';

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

  VendingMachine({
    required this.id,
    required this.name,
    required this.address,
    required this.latitude,
    required this.longitude,
    required this.distance,
    required this.averageRating,
    required this.photoUrl,
  });

  factory VendingMachine.fromJson(Map<String, dynamic> json) =>
      _$VendingMachineFromJson(json);

  Map<String, dynamic> toJson() => _$VendingMachineToJson(this);
}
