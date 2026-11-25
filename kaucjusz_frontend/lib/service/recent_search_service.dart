import 'package:shared_preferences/shared_preferences.dart';

const String recentSearchKey = "recent_search";
const int maxRecentSearch = 5;

class RecentSearchService {
  static late SharedPreferences _prefs;

  static final _recentSearchService = RecentSearchService._internal();

  factory RecentSearchService() {
    return _recentSearchService;
  }

  RecentSearchService._internal();

  static Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  List<String> load() {
    return _prefs.getStringList(recentSearchKey) ?? [];
  }

  Future<void> add(String id) async {
    List<String> searchHistory = load();

    searchHistory = searchHistory.where((element) => element != id).toList();

    await _prefs.setStringList(
      recentSearchKey,
      [id, ...searchHistory].take(maxRecentSearch).toList(),
    );
  }
}
