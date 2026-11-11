import 'package:kaucjusz_go/models/vending_machine.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

class DatabaseService {
  final String databaseUrl = "http://100.100.185.12:8085/api";
  final String token = "lol";

  static final _databaseService = DatabaseService._internal();

  factory DatabaseService() {
    return _databaseService;
  }

  DatabaseService._internal();

  Future<List<VendingMachine>> getVendingMachines(
    double longitude,
    double latitude,
    double radius,
  ) async {
    final uri = Uri.parse("$databaseUrl/vending-machines/search").replace(
      queryParameters: {
        "longitude": longitude.toString(),
        "latitude": latitude.toString(),
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
}
