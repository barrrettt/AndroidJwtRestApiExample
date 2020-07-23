package com.barrrettt.androidjwtrestapiexample.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.barrrettt.androidjwtrestapiexample.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        //onclick Sing in
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            Toast.makeText(getApplicationContext(), "Hola " +username, Toast.LENGTH_LONG).show();
            }
        });
    }


}