package com.example.androidonlinequizapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidonlinequizapp.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import dagger.multibindings.ElementsIntoSet;

public class Playing extends AppCompatActivity implements View.OnClickListener {


    final static long INTERVAL = 1000;
    final static long TIMEOUT = 11000;

    int progressValue = 0;

    CountDownTimer mCountDown;

    int index=0,score=0,thisQuestion=0,totalQuestion=0,correctAnswer;


    //FIREBASE
/*
    FirebaseDatabase database;
    DatabaseReference questions;
*/
    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD;
    TextView txtScore,txtQuestionNum,question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        //firebase

        /*database = FirebaseDatabase.getInstance();
        questions = database.getReference();
*/
        //views

        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestion);
        question_text = (TextView)findViewById(R.id.question_text);

        question_image = (ImageView)findViewById(R.id.question_image);

        progressBar =(ProgressBar)findViewById(R.id.progressBar);





        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);



        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

        //fadeIn();

        fadeIn(2000, btnA, btnB, btnC,btnD);


    }




    private static void fadeIn(long duration, final View... views) {
        if (views == null) return;
        final ValueAnimator va = ValueAnimator.ofFloat(0, 1);
        va.setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                final float alpha = (float) animator.getAnimatedValue();
                for (View view : views) view.setAlpha(alpha);
            }
        });
        va.start();
    }



    @Override
    public void onClick(View view) {

        mCountDown.cancel();
        if (index < totalQuestion)
        {
            Button clickedButton = (Button)view;
            fadeIn(2000, btnA, btnB, btnC,btnD);

            //  fadeIn();

            if (clickedButton.getText().equals(Common.questionsList.get(index).getCorrectAnswer()))
            {


                //correct Answer
                score+=10;
                correctAnswer++;
                showQuestion(++index); //next question

               // fadeIn();

            }
            else
            {
                //Wrong Answer



                showQuestion(++index); //next question

                //fadeIn();
                /* IF WE WANT TO END GAME ON WRONG ANSWER
                Intent intent = new Intent(this,Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE",score);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT",correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
                */
            }

            txtScore.setText(String.format("%d",score));

        }

    }

    private void showQuestion(int index) {

        if (index < totalQuestion)
        {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;
            fadeIn(2000, btnA, btnB, btnC,btnD);

            // fadeIn();



            if (Common.questionsList.get(index).getIsImageQuestion().equals("true"))
            {
                Picasso.with(getBaseContext())
                        .load(Common.questionsList.get(index).getQuestion())
                        .into(question_image);

                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else
            {
                question_text.setText(Common.questionsList.get(index).getQuestion());

                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionsList.get(index).getAnswerA());
            btnB.setText(Common.questionsList.get(index).getAnswerB());
            btnC.setText(Common.questionsList.get(index).getAnswerC());
            btnD.setText(Common.questionsList.get(index).getAnswerD());

            mCountDown.start();

        }
        else
        {
            //IF its final question
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            //finish();

        }

    }




    @Override
    protected void onResume() {
        super.onResume();
        totalQuestion = Common.questionsList.size();
        mCountDown = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;

            }

            @Override
            public void onFinish() {

                mCountDown.cancel();;
                showQuestion(++index);

            }

        };


        showQuestion(index);
    }
}
