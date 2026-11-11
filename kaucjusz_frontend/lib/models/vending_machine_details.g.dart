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
  status: $enumDecode(_$MachineStatusEnumMap, json['status']),
  reviews: Reviews.fromJson(json['reviews'] as Map<String, dynamic>),
);

Map<String, dynamic> _$VendingMachineDetailsToJson(
  VendingMachineDetails instance,
) => <String, dynamic>{
  'id': instance.id,
  'openingHours': instance.openingHours?.toJson(),
  'reviewsCount': instance.reviewsCount,
  'status': _$MachineStatusEnumMap[instance.status]!,
  'reviews': instance.reviews.toJson(),
};

const _$MachineStatusEnumMap = {
  MachineStatus.active: 'ACTIVE',
  MachineStatus.outOfService: 'OUT_OF_SERVICE',
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

Reviews _$ReviewsFromJson(Map<String, dynamic> json) => Reviews(
  totalElements: (json['totalElements'] as num).toInt(),
  content: (json['content'] as List<dynamic>)
      .map((e) => Review.fromJson(e as Map<String, dynamic>))
      .toList(),
  number: (json['number'] as num).toInt(),
);

Map<String, dynamic> _$ReviewsToJson(Reviews instance) => <String, dynamic>{
  'totalElements': instance.totalElements,
  'content': instance.content,
  'number': instance.number,
};

Review _$ReviewFromJson(Map<String, dynamic> json) => Review(
  id: json['id'] as String,
  rating: (json['rating'] as num).toInt(),
  comment: json['comment'] as String?,
  user: json['user'] as String,
  createdAt: DateTime.parse(json['createdAt'] as String),
);

Map<String, dynamic> _$ReviewToJson(Review instance) => <String, dynamic>{
  'id': instance.id,
  'rating': instance.rating,
  'comment': instance.comment,
  'user': instance.user,
  'createdAt': instance.createdAt.toIso8601String(),
};
