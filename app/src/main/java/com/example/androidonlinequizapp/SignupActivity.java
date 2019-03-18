package com.example.androidonlinequizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidonlinequizapp.Common.Common;
import com.example.androidonlinequizapp.Model.Ranking;
import com.example.androidonlinequizapp.Model.User;
import com.google.firebase.auth.FirebaseUser;
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
    DatabaseReference users, ranking;

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
        ranking = database.getReference("Ranking");

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

                    initializeScoreForUser();

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

    private void initializeScoreForUser(){
        ranking.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(Common.currentUser.getUserName())){

                    User newUser = Common.currentUser;
                    ranking.child(Common.currentUser.getUserName())
                            .setValue(new Ranking(newUser.getUserName(), 0));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
