package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button button_registerLogIn = (Button) findViewById(R.id.button_registerLogIn);
        ImageButton button_backToMain = (ImageButton) findViewById(R.id.button_registerBackToMain);

        button_registerLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the sign in activity
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                SignUpActivity.this.startActivity(intent);
                finish();
            }
        });

        // set the listener for the back button
        button_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the main activity
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                SignUpActivity.this.startActivity(intent);
                finish();
            }
        });




    }
    public void navigateToLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish(); // Ensure this activity is finished so the back button won't return here.
    }



}