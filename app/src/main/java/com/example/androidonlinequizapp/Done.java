package com.example.androidonlinequizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidonlinequizapp.Common.Common;
import com.example.androidonlinequizapp.Model.QuestionScore;
import com.example.androidonlinequizapp.Model.Ranking;
import com.example.androidonlinequizapp.Model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Done extends AppCompatActivity {

    //TEST
    Button btnTryAgain;
    TextView txtResultsScore,getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score, ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);


        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");
        ranking = database.getReference("Ranking");

        txtResultsScore = (TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);


        progressBar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);


        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
            }
        });


        Bundle extra = getIntent().getExtras();
        if (extra != null)
        {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultsScore.setText(String.format("SCORE : %d",score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d",correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //Upload point to Firebase

            if(Common.isFirebaseUser){
                uploadScoreForGoogleUser(Common.currentFirebaseUser, score);
            }else if (!Common.isFirebaseUser){
                uploadScoreForAppUser(Common.currentUser, score);
            }
        }

    }

    private void uploadScoreForGoogleUser(final FirebaseUser user, final int score){

        ranking.child(user.getDisplayName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long currentScore = (long) dataSnapshot.child("score").getValue();
                        long newScore = currentScore + score;

                        ranking.child(Common.currentFirebaseUser.getDisplayName())
                                .setValue(new Ranking(user.getDisplayName(), newScore));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void uploadScoreForAppUser(final User user, final long score){
        ranking.child(user.getUserName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long currentScore = (long) dataSnapshot.child("score").getValue();
                        long newScore = currentScore + score;

                        ranking.child(user.getUserName())
                                .setValue(new Ranking(user.getUserName(), newScore));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
