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

import static com.google.firebase.inappmessaging.internal.Logging.TAG;


public class SettingsFragment extends Fragment {

    View myFragment;

    Button btnChangePwd,btnSignOut;
    GoogleApiClient mGoogleApiClient;
    TextView usernameLabel;

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
        btnChangePwd = myFragment.findViewById(R.id.changePasswordButton);
        usernameLabel = myFragment.findViewById(R.id.usernameLabel);
        String welcomeText;

        if(Common.isFirebaseUser){
            welcomeText = "Welcome " + Common.currentFirebaseUser.getDisplayName();
            usernameLabel.setText(welcomeText);

        }else{
            welcomeText = "Welcome " + Common.currentUser.getUserName();
            usernameLabel.setText(welcomeText);
        }

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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

        return myFragment;
    }
}
