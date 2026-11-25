import 'package:flutter/material.dart';
import 'package:kaucjusz_go/service/database_service.dart';
import 'package:kaucjusz_go/service/kaucjusz_service.dart';

class PetPage extends StatelessWidget {
  const PetPage({super.key});

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    final DatabaseService db = DatabaseService();
    return Container(
      width: .infinity,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Container(
              width: 372,
              height: 372,
              decoration: BoxDecoration(borderRadius: .circular(16)),
              clipBehavior: .antiAlias,
              child: ListenableBuilder(
                listenable: KaucjuszService(),
                builder: (context, child) {
                  return switch (KaucjuszService().mood) {
                    Mood.happy => Image.asset(
                      "assets/kaucjusz_adult/kaucjusz_adult_happy.png",
                    ),
                    Mood.neutral => Image.asset(
                      "assets/kaucjusz_adult/kaucjusz_adult_neutral.png",
                    ),
                    Mood.hungry => Image.asset(
                      "assets/kaucjusz_adult/kaucjusz_adult_hungry_sad.png",
                    ),
                    Mood.sleeping => Image.asset(
                      "assets/kaucjusz_adult/kaucjusz_adult_sleeping.png",
                    ),
                  };
                },
              ),
            ),
            Row(
              mainAxisAlignment: .spaceBetween,
              children: [
                Row(
                  spacing: 3,
                  crossAxisAlignment: .center,
                  children: [
                    Text("Nazwa", style: t.textTheme.titleLarge),
                    Icon(Icons.edit_outlined, size: 18),
                  ],
                ),
                Badge(label: Text("LVL")),
              ],
            ),

            FilledButton(
              onPressed: () async {
                final xd = await db.syncUser("lolencjusz", "hahaxd");
                print(xd.toJson());
              },
              child: Text("ale jaja"),
            ),
          ],
        ),
      ),
    );
  }
}
