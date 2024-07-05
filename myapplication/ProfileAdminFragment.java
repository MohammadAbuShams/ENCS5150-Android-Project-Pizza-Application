package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

public class ProfileAdminFragment extends Fragment {

    private TextView textView_email;

    private Cursor currentUser;
    private EditText editText_firstName;
    private EditText editText_lastName;
    private EditText editText_password;
    private EditText editText_confirmPassword;
    private EditText editText_phoneNumber;
    private Button button_save;
    private ImageView genderIcon;
    private boolean changePass = false;
    private DataBaseHelper dataBaseHelper;


    private static final String TAG = "ProfileAdminFragment";
    private String gender;

    public static ProfileAdminFragment newInstance() {
        return new ProfileAdminFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_admin, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView_email = view.findViewById(R.id.textView_email);
        editText_password = view.findViewById(R.id.editText_editPassword);
        editText_confirmPassword = view.findViewById(R.id.editText_editConfirmPassword);
        editText_firstName = view.findViewById(R.id.editText_editFirstName);
        editText_lastName = view.findViewById(R.id.editText_editLastName);
        editText_phoneNumber = view.findViewById(R.id.editText_editPhoneNumber);
        button_save = view.findViewById(R.id.button_saveEdit);
        genderIcon = view.findViewById(R.id.genderIcon);
        dataBaseHelper = new DataBaseHelper(getActivity());

        // Assuming you are storing the logged-in user's email in shared preferences
        String userEmail = SharedPrefManager.getInstance(getContext()).readString("email", "");
        currentUser = dataBaseHelper.getUserByEmail(userEmail);  // Assuming getUserByEmail method returns a Cursor
        assignInfoToInputFields(currentUser);

        button_save.setOnClickListener(v -> saveUserData());
        genderIcon.setOnClickListener(v -> toggleGender());
    }

    private void toggleGender() {
        if (gender != null && gender.equalsIgnoreCase("Male")) {
            gender = "Female";
            genderIcon.setImageResource(R.drawable.baseline_female_35);
        } else {
            gender = "Male";
            genderIcon.setImageResource(R.drawable.baseline_male_35);
        }
    }

    private void saveUserData() {
        String firstName = editText_firstName.getText().toString();
        String lastName = editText_lastName.getText().toString();
        String password = editText_password.getText().toString();
        String confirmPassword = editText_confirmPassword.getText().toString();
        String phoneNumber = editText_phoneNumber.getText().toString();
        String email = currentUser.getString(currentUser.getColumnIndexOrThrow("email"));

        if (!validateInput(firstName, lastName, phoneNumber, password, confirmPassword)) {
            return;
        }

        if (gender == null || gender.isEmpty()) {
            Toast.makeText(getActivity(), "Gender is required", Toast.LENGTH_SHORT).show();
            return;
        }



        if (!password.isEmpty()) {
            changePass = true;
            dataBaseHelper.updateUserPassword(email, User.hashPassword(password));
        }

        User user = new User(firstName, lastName, email, phoneNumber, gender, null, null, null);
        dataBaseHelper.updateUserInfo(user);
        Toast.makeText(getActivity(), "Profile Updated successfully", Toast.LENGTH_SHORT).show();

        // Refresh the user data to verify the changes
        currentUser = dataBaseHelper.getUserByEmail(email);
        assignInfoToInputFields(currentUser);
    }

    @SuppressLint("Range")
    private void assignInfoToInputFields(Cursor currentUser) {
        if (currentUser != null && currentUser.moveToFirst()) {
            Log.d(TAG, "Assigning user info to input fields");

            int emailIndex = currentUser.getColumnIndex("email");
            int firstNameIndex = currentUser.getColumnIndex("first_name");
            int lastNameIndex = currentUser.getColumnIndex("last_name");
            int phoneIndex = currentUser.getColumnIndex("phone");
            int genderIndex = currentUser.getColumnIndex("gender");

            Log.d(TAG, "Email index: " + emailIndex);
            Log.d(TAG, "First Name index: " + firstNameIndex);
            Log.d(TAG, "Last Name index: " + lastNameIndex);
            Log.d(TAG, "Phone index: " + phoneIndex);
            Log.d(TAG, "Gender index: " + genderIndex);

            if (emailIndex != -1) {
                textView_email.setText(currentUser.getString(emailIndex));
            }
            if (firstNameIndex != -1) {
                editText_firstName.setText(currentUser.getString(firstNameIndex));
            }
            if (lastNameIndex != -1) {
                editText_lastName.setText(currentUser.getString(lastNameIndex));
            }
            if (phoneIndex != -1) {
                editText_phoneNumber.setText(currentUser.getString(phoneIndex));
            }

            if (genderIndex != -1) {
                gender = currentUser.getString(genderIndex);
                if (gender != null) {
                    Log.d(TAG, "User gender: " + gender);
                    if (gender.equalsIgnoreCase("Male")) {
                        genderIcon.setImageResource(R.drawable.baseline_male_35);
                    } else if (gender.equalsIgnoreCase("Female")) {
                        genderIcon.setImageResource(R.drawable.baseline_female_35);
                    }
                }
            }
        } else {
            Log.d(TAG, "Current user data is null or cursor is empty");
        }
    }

    private boolean validateInput(String firstName, String lastName, String phoneNumber, String password, String confirmPassword) {
        boolean valid = true;

        if (firstName.length() < 3) {
            editText_firstName.setError("First name must not be less than 3 characters");
            valid = false;
        }
        if (lastName.length() < 3) {
            editText_lastName.setError("Last name must not be less than 3 characters");
            valid = false;
        }
        if (!isPhoneNumberValid(phoneNumber)) {
            editText_phoneNumber.setError("Phone number must be 10 digits and start with '05'");
            valid = false;
        }
        if (!password.isEmpty() && !password.equals(confirmPassword)) {
            editText_confirmPassword.setError("Passwords do not match");
            valid = false;
        }
        if (!password.isEmpty() && !isPasswordValid(password)) {
            editText_password.setError("Password must be at least 8 characters and include at least 1 number and 1 letter");
            valid = false;
        }
        return valid;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^05\\d{8}$");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[a-zA-Z].*");
    }



}
