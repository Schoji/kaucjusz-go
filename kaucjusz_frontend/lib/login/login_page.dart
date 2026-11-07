import 'package:flutter/material.dart';
import 'package:kaucjusz_go/service/auth_service.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final AuthService auth = AuthService();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Center(
            child: Column(
              children: [
                Text("Logowanie hehe"),
                FilledButton(
                  onPressed: () async => await auth.signInWithGoogle(),
                  child: Text("gugiel"),
                ),
                FilledButton(
                  onPressed: () async => await auth.signInWithFacebook(),
                  child: Text("fb"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
