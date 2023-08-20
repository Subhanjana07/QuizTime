package com.company.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Questions");
    AppCompatButton startQuiz;
    ProgressBar progressBar;
    String question,optionA,optionB,optionC,optionD,correctAns;
    int questionNumber = 1;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Quiz Time");
        startQuiz = findViewById(R.id.buttonStart);
        progressBar = findViewById(R.id.progressBarloadQuestion);

        progressBar.setVisibility(View.INVISIBLE);

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.signOut)
        {
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void game()
    {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                question = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                optionA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                optionB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                optionC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                optionD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                correctAns = dataSnapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();
                Intent intent = new Intent(MainActivity.this,QuizPage.class);
                intent.putExtra("question",question);
                intent.putExtra("optionA",optionA);
                intent.putExtra("optionB",optionB);
                intent.putExtra("optionC",optionC);
                intent.putExtra("optionD",optionD);
                intent.putExtra("correctAns",correctAns);
                startActivity(intent);
                Log.d("FirebaseData", "question is: " + question);
                Log.d("FirebaseData", "optionA is: " + optionA);
                Log.d("FirebaseData", "optionB is: " + optionB);
                Log.d("FirebaseData", "optionC is: " + optionC);
                Log.d("FirebaseData", "optionD is: " + optionD);
                Log.d("FirebaseData", "correctAns is: " + correctAns);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                Log.w("Firebase Data", "Failed to read value.", error.toException());
            }
        });
    }
}