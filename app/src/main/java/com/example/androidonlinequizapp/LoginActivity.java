package com.example.androidonlinequizapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidonlinequizapp.Common.Common;
import com.example.androidonlinequizapp.Model.Ranking;
import com.example.androidonlinequizapp.Model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

import static com.example.androidonlinequizapp.R.layout.activity_login;
import static com.google.firebase.inappmessaging.internal.Logging.TAG;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";

    EditText mUsername, mPassword;
    Button mSignInButton, mSignUpButton, mPlayWithoutAccBtn;

    SignInButton mGoogleSignInButton;

    FirebaseDatabase database;
    DatabaseReference users, rankingTbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mSignInButton = findViewById(R.id.username_sign_in_button);
        mGoogleSignInButton = findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(this);
        mGoogleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignUpButton = findViewById(R.id.signup_button);
        mPlayWithoutAccBtn = findViewById(R.id.playWithoutAccount);


        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        rankingTbl = database.getReference("Ranking");

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpActivity();
            }
        });

        mPlayWithoutAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.isAnonUser = true;

                Intent intent = new Intent(LoginActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void goToSignUpActivity() {
        Intent target = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(target);
        finish();
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
                            Toast.makeText(LoginActivity.this, R.string.wrong_password_login, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.enter_username_login, Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(LoginActivity.this, R.string.user_not_exists_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }


    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                initializeScoreForGoogleUser();
                Common.currentFirebaseUser = user;
                Common.isFirebaseUser = true;
                Intent homeActivity = new Intent(LoginActivity.this,Home.class);
                startActivity(homeActivity);
                finish();
            } else {
                Log.d(TAG, "SIGN IN FAILED");

                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        createSignInIntent();
    }
    // [END auth_fui_result]

    private void initializeScoreForGoogleUser(){
        rankingTbl.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.hasChild(Common.currentFirebaseUser.getDisplayName())){

                        FirebaseUser newUser = Common.currentFirebaseUser;
                        rankingTbl.child(Common.currentFirebaseUser.getDisplayName())
                                .setValue(new Ranking(newUser.getDisplayName(), 0));

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}