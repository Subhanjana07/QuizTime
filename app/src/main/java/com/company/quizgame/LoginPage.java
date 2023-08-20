package com.company.quizgame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText editTextemail,editTextpassword;
    Button buttonsignIn;
    SignInButton buttonsignInGoogle;
    TextView textViewsignUp,textViewforgotPassword;
    ProgressBar progressBarSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        Toolbar toolbar = findViewById(R.id.toolbarSignin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Time");

        editTextemail = findViewById(R.id.editTextSigninEmailAddress);
        editTextpassword = findViewById(R.id.editTextSignInPassword);
        buttonsignIn = findViewById(R.id.buttonSignin);
        buttonsignInGoogle = findViewById(R.id.signInButtonGoogle);
        textViewsignUp = findViewById(R.id.textViewNoAccount);
        textViewforgotPassword = findViewById(R.id.textViewForgotPass);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);

        progressBarSignIn.setVisibility(View.INVISIBLE);

        registerActivityForGoogleSignIn();

        buttonsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextemail.getText().toString();
                String password = editTextpassword.getText().toString();
                if(email.equals("")) {
                    Toast.makeText(LoginPage.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")) {
                    Toast.makeText(LoginPage.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                }
                else {
                    signInFirebase(email, password);
                        editTextemail.setText("");
                        editTextpassword.setText("");
                }

            }
        });

        buttonsignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOutGoogle();

                signInGoogle();
            }
        });

        textViewsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPage.this, SigbUpPage.class);
                startActivity(intent);

            }
        });

        textViewforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPage.this, Forgotpassword.class);
                startActivity(intent);
            }
        });
    }

    public void signOutGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("656067261269-bmemcrhrlp95dsl1huiqsk28i4dpgovf.apps.googleusercontent.com")
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Sign-out completed successfully, you can proceed with the new sign-in
                    }
                });
    }

    public void signInGoogle(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("656067261269-bmemcrhrlp95dsl1huiqsk28i4dpgovf.apps.googleusercontent.com")
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signIn();
    }

    public void signIn(){

        Intent signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }

    public void registerActivityForGoogleSignIn(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            firebaseSignInWithGoogle(task);
                        }
                    }
                });

    }
    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task){

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "Login Successfull !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            finish();
            firebaseGoogleAccount(account);
        }catch (ApiException e){
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseGoogleAccount(GoogleSignInAccount account){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = auth.getCurrentUser();
                }
                else{

                }
            }
        });
    }

    public void signInFirebase(String useremail,String userpass)
    {
        progressBarSignIn.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(useremail,userpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressBarSignIn.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginPage.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBarSignIn.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginPage.this, "credentials do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}