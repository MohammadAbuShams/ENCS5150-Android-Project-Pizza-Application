package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends Fragment {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPhoneNumber, editTextPassword, editTextConfirmPassword;
    private Spinner spinnerGender;
    private Button buttonSignUp;
    private boolean firstGender = true; // Flag to check if it's the first interaction with the spinner

    // Gender options for the spinner
    private String[] genders = {"Gender", "Male", "Female"};
    private String permission;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String permission) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("PERMISSION", permission);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeViews(view);

        if (getArguments() != null) {
            permission = getArguments().getString("PERMISSION", "User");
        } else {
            permission = "User";
        }

        buttonSignUp.setOnClickListener(v -> attemptSignUp());
        return view;
    }

    private void initializeViews(View view) {
        editTextFirstName = view.findViewById(R.id.editText_registerFirstName);
        editTextLastName = view.findViewById(R.id.editText_registerLastName);
        editTextEmail = view.findViewById(R.id.editText_registerEmail);
        editTextPhoneNumber = view.findViewById(R.id.editText_registerPhoneNumber);
        spinnerGender = view.findViewById(R.id.spinner_registerGender);
        editTextPassword = view.findViewById(R.id.editText_registerPassword);
        editTextConfirmPassword = view.findViewById(R.id.editText_registerConfirmPassword);
        buttonSignUp = view.findViewById(R.id.button_registerSignUp);

        // Set up the spinner with the genders array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Handle spinner interactions
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (firstGender) {
                    // Change the color of the hint if needed
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey));
                    firstGender = false;
                } else {
                    if (pos == 0) {
                        // Show toast to select Gender
                        Toast.makeText(getActivity(), "Please select a Gender", Toast.LENGTH_SHORT).show();
                        spinnerGender.setSelection(1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void attemptSignUp() {
        boolean valid = true;

        // Get values from EditTexts and Spinner
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // Validate each field
        if (!isEmailValid(email)) {
            editTextEmail.setError("Invalid email address");
            valid = false;
        }
        if (!isPhoneNumberValid(phoneNumber)) {
            editTextPhoneNumber.setError("Phone number must be 10 digits and start with '05'");
            valid = false;
        }
        if (firstName.length() < 3) {
            editTextFirstName.setError("First name must not be less than 3 characters");
            valid = false;
        }
        if (lastName.length() < 3) {
            editTextLastName.setError("Last name must not be less than 3 characters");
            valid = false;
        }
        if (!isPasswordValid(password)) {
            editTextPassword.setError("Password must be at least 8 characters and include at least 1 number and 1 letter");
            valid = false;
        }
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            valid = false;
        }
        if (gender.equals("Gender")) {
            Toast.makeText(getActivity(), "Please select a valid gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid) {
            User user = new User(firstName, lastName, email, phoneNumber, gender, password, permission, null);
            // add the user to the database
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());

            // check if the user is added successfully
            boolean isInserted = dataBaseHelper.insertUser(user);
            if (isInserted) {
                Toast.makeText(getActivity(), "User Registered successfully", Toast.LENGTH_SHORT).show();
                // open the sign in activity
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else {
                editTextEmail.setError("Email is already Exist");
                Toast.makeText(getActivity(), "Email is already Exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("05\\d{8}");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }
}
