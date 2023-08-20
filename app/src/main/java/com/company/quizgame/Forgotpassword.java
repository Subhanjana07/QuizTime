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
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText editTextForgotPassemail;
    Button buttonReset;
    ProgressBar progressBarReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        Toolbar toolbar = findViewById(R.id.toolbarForgotPass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Time");

        editTextForgotPassemail = findViewById(R.id.editTextfForgotPassEmail);
        buttonReset = findViewById(R.id.buttonReset);
        progressBarReset = findViewById(R.id.progressBarReset);

        progressBarReset.setVisibility(View.INVISIBLE);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextForgotPassemail.getText().toString();
                if(email.equals(""))
                {
                    Toast.makeText(Forgotpassword.this, "Email field cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    resetPassword(email);
                }
            }
        });
    }

    public void resetPassword(String userEmail){
        progressBarReset.setVisibility(View.VISIBLE);
        buttonReset.setClickable(false);
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBarReset.setVisibility(View.INVISIBLE);
                    Toast.makeText(Forgotpassword.this, "Password Reset Link sent to the email", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    progressBarReset.setVisibility(View.INVISIBLE);
                    Toast.makeText(Forgotpassword.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}