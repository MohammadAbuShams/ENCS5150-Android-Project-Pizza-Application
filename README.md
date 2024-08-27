# Advance Pizza Android Application

## Project Overview
Advance Pizza is an Android application designed for a pizza restaurant to enable their customers to order pizzas online using either a direct server connection or a local database. The application focuses on a user-friendly interface with comprehensive features for both customers and administrators.

## Features

### User Functionalities:
- **Introduction Layout**: Start screen with a "Get Started" button that initiates a server connection to load available pizza types.
- **Login and Registration**: Secure access with encrypted password storage, including form validations and persistent login feature using shared preferences.
- **Customer Home Layout**:
  - **Navigation Drawer** with options like Home, Pizza Menu, Orders, Favorites, Special Offers, Profile, Contact, and Location.
  - Interactions include adding to favorites, placing orders, and detailed views of pizza types using fragments.

### Admin Functionalities:
- **Admin Home Layout**:
  - Manage profiles, add admins, and view all orders.
  - Add and manage special offers.
  - Track orders and calculate income based on pizza types.

## Technical Specifications
- **Android Layouts**: Dynamically and statically designed layouts.
- **Intents & Notifications**: Toast messages for user interactions.
- **Data Management**: SQLite for local data storage.
- **Animations**: Includes Frame and Tween animations to enhance UX.
- **Components**: Utilizes Fragments, Shared Preferences, and RESTful services.


