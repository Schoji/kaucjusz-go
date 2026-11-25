import 'package:flutter/material.dart';
import 'package:kaucjusz_go/rank/rank_page.dart';

class RankTypeSwitch extends StatefulWidget {
  const RankTypeSwitch({super.key, required this.onChanged});

  final ValueChanged<RankType> onChanged;

  @override
  State<RankTypeSwitch> createState() => _RankTypeSwitchState();
}

class _RankTypeSwitchState extends State<RankTypeSwitch> {
  RankType selectedTab = RankType.local;

  @override
  Widget build(BuildContext context) {
    return Row(
      spacing: 2,
      children: [
        ConnectedButtonSegment(
          buttonType: .left,
          label: "Lokalnie",
          onPressed: () => setState(() {
            selectedTab = RankType.local;
            widget.onChanged(RankType.local);
          }),
          isSelected: selectedTab == RankType.local,
        ),
        ConnectedButtonSegment(
          buttonType: .right,
          label: "Globalnie",
          onPressed: () => setState(() {
            selectedTab = RankType.global;
            widget.onChanged(RankType.global);
          }),
          isSelected: selectedTab == RankType.global,
        ),
      ],
    );
  }
}

enum ButtonType { left, right }

class ConnectedButtonSegment extends StatelessWidget {
  const ConnectedButtonSegment({
    super.key,
    required this.buttonType,
    required this.label,
    required this.onPressed,
    required this.isSelected,
  });

  final ButtonType buttonType;
  final String label;
  final VoidCallback onPressed;
  final bool isSelected;

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);

    final BorderRadius radius = isSelected
        ? BorderRadius.circular(20)
        : switch (buttonType) {
            ButtonType.left => BorderRadius.horizontal(
              left: Radius.circular(20),
              right: Radius.circular(8),
            ),
            ButtonType.right => BorderRadius.horizontal(
              left: Radius.circular(8),
              right: Radius.circular(20),
            ),
          };

    return Expanded(
      child: SizedBox(
        height: 40,

        child: FilledButton(
          onPressed: onPressed,
          style: FilledButton.styleFrom(
            backgroundColor: isSelected
                ? t.colorScheme.secondary
                : t.colorScheme.secondaryContainer,
            foregroundColor: isSelected
                ? t.colorScheme.onSecondary
                : t.colorScheme.onSecondaryContainer,
            shape: RoundedRectangleBorder(borderRadius: radius),
          ),
          child: Text(label),
        ),
      ),
    );
  }
}
