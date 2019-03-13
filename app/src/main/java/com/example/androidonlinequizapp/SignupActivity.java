package com.example.androidonlinequizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidonlinequizapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import androidx.annotation.NonNull;

import static com.example.androidonlinequizapp.R.drawable.ic_account_circle_black_24dp;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    ImageView mDefaultProfileImage;
    EditText mNewUsername, mNewEmail, mNewPassword;
    Button mCreateAccountButton;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDefaultProfileImage = findViewById(R.id.default_profile_image);
        mDefaultProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);

        mNewUsername = findViewById(R.id.newusername);
        mNewEmail = findViewById(R.id.newemail);
        mNewPassword = findViewById(R.id.newpassword);

        mCreateAccountButton = findViewById(R.id.create_new_account_button);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                
                createAccount();
            }

        });

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

    }

    private void createAccount() {
        Log.d(TAG, "createAccount()");
        final User user = new User (mNewUsername.getText().toString(),
                mNewPassword.getText().toString(),
                mNewEmail.getText().toString());


        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUserName()).exists())
                    Toast.makeText(SignupActivity.this, "User Already Exists!", Toast.LENGTH_SHORT).show();


                else
                {
                    users.child(user.getUserName())
                            .setValue(user);
                    Toast.makeText(SignupActivity.this, "User registration success!", Toast.LENGTH_SHORT).show();

                    Intent target = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(target);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
