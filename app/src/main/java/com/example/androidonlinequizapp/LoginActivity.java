package com.example.androidonlinequizapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidonlinequizapp.Common.Common;
import com.example.androidonlinequizapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

import static com.example.androidonlinequizapp.R.layout.activity_login;

public class LoginActivity extends AppCompatActivity {

    EditText mUsername, mPassword;
    Button mSignInButton;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mSignInButton = findViewById(R.id.username_sign_in_button);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });
    }

    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {

                    if (!user.isEmpty()) {

                        User login = dataSnapshot.child(user).getValue(User.class);

                        if (login.getPassword().equals(pwd)) {
                            Intent homeActivity = new Intent(LoginActivity.this, Home.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            finish();
                        } else
                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter a Username", Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(LoginActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}