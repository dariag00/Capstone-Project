package com.kloso.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    String TAG = LoginActivity.class.getName();

    @BindView(R.id.pb_login)
    ProgressBar progressBar;
    @BindView(R.id.et_login_email)
    EditText emailEditText;
    @BindView(R.id.et_login_password)
    EditText passwordEditText;
    @BindView(R.id.button_login)
    Button loginButton;
    @BindView(R.id.form_login)
    ScrollView formLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        loginButton.setOnClickListener(it -> processLoginAttempt());

        changeFormVisibility(true);

    }

    private void processLoginAttempt(){
        changeFormVisibility(false);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Log.d(TAG, "Login attempt received. Username: <" + email + "> password: <" + password + ">");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void changeFormVisibility(boolean showLoginForm){
        formLogin.setVisibility(showLoginForm ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(showLoginForm ? View.GONE :  View.VISIBLE);
    }

}
