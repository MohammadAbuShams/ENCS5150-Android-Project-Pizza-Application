package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LogInActivity extends AppCompatActivity {

    static final LogInFragment logInFragment = new LogInFragment();
    static final LogInButtonFragment LogInButtonFragment = new LogInButtonFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.root_layout,   LogInButtonFragment, "LogInButtonFrag");
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton backToMain= (ImageButton) findViewById(R.id.backToMain);


        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder details = new StringBuilder();
                details.append("Are you sure you want to disconnect to the server?");

                AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                builder.setTitle("Confirm Disconnect")
                        .setMessage(details.toString())
                        .setNegativeButton("cancel",null)
                        .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                LogInActivity.this.startActivity(intent);
                                finish();
                            }
                        })
                        .create()
                        .show();

            }
        });


    }

    public void addLogInFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.root_layout, logInFragment, "LogInFrag");
        fragmentTransaction.commit();
    }
    public void removeLogInFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(logInFragment);
        fragmentTransaction.commit();
    }


}