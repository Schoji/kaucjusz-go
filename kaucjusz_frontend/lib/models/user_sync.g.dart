// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'user_sync.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserSync _$UserSyncFromJson(Map<String, dynamic> json) => UserSync(
  id: json['id'] as String,
  firebaseUid: json['firebaseUid'] as String,
  email: json['email'] as String,
  nickname: json['nickname'] as String,
  region: json['region'] as String,
  currentPoints: (json['currentPoints'] as num).toInt(),
  lifetimePoints: (json['lifetimePoints'] as num).toInt(),
  currentStreak: (json['currentStreak'] as num).toInt(),
  isNewUser: json['isNewUser'] as bool,
);

Map<String, dynamic> _$UserSyncToJson(UserSync instance) => <String, dynamic>{
  'id': instance.id,
  'firebaseUid': instance.firebaseUid,
  'email': instance.email,
  'nickname': instance.nickname,
  'region': instance.region,
  'currentPoints': instance.currentPoints,
  'lifetimePoints': instance.lifetimePoints,
  'currentStreak': instance.currentStreak,
  'isNewUser': instance.isNewUser,
};
