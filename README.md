# Advance Pizza Android Application

## Project Overview
Advance Pizza is a user-friendly Android application that enables customers of a pizza restaurant to order their favorite pizzas online or through a local database. This project is created to streamline the ordering process and enhance the customer experience with intuitive navigation and comprehensive features for managing user profiles and orders.

## Features

### User Functionalities
- **Introduction Layout**: Includes a "Get Started" button to fetch pizza types from the server. Handles connection success or failure appropriately.
- **Login and Registration Layout**:
  - Secure login with email and password.
  - New user registration with extensive validation.
- **Customer Home Layout (Navigation Drawer Activity)**:
  - Home: Displays restaurant history.
  - Pizza Menu: Lists pizzas with options to add to favorites and order.
  - Your Orders: Shows past orders with detailed views.
  - Your Favorites: Manages favorite pizzas with ordering functionality.
  - Special Offers: Lists current promotions.
  - Profile: Allows users to edit their profile.
  - Contact Us: Includes options to call, find, or email the restaurant.
  - Logout: Logs out and redirects to the login page.

### Admin Functionalities
- **Admin Home Layout (Navigation Drawer Activity)**:
  - Admin Profile: View and edit personal information.
  - Add Admin: Add new admins with full validations.
  - View All Orders: Overview of all customer orders.
  - Add Special Offers: Create and manage promotions.
  - Logout: Security feature to log out and secure the session.

## Technical Specifications
- **Languages and Frameworks**: Java, Android SDK.
- **Database**: SQLite for local storage.
- **API Integration**: RESTful services to fetch pizza types.
- **Additional Components**:
  - Android Layouts (dynamic and static).
  - Intents & Notifications for real-time user feedback.
  - Animations (Frame and Tween) to enhance user interaction.
  - Fragments for modular design.
  - Shared Preferences to store user session data.
