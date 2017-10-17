package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.navjot.deepak.manpreet.pdfsociety.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {

    EditText txtEmail,txtPassword;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        txtEmail = (EditText) findViewById(R.id.EditTextEmail);
        txtPassword = (EditText) findViewById(R.id.EditTextPassword);
        btnSignIn = (Button) findViewById(R.id.ButtonSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    private void SignIn(){

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
            Toast.makeText(getApplicationContext(),"Sign In Successful",Toast.LENGTH_SHORT).show();
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
