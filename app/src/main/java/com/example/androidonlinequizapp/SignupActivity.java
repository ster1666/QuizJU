package com.example.androidonlinequizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import static com.example.androidonlinequizapp.R.drawable.ic_account_circle_black_24dp;

public class SignupActivity extends AppCompatActivity {

    ImageView mDefaultProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDefaultProfileImage = findViewById(R.id.default_profile_image);
        mDefaultProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
    }
}
