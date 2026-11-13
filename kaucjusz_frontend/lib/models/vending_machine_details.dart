import 'package:json_annotation/json_annotation.dart';

part 'vending_machine_details.g.dart';

@JsonEnum(fieldRename: FieldRename.screamingSnake)
enum MachineStatus { active, outOfService }

@JsonSerializable(explicitToJson: true)
class VendingMachineDetails {
  final String id;
  final OpeningHours? openingHours;
  final int reviewsCount;
  final MachineStatus status;
  final Reviews reviews;

  VendingMachineDetails({
    required this.id,
    required this.openingHours,
    required this.reviewsCount,
    required this.status,
    required this.reviews,
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

@JsonSerializable()
class Reviews {
  final int totalElements;
  final List<Review> content;
  final int number;

  Reviews({
    required this.totalElements,
    required this.content,
    required this.number,
  });

  factory Reviews.fromJson(Map<String, dynamic> json) =>
      _$ReviewsFromJson(json);

  Map<String, dynamic> toJson() => _$ReviewsToJson(this);
}

@JsonSerializable()
class Review {
  final String id;
  final int rating;
  final String? comment;
  final String user;
  final DateTime createdAt;

  Review({
    required this.id,
    required this.rating,
    this.comment,
    required this.user,
    required this.createdAt,
  });

  factory Review.fromJson(Map<String, dynamic> json) => _$ReviewFromJson(json);

  Map<String, dynamic> toJson() => _$ReviewToJson(this);
}
