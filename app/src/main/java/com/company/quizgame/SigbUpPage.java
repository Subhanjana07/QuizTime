package com.company.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigbUpPage extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    EditText signUpEmail,signUpPassword;
    Button buttonSignUp;
    ProgressBar progressBarSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigb_up_page);

        Toolbar toolbar = findViewById(R.id.toolbarSignUp);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz Time");

        signUpEmail = findViewById(R.id.editTextSignUpEmailAddress);
        signUpPassword = findViewById(R.id.editTextSignUpPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        progressBarSignUp = findViewById(R.id.progressBarSignUp);

        progressBarSignUp.setVisibility(View.INVISIBLE);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = signUpEmail.getText().toString();
                String password = signUpPassword.getText().toString();
                if(email.equals("")) {
                    Toast.makeText(SigbUpPage.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                    else if (password.equals("")) {
                    Toast.makeText(SigbUpPage.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                }
                else {
                    signUpFirebase(email, password);
                }

            }
        });
    }

    public void signUpFirebase(String useremail,String userpassword)
    {
        buttonSignUp.setClickable(false);
        progressBarSignUp.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(this
                , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    progressBarSignUp.setVisibility(View.INVISIBLE);
                    Toast.makeText(SigbUpPage.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(SigbUpPage.this, "There was a problem.Account creation unsuccessful", Toast.LENGTH_SHORT).show();
                    progressBarSignUp.setVisibility(View.INVISIBLE);
                }

            }
        });

    }
}