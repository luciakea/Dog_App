package com.example.logincomponent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button btRegister;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = userEmail.getText().toString().trim();
                String user_password = userPassword.getText().toString().trim();
                if (!user_password.isEmpty() && !user_email.isEmpty()) {

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(Signup.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                firebaseAuth().onAuthStateChanged(function(user) {



                                    if (user) {
                                        firebaseDataBase.ref('users/' + user.uid).set({
                                                email: user.email,
                                                uid : user.uid,
                                                username: username
    });


                                        console.log("User is signed in.");
                                    } else {
                                        console.log("No user is signed in.");

                                    }
                                });
                                startActivity(new Intent(Signup.this, HomePage.class));
                            } else {
                                Toast.makeText(Signup.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                userName.setText("");
                                userPassword.setText("");
                                userEmail.setText("");
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
    }

    private void setupUIViews() {
        userName = findViewById(R.id.etUsername);
        userPassword = findViewById(R.id.etPassword);
        userEmail = findViewById(R.id.etEmail);
        btRegister = findViewById(R.id.bRegister);
        userLogin = findViewById(R.id.tvLogin);

    }

    private Boolean validate() {
        boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }
}