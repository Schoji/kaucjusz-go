import 'package:kaucjusz_go/models/reviews.dart';
import 'package:kaucjusz_go/models/user_sync.dart';
import 'package:kaucjusz_go/models/vending_machine.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:kaucjusz_go/models/vending_machine_details.dart';
import 'package:latlong2/latlong.dart';

class DatabaseService {
  final String databaseUrl = "http://100.100.185.12:8085/api";
  final String token = "dev-piotr";

  static final _databaseService = DatabaseService._internal();

  factory DatabaseService() {
    return _databaseService;
  }

  DatabaseService._internal();

  Future<List<VendingMachine>> getVendingMachines(
    LatLng coordinates,
    double radius,
  ) async {
    final uri = Uri.parse("$databaseUrl/vending-machines").replace(
      queryParameters: {
        "longitude": coordinates.longitude.toString(),
        "latitude": coordinates.latitude.toString(),
        "radius": radius.toString(),
      },
    );

    final response = await http.get(
      uri,
      headers: {"Authorization": "Bearer $token", "Accept": "application/json"},
    );

    if (response.statusCode != 200) {
      throw Exception("HTTP ${response.statusCode}: ${response.body}");
    }

    final List<dynamic> data = jsonDecode(response.body);
    return data
        .map((e) => VendingMachine.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<VendingMachineDetails> getVendingMachine(String id) async {
    final uri = Uri.parse("$databaseUrl/vending-machines/$id");

    final response = await http.get(
      uri,
      headers: {"Authorization": "Bearer $token", "Accept": "application/json"},
    );

    if (response.statusCode != 200) {
      throw Exception("HTTP ${response.statusCode}: ${response.body}");
    }

    final data = jsonDecode(response.body);
    return VendingMachineDetails.fromJson(data as Map<String, dynamic>);
  }

  Future<Reviews> getReviews(String id) async {
    final uri = Uri.parse("$databaseUrl/vending-machines/$id/reviews");

    final response = await http.get(
      uri,
      headers: {"Authorization": "Bearer $token", "Accept": "application/json"},
    );

    if (response.statusCode != 200) {
      throw Exception("HTTP ${response.statusCode}: ${response.body}");
    }

    final data = jsonDecode(response.body);
    return Reviews.fromJson(data as Map<String, dynamic>);
  }

  Future<UserSync> syncUser(String nickname, String region) async {
    final uri = Uri.parse("$databaseUrl/users/sync");

    final response = await http.post(
      uri,
      headers: {
        "Authorization": "Bearer $token",
        "Accept": "application/json",
        "Content-Type": "application/json",
      },
      body: jsonEncode({"nickname": nickname, "region": region}),
    );

    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception("HTTP ${response.statusCode}: ${response.body}");
    } // todo: deal with 409

    final data = jsonDecode(response.body);
    return UserSync.fromJson(data as Map<String, dynamic>);
  }
}
