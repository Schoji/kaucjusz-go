import 'package:flutter/material.dart';
import 'package:kaucjusz_go/rank/widgets/rank_type_switch.dart';
import 'package:kaucjusz_go/widgets/theme_extensions.dart';

enum RankType { local, global }

class RankPage extends StatefulWidget {
  const RankPage({super.key});

  @override
  State<RankPage> createState() => _RankPageState();
}

class _RankPageState extends State<RankPage> {
  RankType selectedRank = .local;

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
      child: Column(
        spacing: 8,
        children: [
          RankTypeSwitch(
            onChanged: (value) {
              setState(() {
                selectedRank = value;
              });
            },
          ),
          Container(
            clipBehavior: .antiAlias,
            decoration: BoxDecoration(
              color: t.colorScheme.surfaceContainer,
              borderRadius: BorderRadius.circular(20),
            ),
            height: 214,
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                PodiumPlace(username: "Magaj67", userLevel: 67, place: 2),
                PodiumPlace(username: "Magaj67", userLevel: 67, place: 1),
                PodiumPlace(username: "Magaj67", userLevel: 67, place: 3),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class PodiumPlace extends StatelessWidget {
  const PodiumPlace({
    super.key,
    required this.username,
    required this.userLevel,
    required this.place,
  });

  final String username;
  final int userLevel;
  final int place;

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    final double widgetHeight = switch (place) {
      1 => 100,
      2 => 75,
      _ => 50,
    };
    final color = switch (place) {
      1 => t.colorScheme.podiumGold,
      2 => t.colorScheme.podiumSilver,
      _ => t.colorScheme.podiumBronze,
    };
    return Expanded(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 10),
        child: Column(
          mainAxisAlignment: .end,
          children: [
            CircleAvatar(child: Icon(Icons.person)),
            Text(username, style: t.textTheme.bodyMedium),
            Text(
              "$userLevel LVL",
              style: t.textTheme.bodyMedium?.copyWith(
                color: t.colorScheme.onSurfaceVariant,
              ),
            ),
            Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.vertical(top: .circular(8)),
                color: color,
              ),
              height: widgetHeight,
              width: double.infinity,
              child: Center(
                child: Text(
                  place.toString(),
                  style: t.textTheme.headlineMedium,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
