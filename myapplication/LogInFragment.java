package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class LogInFragment extends Fragment {

    private EditText editText_email;
    private EditText editText_password;
    private CheckBox checkBox_rememberMe;
    private SharedPrefManager sharedPrefManager;
    private DataBaseHelper dataBaseHelper;

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        dataBaseHelper = new DataBaseHelper(getActivity()); // Initialize once here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        editText_email = view.findViewById(R.id.editText_email);
        editText_password = view.findViewById(R.id.editText_password);
        checkBox_rememberMe = view.findViewById(R.id.checkBox_rememberMe);
        Button button_LogIn = view.findViewById(R.id.button_LogIn);
        Button btnOpenSignUp = view.findViewById(R.id.button_OpenSignUp);

        button_LogIn.setOnClickListener(this::attemptLogin);
        btnOpenSignUp.setOnClickListener(this::navigateToSignUp);
    }

    private void attemptLogin(View view) {
        String email = editText_email.getText().toString().trim();
        String password = editText_password.getText().toString();
        new Thread(() -> loginUser(email, password)).start();
    }

    @SuppressLint("Range")
    private void loginUser(String email, String password) {
        // Use the already initialized dataBaseHelper
        try (Cursor cursor = dataBaseHelper.getUserByEmail(email)) {
            if (cursor != null && cursor.moveToFirst()) {
                Log.d("LoginDebug", "User found: " + cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_EMAIL)));
                validateCredentials(cursor, password);
            } else {
                Log.d("LoginDebug", "Email not found in database: " + email);
                postError(editText_email, "Email not found");
            }
        }
    }

    @SuppressLint("Range")
    private void validateCredentials(Cursor cursor, String password) {
        String passwordFromDB = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_PASSWORD));
        String permission = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_PERMISSION));
        if (User.checkPassword(password, passwordFromDB)) {
            if (checkBox_rememberMe.isChecked()) {
                sharedPrefManager.writeString("email", cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_EMAIL)));
            } else {
                sharedPrefManager.writeString("email", "");
            }
            navigateBasedOnPermission(permission);
        } else {
            postError(editText_password, "Password is incorrect");
        }
    }

    private void navigateBasedOnPermission(String permission) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> {
            Intent intent = "Admin".equals(permission) ?
                    new Intent(getActivity(), HomeAdminActivity.class) :
                    new Intent(getActivity(), HomeNormalCustomerActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void navigateToSignUp(View view) {
        startActivity(new Intent(getActivity(), SignUpActivity.class));
        getActivity().finish();
    }

    private void postError(EditText editText, String error) {
        new Handler(Looper.getMainLooper()).post(() -> editText.setError(error));
    }

    @Override
    public void onResume() {
        super.onResume();
        String email = sharedPrefManager.readString("email", "");
        editText_email.setText(email);
        checkBox_rememberMe.setChecked(!email.isEmpty());
    }
}
