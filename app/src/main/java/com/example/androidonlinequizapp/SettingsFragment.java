package com.example.androidonlinequizapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidonlinequizapp.Common.Common;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;


public class SettingsFragment extends Fragment {

    View myFragment;

    Button btnChangePwd,btnSignOut, btnSignIn, mBtnDelete;
    TextView usernameLabel, userScoreLabel;

    FirebaseDatabase database;
    DatabaseReference ranking, users;

    final static String TAG = "SettingsFragment";

    public static SettingsFragment newInstance()
    {
        SettingsFragment settingsFragment = new SettingsFragment();

        return settingsFragment;

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_settings, container, false);


        btnSignOut = myFragment.findViewById(R.id.signoutButton);
        usernameLabel = myFragment.findViewById(R.id.usernameLabel);
        userScoreLabel = myFragment.findViewById(R.id.userScoreLabel);
        mBtnDelete = myFragment.findViewById(R.id.deleteAccountBtn);
        String welcomeText;

        btnSignIn = myFragment.findViewById(R.id.signInIfNotAlreadySignedInButton);

        database = FirebaseDatabase.getInstance();
        ranking = database.getReference("Ranking");
        users = database.getReference("Users");


        if(Common.isFirebaseUser){
            welcomeText = getString(R.string.welcome_text) + " " + Common.currentFirebaseUser.getDisplayName() + "!";
            usernameLabel.setText(welcomeText);
            btnSignIn.setVisibility(View.GONE);

            ranking.child(Common.currentFirebaseUser.getDisplayName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           long scoreForCurrentlyLoggedInUser = (long) dataSnapshot.child("score").getValue();
                          final String text = getString(R.string.score_text) + scoreForCurrentlyLoggedInUser;
                          userScoreLabel.setText(text);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }else if(Common.isAnonUser){
           String pleaseLogIn = getString(R.string.please_log_in);
            usernameLabel.setText(pleaseLogIn);
            btnSignOut.setVisibility(View.GONE);
            userScoreLabel.setVisibility(View.GONE);
            mBtnDelete.setVisibility(View.GONE);
        } else{
            welcomeText = getString(R.string.welcome_text) + " " + Common.currentUser.getUserName() + "!";
            usernameLabel.setText(welcomeText);
            btnSignIn.setVisibility(View.GONE);

            ranking.child(Common.currentUser.getUserName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long scoreForCurrentlyLoggedInUser = (long) dataSnapshot.child("score").getValue();
                            final String text = getString(R.string.score_text) + scoreForCurrentlyLoggedInUser;
                            userScoreLabel.setText(text);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Signout button clicked");

                if(Common.isFirebaseUser){
                    Log.d(TAG, "isFirebaseUser == true");

                    AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Signout succeded");
                                Common.isFirebaseUser = false;
                            } else {
                                Log.d(TAG, "Signout failed");
                            }
                        }
                    });


                }
                else{
                    Log.d(TAG, "isFirebaseUser == false");
                    AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "delete succeded");
                                Common.isFirebaseUser = false;
                            } else {
                                Log.d(TAG, "delete failed");
                            }
                        }
                    });

                }






                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Common.isAnonUser = false;
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isFirebaseUser){
                    AuthUI.getInstance().delete(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){

                                ranking.child(Common.currentFirebaseUser.getDisplayName()).removeValue();
                                users.child(Common.currentFirebaseUser.getDisplayName()).removeValue();

                                Common.isFirebaseUser = false;

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);

                            }
                        }
                    });
                }else{
                    ranking.child(Common.currentUser.getUserName()).removeValue();
                    users.child(Common.currentUser.getUserName()).removeValue();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        return myFragment;
    }
}
