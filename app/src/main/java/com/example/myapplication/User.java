package com.example.myapplication;

import android.database.Cursor;
import android.text.TextUtils;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    public static Cursor currentUser;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String password;
    private String permission;
    private byte[] profilePicture;

    // Constructor for creating a new user during the sign-up process
    public User(String firstName, String lastName, String email, String phoneNumber, String gender, String password, String permission, byte[] profilePicture) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setGender(gender);
        setPassword(password);
        setPermission(permission);
        this.profilePicture = profilePicture;
    }

    public void setPermission(String newPermission) {
        this.permission = newPermission;
    }

    public String getRole() {
        return permission;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getPermission() {
        return permission;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    // Setters with validation
    public void setFirstName(String firstName) {
        if (!TextUtils.isEmpty(firstName) && firstName.length() >= 3) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("First name must not be less than 3 characters");
        }
    }

    public Cursor getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Cursor newUser) {
        currentUser = newUser;
    }

    public void setLastName(String lastName) {
        if (!TextUtils.isEmpty(lastName) && lastName.length() >= 3) {
            this.lastName = lastName;
        } else {
            throw new IllegalArgumentException("Last name must not be less than 3 characters");
        }
    }

    public void setEmail(String email) {
        if (isEmailValid(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Phone number must be 10 digits and start with '05'");
        }
    }

    public void setGender(String gender) {
        if (!TextUtils.isEmpty(gender)) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Gender is required");
        }
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            this.password = null;
        } else if (isPasswordValid(password)) {
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    // Helper methods for validation
    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("05\\d{8}");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    public static String hashPassword(String plainTextPassword) {
        int logRounds = 12; // Adjust the number of rounds as necessary
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(logRounds));
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
