package com.example.androidonlinequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidonlinequizapp.Common.Common;
import com.example.androidonlinequizapp.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        database = FirebaseDatabase.getInstance();
        question = database.getReference("Questions");

        loadQuestion(Common.categoryId);


        btnPlay = (Button)findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,Playing.class);
                startActivity(intent);
                finish();
            }
        });



    }

    private void loadQuestion(String categoryId) {


        if (Common.questionsList.size() > 0)
            Common.questionsList.clear();

        question.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionsList.add(ques);
                        }

                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {
                    }
                });

        Collections.shuffle(Common.questionsList);

    }
}
