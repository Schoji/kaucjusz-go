import 'package:flutter/material.dart';
import 'package:kaucjusz_go/service/database_service.dart';
import 'package:kaucjusz_go/service/kaucjusz_service.dart';

class PetPage extends StatelessWidget {
  const PetPage({super.key});

  @override
  Widget build(BuildContext context) {
    final DatabaseService db = DatabaseService();
    return Column(
      children: [
        ListenableBuilder(
          listenable: KaucjuszService(),
          builder: (context, child) {
            switch (KaucjuszService().mood) {
              case Mood.happy:
                {}
                return Image.asset(
                  "assets/kaucjusz_adult/kaucjusz_adult_happy.png",
                );
              case Mood.neutral:
                return Image.asset(
                  "assets/kaucjusz_adult/kaucjusz_adult_neutral.png",
                );
              case Mood.hungry:
                return Image.asset(
                  "assets/kaucjusz_adult/kaucjusz_adult_hungry_sad.png",
                );
              case Mood.sleeping:
                return Image.asset(
                  "assets/kaucjusz_adult/kaucjusz_adult_sleeping.png",
                );
            }
          },
        ),

        Container(
          child: FilledButton(
            onPressed: () async {
              final xd = await db.syncUser("lolencjusz", "hahaxd");
              print(xd.toJson());
            },
            child: Text("ale jaja"),
          ),
        ),
      ],
    );
  }
}
