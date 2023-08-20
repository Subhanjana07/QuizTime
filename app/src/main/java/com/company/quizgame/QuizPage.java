package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlin.jvm.internal.Intrinsics;

public class QuizPage extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Questions");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference databaseReferenceSecond = database.getReference();
    TextView textViewTime,textViewCorrectAns,textViewWrongAns,textViewQuestion,textViewOptionA,textViewOptionB,textViewOptionC,textViewOptionD;
    AppCompatButton buttonFinishGame,buttonNextQuestion;
    String question,optionA,optionB,optionC,optionD,correctAns;
    int questionNumber = 2;

    int userCorrect = 0,userWrong = 0;
    Long questionCount;
    String userAnswer;
    CountDownTimer countDownTimer;
    public static final long TOTAL_TIME = 25000;
    Boolean timerContinue;
    long timeLeft = TOTAL_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        textViewTime = findViewById(R.id.textViewtime);
        textViewCorrectAns = findViewById(R.id.textViewCorrectAns);
        textViewWrongAns = findViewById(R.id.textViewWrongAns);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewOptionA = findViewById(R.id.textViewOptionA);
        textViewOptionB = findViewById(R.id.textViewOptionB);
        textViewOptionC = findViewById(R.id.textViewOptionC);
        textViewOptionD = findViewById(R.id.textViewOptionD);
        buttonFinishGame = findViewById(R.id.buttonFinishGame);
        buttonNextQuestion = findViewById(R.id.buttonNextQues);

        Intent i =getIntent();
        textViewQuestion.setText("1. "+i.getStringExtra("question"));
        textViewOptionA.setText("(a) "+i.getStringExtra("optionA"));
        textViewOptionB.setText("(b) "+i.getStringExtra("optionB"));
        textViewOptionC.setText("(c) "+i.getStringExtra("optionC"));
        textViewOptionD.setText("(d) "+i.getStringExtra("optionD"));
        correctAns = i.getStringExtra("correctAns");
        startTimer();
        buttonNextQuestion.setEnabled(false);

        buttonFinishGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendScore();
                Intent intent = new Intent(QuizPage.this,ScorePage.class);
                startActivity(intent);
                finish();

            }
        });

        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetTimer();
                game();
            }
        });

        textViewOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseTimer();
                buttonNextQuestion.setEnabled(true);
                userAnswer = "a";
                textViewOptionA.setClickable(false);
                textViewOptionB.setClickable(false);
                textViewOptionC.setClickable(false);
                textViewOptionD.setClickable(false);
                if(correctAns.equals(userAnswer))
                {
                    textViewOptionA.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    textViewCorrectAns.setText(""+userCorrect);
                }
                else{

                    textViewOptionA.setBackgroundColor(Color.RED);
                    userWrong++;
                    textViewWrongAns.setText(""+userWrong);
                    if(correctAns.equals("b"))
                    {
                        textViewOptionB.setBackgroundColor(Color.GREEN);
                    }
                    else if (correctAns.equals("c"))
                    {
                        textViewOptionC.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        textViewOptionD.setBackgroundColor(Color.GREEN);
                    }
                }
            }
        });

        textViewOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseTimer();
                buttonNextQuestion.setEnabled(true);
                userAnswer = "b";
                textViewOptionA.setClickable(false);
                textViewOptionB.setClickable(false);
                textViewOptionC.setClickable(false);
                textViewOptionD.setClickable(false);
                if(correctAns.equals(userAnswer))
                {
                    textViewOptionB.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    textViewCorrectAns.setText(""+userCorrect);
                }
                else{

                    textViewOptionB.setBackgroundColor(Color.RED);
                    userWrong++;
                    textViewWrongAns.setText(""+userWrong);
                    if(correctAns.equals("a"))
                    {
                        textViewOptionA.setBackgroundColor(Color.GREEN);
                    }
                    else if (correctAns.equals("c"))
                    {
                        textViewOptionC.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        textViewOptionD.setBackgroundColor(Color.GREEN);
                    }
                }

            }
        });

        textViewOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseTimer();
                buttonNextQuestion.setEnabled(true);
                userAnswer = "c";
                textViewOptionA.setClickable(false);
                textViewOptionB.setClickable(false);
                textViewOptionC.setClickable(false);
                textViewOptionD.setClickable(false);
                if(correctAns.equals(userAnswer))
                {
                    textViewOptionC.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    textViewCorrectAns.setText(""+userCorrect);
                }
                else{

                    textViewOptionC.setBackgroundColor(Color.RED);
                    userWrong++;
                    textViewWrongAns.setText(""+userWrong);
                    if(correctAns.equals("a"))
                    {
                        textViewOptionA.setBackgroundColor(Color.GREEN);
                    }
                    else if (correctAns.equals("b"))
                    {
                        textViewOptionB.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        textViewOptionD.setBackgroundColor(Color.GREEN);
                    }
                }
            }
        });

        textViewOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pauseTimer();
                buttonNextQuestion.setEnabled(true);
                userAnswer = "d";
                textViewOptionA.setClickable(false);
                textViewOptionB.setClickable(false);
                textViewOptionC.setClickable(false);
                textViewOptionD.setClickable(false);
                if(correctAns.equals(userAnswer))
                {
                    textViewOptionD.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    textViewCorrectAns.setText(""+userCorrect);
                }
                else{

                    textViewOptionD.setBackgroundColor(Color.RED);
                    userWrong++;
                    textViewWrongAns.setText(""+userWrong);
                    if(correctAns.equals("a"))
                    {
                        textViewOptionA.setBackgroundColor(Color.GREEN);
                    }
                    else if (correctAns.equals("b"))
                    {
                        textViewOptionB.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        textViewOptionC.setBackgroundColor(Color.GREEN);
                    }
                }
            }
        });
    }

    public void game()
    {
        startTimer();
        textViewOptionA.setClickable(true);
        textViewOptionB.setClickable(true);
        textViewOptionC.setClickable(true);
        textViewOptionD.setClickable(true);

        textViewOptionA.setBackgroundColor(Color.WHITE);
        textViewOptionB.setBackgroundColor(Color.WHITE);
        textViewOptionC.setBackgroundColor(Color.WHITE);
        textViewOptionD.setBackgroundColor(Color.WHITE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                questionCount = dataSnapshot.getChildrenCount();

                question = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                optionA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                optionB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                optionC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                optionD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                correctAns = dataSnapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();
                Log.d("FirebaseData", "question is: " + question);
                Log.d("FirebaseData", "optionA is: " + optionA);
                Log.d("FirebaseData", "optionB is: " + optionB);
                Log.d("FirebaseData", "optionC is: " + optionC);
                Log.d("FirebaseData", "optionD is: " + optionD);
                Log.d("FirebaseData", "correctAns is: " + correctAns);

                textViewQuestion.setText(questionNumber+". "+question);
                textViewOptionA.setText("(a) "+optionA);
                textViewOptionB.setText("(b) "+optionB);
                textViewOptionC.setText("(c) "+optionC);
                textViewOptionD.setText("(d) "+optionD);

                if(questionNumber < questionCount)
                {
                    questionNumber++;
                }
                else{
                    Toast.makeText(QuizPage.this, "You answered all the questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(QuizPage.this, "There is an error", Toast.LENGTH_SHORT).show();
                Log.w("Firebase Data", "Failed to read value.", error.toException());
            }
        });
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateCountDowntext();

            }

            @Override
            public void onFinish() {

                timerContinue = false;
                buttonNextQuestion.setEnabled(true);
                pauseTimer();
                textViewQuestion.setText("Sorry ! Time is Up");
                textViewOptionA.setClickable(false);
                textViewOptionB.setClickable(false);
                textViewOptionC.setClickable(false);
                textViewOptionD.setClickable(false);

            }
        }.start();
        timerContinue = true;
    }
    public void resetTimer(){

        timeLeft = TOTAL_TIME;
        updateCountDowntext();

    }
    public void updateCountDowntext(){
        int second = (int) (timeLeft/1000) % 60;
        textViewTime.setText(""+second);
    }
    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue = false;
        buttonNextQuestion.setEnabled(true);
    }

    public void sendScore(){

        String userUID = user.getUid();
        databaseReferenceSecond.child("Scores").child(userUID).child("Correct").setValue(userCorrect)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(QuizPage.this, "Scores sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
        databaseReferenceSecond.child("Scores").child(userUID).child("Wrong").setValue(userWrong);
    }
}