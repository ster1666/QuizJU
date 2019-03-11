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

import com.google.firebase.auth.FirebaseAuth;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;


public class SettingsFragment extends Fragment {

    View myFragment;

    Button btnChangePwd,btnSignOut;

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

        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Signout button clicked");

                FirebaseAuth.getInstance().signOut();



                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return myFragment;
    }
}
