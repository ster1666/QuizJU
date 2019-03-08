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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SettingsFragment extends Fragment {

    View myFragment;

    Button btnChangePwd,btnSignOut;

    RecyclerView settingsView;
    LinearLayoutManager layoutManager;

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

        settingsView = myFragment.findViewById(R.id.settingsView);
        layoutManager = new LinearLayoutManager(getActivity());
        settingsView.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        settingsView.setLayoutManager(layoutManager);

        btnSignOut = myFragment.findViewById(R.id.logoutButton);
        btnChangePwd = myFragment.findViewById(R.id.changePasswordButton);

        return myFragment;
    }
}
