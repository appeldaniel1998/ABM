package com.example.abm.LoginAndRegistration;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginDatabaseUtils {
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void loginUser(String email, String password, LogInActivity logInActivity) {
        //send to firebase auth - upon success logs in automatically
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(logInActivity, "Login successful!", Toast.LENGTH_SHORT).show();

            // transfer to appointments main activity
            logInActivity.userLoggedIn();
        }).addOnFailureListener(e -> Toast.makeText(logInActivity, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
