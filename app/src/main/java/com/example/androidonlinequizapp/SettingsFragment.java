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
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;


public class SettingsFragment extends Fragment {

    View myFragment;

    final String TAG = "SettingsFragment";

    Button btnChangePwd,btnSignOut, btnDeleteUser;
    GoogleApiClient mGoogleApiClient;

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
        Log.d(TAG, "isFirebaseUser: ");
        Log.d(TAG, Common.isFirebaseUser.toString());
        myFragment = inflater.inflate(R.layout.fragment_settings, container, false);

        FirebaseUser currentFirebaseUser = Common.currentFirebaseUser;

        if(Common.isFirebaseUser){
            Log.d(TAG, "current firebase user: ");
            Log.d(TAG, currentFirebaseUser.getDisplayName());
        }


        btnSignOut = myFragment.findViewById(R.id.signoutButton);
        btnChangePwd = myFragment.findViewById(R.id.changePasswordButton);
        btnDeleteUser = myFragment.findViewById(R.id.deleteUser);

        //Show change pwd if user is not firebase user
        if(!Common.isFirebaseUser){

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

                if (Common.isFirebaseUser) {
                    signOutGoogleUser();
                    Common.currentFirebaseUser = null;
                }
                else if (!Common.isFirebaseUser){
                    logout();
                }
            }
        });

        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isFirebaseUser) {
                    deleteGoogleUser();
                }
                else if (!Common.isFirebaseUser){

                }
            }
        });

        return myFragment;
    }

    private void deleteGoogleUser(){
        AuthUI.getInstance().delete(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "delete account succeded");
                    Common.isFirebaseUser = false;
                } else {
                    Log.d(TAG, "Signout failed");
                }
            }
        });


        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void signOutGoogleUser(){
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
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void logout(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
