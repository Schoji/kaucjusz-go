import 'package:json_annotation/json_annotation.dart';

part 'user_sync.g.dart';

@JsonSerializable()
class UserSync {
  final String id;
  final String firebaseUid;
  final String email;
  final String nickname;
  final String region;
  final int currentPoints;
  final int lifetimePoints;
  final int currentStreak;
  final bool isNewUser;

  UserSync({
    required this.id,
    required this.firebaseUid,
    required this.email,
    required this.nickname,
    required this.region,
    required this.currentPoints,
    required this.lifetimePoints,
    required this.currentStreak,
    required this.isNewUser,
  });

  factory UserSync.fromJson(Map<String, dynamic> json) =>
      _$UserSyncFromJson(json);

  Map<String, dynamic> toJson() => _$UserSyncToJson(this);
}
