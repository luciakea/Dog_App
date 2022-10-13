package com.example.logincomponent;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.logincomponent.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.ArrayList;


public class Login extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    public int counter = 5;

    ArrayList<String> Users = new ArrayList<>()

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = findViewById(R.id.ptUsername);
        Password = findViewById(R.id.ptPassword);
        TextView attemptInfo = findViewById(R.id.tvAttemptsInfo);
        Button login = findViewById(R.id.bLogin);
        TextView signUp = findViewById(R.id.tvSignUp);
        TextView forgotPassword = findViewById(R.id.tvForgotPassword);

        attemptInfo.setText("No of attempts remaining: " + counter);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);


        FirebaseUser user = firebaseAuth.getCurrentUser();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            boolean emailVerified = user.isEmailVerified();

            if (emailVerified) {
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.

                startActivity(new Intent(Login.this, HomePage.class));
            }
        }

        signUp.setOnClickListener(view -> startActivity(new Intent(Login.this, Signup.class)));

        forgotPassword.setOnClickListener(view -> startActivity(new Intent(Login.this, PasswordReset.class)));

        login.setOnClickListener(view -> {
            if (counter > 0) {
                String user_name = Name.getText().toString().trim();
                String user_password = Password.getText().toString().trim();
                if (!user_name.isEmpty() && !user_password.isEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(user_name, user_password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                        updateUI(user1);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }

                                private void updateUI(FirebaseUser user1) {
                                    if (user1 != null) {
                                        Toast.makeText(Login.this, "You Signed In successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Login.this, HomePage.class));

                                    } else {
                                        //Toast.makeText(Login.this, "You Didn't signed in", Toast.LENGTH_LONG).show();
                                        --counter;
                                        if (counter < 0){
                                            counter = 0;
                                            attemptInfo.setText("No of attempts remaining: " + counter);
                                        }
                                        else {
                                            attemptInfo.setText("No of attempts remaining: " + counter);
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Login.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                }
            } else {login.setEnabled(false);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2*
                                    60*
                                    1000);//min secs milliseconds
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Login.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                counter = 5;
                                attemptInfo.setText("No of attempts remaining: " + counter);
                                login.setEnabled(true);


                            }
                        });
                    }
                }).start();


            }
        });
    }
}
