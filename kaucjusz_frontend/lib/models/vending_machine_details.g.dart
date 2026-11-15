// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'vending_machine_details.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VendingMachineDetails _$VendingMachineDetailsFromJson(
  Map<String, dynamic> json,
) => VendingMachineDetails(
  id: json['id'] as String,
  openingHours: json['openingHours'] == null
      ? null
      : OpeningHours.fromJson(json['openingHours'] as Map<String, dynamic>),
  reviewsCount: (json['reviewsCount'] as num).toInt(),
);

Map<String, dynamic> _$VendingMachineDetailsToJson(
  VendingMachineDetails instance,
) => <String, dynamic>{
  'id': instance.id,
  'openingHours': instance.openingHours?.toJson(),
  'reviewsCount': instance.reviewsCount,
};

OpeningHours _$OpeningHoursFromJson(Map<String, dynamic> json) => OpeningHours(
  monday: json['monday'] as String?,
  tuesday: json['tuesday'] as String?,
  wednesday: json['wednesday'] as String?,
  thursday: json['thursday'] as String?,
  friday: json['friday'] as String?,
  saturday: json['saturday'] as String?,
  sunday: json['sunday'] as String?,
);

Map<String, dynamic> _$OpeningHoursToJson(OpeningHours instance) =>
    <String, dynamic>{
      'monday': instance.monday,
      'tuesday': instance.tuesday,
      'wednesday': instance.wednesday,
      'thursday': instance.thursday,
      'friday': instance.friday,
      'saturday': instance.saturday,
      'sunday': instance.sunday,
    };
