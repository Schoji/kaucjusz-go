import 'package:flutter/material.dart';

class WalletBadge extends StatelessWidget {
  const WalletBadge({super.key});

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Container(
      padding: .symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        borderRadius: .circular(100),
        color: t.colorScheme.secondaryContainer,
      ),
      child: Row(spacing: 4, children: [Icon(Icons.wallet), Text("2000 pkt")]),
    );
  }
}
