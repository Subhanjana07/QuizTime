package com.company.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScorePage extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Scores");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    TextView textViewCorrect,textViewWrong;
    Button buttonPlayAgain,buttonExit;
    String userCorrect,userWrong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        Toolbar toolbar = findViewById(R.id.toolbarFinish);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Time");

        textViewCorrect = findViewById(R.id.textViewCorrectScore);
        textViewWrong = findViewById(R.id.textViewWrongScore);
        buttonPlayAgain =findViewById(R.id.buttonPlayAgain);
        buttonExit = findViewById(R.id.buttonExit);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userUID = user.getUid();
                userCorrect = snapshot.child(userUID).child("Correct").getValue().toString();
                userWrong = snapshot.child(userUID).child("Wrong").getValue().toString();

                textViewCorrect.setText(userCorrect);
                textViewWrong.setText(userWrong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finishAffinity();

            }
        });

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScorePage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}