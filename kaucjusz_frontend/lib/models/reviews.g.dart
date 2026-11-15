// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'reviews.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Reviews _$ReviewsFromJson(Map<String, dynamic> json) => Reviews(
  totalElements: (json['totalElements'] as num).toInt(),
  items: (json['items'] as List<dynamic>)
      .map((e) => Review.fromJson(e as Map<String, dynamic>))
      .toList(),
  page: (json['page'] as num).toInt(),
);

Map<String, dynamic> _$ReviewsToJson(Reviews instance) => <String, dynamic>{
  'totalElements': instance.totalElements,
  'items': instance.items,
  'page': instance.page,
};

Review _$ReviewFromJson(Map<String, dynamic> json) => Review(
  id: json['id'] as String,
  rating: (json['rating'] as num).toDouble(),
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
