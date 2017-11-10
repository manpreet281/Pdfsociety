package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.navjot.deepak.manpreet.pdfsociety.Activities.NavDrawer.HomeActivity;
import com.navjot.deepak.manpreet.pdfsociety.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends Progressdialog {

    EditText txtEmail,txtPassword;
    Button btnSignIn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        txtEmail = (EditText) findViewById(R.id.EditTextEmail);
        txtPassword = (EditText) findViewById(R.id.EditTextPassword);
        btnSignIn = (Button) findViewById(R.id.ButtonSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInClicked();
            }
        });
    }

    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){
        hideProgressDialog();
        if(user != null){
            startActivity(
                    new Intent(SignIn.this, HomeActivity.class)
            );
            finish();
        }
    }

    private void SignInClicked(){

        if(TextUtils.isEmpty(txtEmail.getText().toString().trim())){
            txtEmail.setError("Fields can't be empty");

        }
        else if(TextUtils.isEmpty(txtPassword.getText().toString().trim())){
            txtPassword.setError("Fields can't be empty");
        }
            else if (!emailValidator(txtEmail.getText().toString())) {
            txtEmail.setError("Please enter a valid Email address ");
            }
            else{
                authSignIn(txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim());
        }
    }

    public void authSignIn(String email, String password){
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified();
                        } else {
                            Toast.makeText(SignIn.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void checkIfEmailVerified(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()){
            Toast.makeText(getApplicationContext(),
                    "Login successful", Toast.LENGTH_SHORT).show();
            Intent sendToHome = new Intent(getApplicationContext(),
                    HomeActivity.class);
            startActivity(sendToHome);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Email verification is not complete",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String  email_pattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+";
        pattern = Pattern.compile(email_pattern);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void onclick(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));
    }

}
