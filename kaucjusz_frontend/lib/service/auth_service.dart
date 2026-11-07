import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_facebook_auth/flutter_facebook_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';

class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn.instance;

  // Stream to listen to authentication state changes
  Stream<User?> get authStateChanges => _auth.authStateChanges();
  // Get current user
  User? get currentUser => _auth.currentUser;
  // Sign in with Google
  Future<UserCredential?> signInWithGoogle() async {
    try {
      await _googleSignIn.initialize();
      // Trigger the Google authentication flow
      final GoogleSignInAccount googleUser = await _googleSignIn.authenticate();

      // Obtain authentication details
      final GoogleSignInAuthentication googleAuth = googleUser.authentication;
      // Create a new credential
      final credential = GoogleAuthProvider.credential(
        idToken: googleAuth.idToken,
      );
      // Sign in to Firebase with the credential
      return await _auth.signInWithCredential(credential);
    } catch (e) {
      if (e is GoogleSignInException) {
        print("User odmówił logowania");
      }
      print('Error signing in with Google: $e');
      return null;
    }
  }

  Future<UserCredential?> signInWithFacebook() async {
    try {
      // Trigger the Google authentication flow
      final LoginResult loginResult = await FacebookAuth.instance.login();

      if (loginResult.status != LoginStatus.success) {
        print("Facebook login failed ${loginResult.message}");
        return null;
      }
      // Create a credential from the access token
      if (loginResult.accessToken!.type == AccessTokenType.classic) {
        final OAuthCredential facebookAuthCredential =
            FacebookAuthProvider.credential(
              loginResult.accessToken!.tokenString,
            );

        return await FirebaseAuth.instance.signInWithCredential(
          facebookAuthCredential,
        );
      }
    } catch (e) {
      print('Error signing in with Facebook: $e');
      return null;
    }
    return null;
  }

  // Sign out
  Future<void> signOut() async {
    await _googleSignIn.signOut();
    await _auth.signOut();
  }
}
