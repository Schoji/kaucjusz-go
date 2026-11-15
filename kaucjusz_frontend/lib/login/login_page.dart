import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:kaucjusz_go/service/auth_service.dart';
import 'package:kaucjusz_go/widgets/theme_extensions.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final AuthService auth = AuthService();
  @override
  Widget build(BuildContext context) {
    final t = Theme.of(context);
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Center(
            child: Column(
              children: [
                Expanded(
                  child: Column(
                    spacing: 10,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      SvgPicture.asset("assets/welcoming.svg", height: 128),
                      Text(
                        "Witaj w Kaucjusz Go!",
                        style: t.textTheme.headlineMediumEmphasized,
                      ),
                      Text(
                        "Zaloguj się, żeby zapisywać swoje postępy.",
                        style: t.textTheme.titleMedium,
                      ),
                    ],
                  ),
                ),
                Column(
                  spacing: 10,
                  children: [
                    SizedBox(
                      width: double.infinity,
                      child: OutlinedButton.icon(
                        onPressed: () async => await auth.signInWithGoogle(),
                        icon: SvgPicture.asset("assets/google.svg", height: 24),
                        label: Text("Kontynuuj z Google"),
                      ),
                    ),
                    SizedBox(
                      width: double.infinity,
                      child: OutlinedButton.icon(
                        onPressed: () async => await auth.signInWithFacebook(),
                        icon: SvgPicture.asset(
                          "assets/facebook.svg",
                          height: 24,
                        ),
                        label: Text("Kontynuuj z Facebook"),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
