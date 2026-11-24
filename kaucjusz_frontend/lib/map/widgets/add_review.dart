import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';

class AddReview extends StatelessWidget {
  const AddReview({super.key});

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Scaffold(
      appBar: AppBar(title: Text("Dodaj opinie")),
      body: Padding(
        padding: const EdgeInsets.only(left: 20, bottom: 20, right: 20),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          spacing: 20,
          children: [
            Row(
              spacing: 10,
              children: [
                CircleAvatar(
                  backgroundColor: t.colorScheme.primaryContainer,
                  foregroundColor: t.colorScheme.onPrimaryContainer,
                  child: Icon(Icons.person_outline),
                ),
                Text("Username", style: t.textTheme.titleMedium),
              ],
            ),
            SelfRating(),
            SizedBox(
              height: 120,
              child: TextField(
                maxLines: null,
                expands: true,
                textAlignVertical: TextAlignVertical.top,
                decoration: InputDecoration(
                  hintText: "Opinia (opcjonalne)",
                  alignLabelWithHint: true,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(4),
                  ),
                ),
              ),
            ),
            Spacer(),
            ElevatedButton(onPressed: () {}, child: Text("Siema")),
          ],
        ),
      ),
    );
  }
}

class SelfRating extends StatefulWidget {
  const SelfRating({super.key});

  @override
  State<SelfRating> createState() => _SelfRatingState();
}

class _SelfRatingState extends State<SelfRating> {
  int rating = 0;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        ...List<int>.generate(5, (int index) => index, growable: true).map(
          (number) => IconButton(
            icon: SvgPicture.asset("assets/bottle_alt.svg"),
            selectedIcon: SvgPicture.asset("assets/bottle_alt_filled.svg"),
            isSelected: number < rating,
            onPressed: () {
              setState(() {
                rating = number + 1;
              });
              print(rating);
            },
            style: IconButton.styleFrom(padding: EdgeInsets.zero),
          ),
        ),
      ],
    );
  }
}
