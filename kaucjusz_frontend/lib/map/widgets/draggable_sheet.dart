import 'package:flutter/material.dart';
import 'package:kaucjusz_go/map/map_page.dart';
import 'package:kaucjusz_go/map/widgets/rating_builder.dart';
import 'package:kaucjusz_go/models/reviews.dart';
import 'package:kaucjusz_go/models/vending_machine.dart';
import 'package:kaucjusz_go/models/vending_machine_details.dart';
import 'package:kaucjusz_go/widgets/theme_extensions.dart';
import 'package:kaucjusz_go/map/widgets/drag_handle.dart';

const double handleHeight = 48;

class DraggableSheet extends StatefulWidget {
  const DraggableSheet({
    super.key,
    required this.sheetController,
    this.vendingMachine,
    this.detailsFuture,
    this.reviewsFuture,
    this.onPeekSizeChanged,
  });

  final DraggableScrollableController sheetController;
  final VendingMachine? vendingMachine;
  final Future<VendingMachineDetails>? detailsFuture;
  final Future<Reviews>? reviewsFuture;
  final ValueChanged<double>? onPeekSizeChanged;

  @override
  State<DraggableSheet> createState() => _DraggableSheetState();
}

class _DraggableSheetState extends State<DraggableSheet> {
  double maxHeight = 0;
  double newPeekSize = 0.35;
  final headerKey = GlobalKey();
  @override
  Widget build(BuildContext context) {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final box = headerKey.currentContext?.findRenderObject() as RenderBox?;
      final h = box?.size.height;

      if (h == null) return;
      final fraction = (h + handleHeight) / maxHeight;

      if (newPeekSize != fraction) {
        setState(() {
          newPeekSize = fraction;
        });
        widget.onPeekSizeChanged?.call(fraction);
      }
    });
    final t = Theme.of(context);
    final machine = widget.vendingMachine;
    final bottomInset = MediaQuery.viewPaddingOf(context).bottom;
    return LayoutBuilder(
      builder: (context, constraints) {
        maxHeight = constraints.maxHeight;
        final peek = newPeekSize.clamp(0.0, maxSize);
        return DraggableScrollableSheet(
          initialChildSize: 0,
          minChildSize: 0.0,
          maxChildSize: maxSize,
          snap: true,
          controller: widget.sheetController,
          snapAnimationDuration: Duration(milliseconds: 150),

          snapSizes: [peek],
          builder: (context, scrollController) {
            return Material(
              elevation: 1,
              clipBehavior: Clip.antiAlias,
              color: t.colorScheme.surfaceContainerLow,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(28),
                  topRight: Radius.circular(28),
                ),
              ),
              child: CustomScrollView(
                controller: scrollController,
                slivers: [
                  PinnedHeaderSliver(
                    child: ColoredBox(
                      color: t.colorScheme.surfaceContainerLow,
                      child: DragHandle(),
                    ),
                  ),
                  SliverToBoxAdapter(
                    child: Column(
                      children: [
                        ListenableBuilder(
                          listenable: widget.sheetController,
                          builder: (context, child) {
                            if (widget.sheetController.isAttached) {
                              final progress =
                                  ((widget.sheetController.size - newPeekSize) /
                                          (maxSize - newPeekSize))
                                      .clamp(0.0, 1.0);
                              return ShaderMask(
                                blendMode: BlendMode.dstIn,
                                shaderCallback: (Rect bounds) {
                                  return LinearGradient(
                                    begin: Alignment.topCenter,
                                    end: Alignment.bottomCenter,
                                    stops: [0.9, 1.0],
                                    colors: <Color>[
                                      Colors.black,
                                      Colors.transparent,
                                    ],
                                  ).createShader(bounds);
                                },
                                child: Opacity(
                                  opacity: progress,
                                  child: ClipRect(
                                    child: Align(
                                      alignment: Alignment.bottomCenter,
                                      heightFactor: progress,
                                      child: Image.asset(
                                        "assets/mock_machine.png",
                                        fit: BoxFit.cover,
                                      ),
                                    ),
                                  ),
                                ),
                              );
                            }
                            return Container();
                          },
                        ),
                        if (machine != null)
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 16),
                            child: Column(
                              spacing: 16,
                              crossAxisAlignment: CrossAxisAlignment.stretch,
                              children: [
                                Padding(
                                  padding: EdgeInsets.only(
                                    bottom: bottomInset + 16,
                                  ),
                                  key: headerKey,
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.stretch,
                                    spacing: 16,
                                    children: [
                                      Column(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.start,
                                        spacing: 8,
                                        children: [
                                          Text(
                                            "Kaucjomat ${machine.name}",
                                            style: t
                                                .textTheme
                                                .headlineMediumEmphasized,
                                          ),
                                          Text(
                                            "${machine.address} • ${machine.distance.round()}m • Otwarte całą dobę",
                                            style: t.textTheme.labelLarge,
                                          ),
                                        ],
                                      ),
                                      Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Row(
                                          spacing: 8,
                                          children: [
                                            Text(
                                              machine.averageRating.toString(),
                                              style: t
                                                  .textTheme
                                                  .displayMediumEmphasized,
                                            ),
                                            Column(
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.start,
                                              children: [
                                                RatingBuilder(
                                                  rating: machine.averageRating,
                                                ),
                                                Text(
                                                  "${machine.reviewsCount} opinie",
                                                  style:
                                                      t.textTheme.labelMedium,
                                                ),
                                              ],
                                            ),
                                          ],
                                        ),
                                      ),
                                      Row(
                                        spacing: 10,
                                        children: [
                                          FilledButton.icon(
                                            onPressed: () {},
                                            icon: Icon(Icons.navigation),
                                            label: Text("Nawiguj"),
                                            style: FilledButton.styleFrom(
                                              shape: RoundedRectangleBorder(
                                                borderRadius:
                                                    BorderRadiusGeometry.circular(
                                                      12,
                                                    ),
                                              ),
                                            ),
                                          ),
                                          FilledButton.icon(
                                            onPressed: () {},
                                            icon: Icon(Icons.edit),
                                            label: Text("Dodaj opinię"),
                                            style: FilledButton.styleFrom(
                                              shape: RoundedRectangleBorder(
                                                borderRadius:
                                                    BorderRadiusGeometry.circular(
                                                      12,
                                                    ),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ],
                                  ),
                                ),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Divider(),
                                    Text(
                                      "Opinie",
                                      style: t.textTheme.titleMediumEmphasized,
                                    ),
                                  ],
                                ),
                                FutureBuilder(
                                  future: widget.reviewsFuture,
                                  builder: (context, asyncSnapshot) {
                                    if (asyncSnapshot.connectionState ==
                                        ConnectionState.waiting) {
                                      return CircularProgressIndicator();
                                    }

                                    final reviews = asyncSnapshot.data!.items;

                                    return Padding(
                                      padding: const EdgeInsets.only(
                                        bottom: 20,
                                      ),
                                      child: Column(
                                        spacing: 20,
                                        children: [
                                          ...reviews.map(
                                            (review) => UserReview(
                                              review: Review(
                                                id: review.id,
                                                rating: review.rating,
                                                user: review.user,
                                                createdAt: review.createdAt,
                                                comment: review.comment,
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    );
                                  },
                                ),
                              ],
                            ),
                          ),
                      ],
                    ),
                  ),
                ],
              ),
            );
          },
        );
      },
    );
  }
}

class UserReview extends StatelessWidget {
  const UserReview({super.key, required this.review});

  final Review review;

  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      spacing: 8,
      children: [
        CircleAvatar(
          backgroundColor: t.colorScheme.primaryContainer,
          foregroundColor: t.colorScheme.onPrimaryContainer,
          child: Icon(Icons.person_outline),
        ),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            spacing: 8,
            children: [
              Row(
                children: [
                  Text(review.user, style: t.textTheme.labelLarge),
                  Spacer(),
                  Text("2 godz. temu", style: t.textTheme.labelMedium),
                ],
              ),
              RatingBuilder.small(rating: review.rating),
              if (review.comment != null) Text(review.comment!),
            ],
          ),
        ),
      ],
    );
  }
}
