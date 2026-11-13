// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'vending_machine.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VendingMachine _$VendingMachineFromJson(Map<String, dynamic> json) =>
    VendingMachine(
      id: json['id'] as String,
      name: json['name'] as String,
      address: json['address'] as String,
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      distance: (json['distance'] as num).toDouble(),
      averageRating: (json['averageRating'] as num).toDouble(),
      photoUrl: json['photoUrl'] as String?,
    );

Map<String, dynamic> _$VendingMachineToJson(VendingMachine instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'address': instance.address,
      'latitude': instance.latitude,
      'longitude': instance.longitude,
      'distance': instance.distance,
      'averageRating': instance.averageRating,
      'photoUrl': instance.photoUrl,
    };
