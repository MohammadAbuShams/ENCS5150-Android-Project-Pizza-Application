package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4; // Updated version
    public static final String DATABASE_NAME = "AdvancePizzaDB";

    private static final String TABLE_USERS = "users";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_PIZZAS = "pizzas";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_SPECIAL_OFFERS = "special_offers";
    private static final String TABLE_OFFER_PIZZAS = "offer_pizzas";

    public static final String KEY_EMAIL = "email";  // Primary key for users
    private static final String KEY_PHONE = "phone";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_GENDER = "gender";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PERMISSION = "permission";
    private static final String KEY_PROFILE_PICTURE = "profilePicture";
    private static final String KEY_ORDER_DATE = "order_date";
    private static final String KEY_ORDER_DETAILS = "order_details";

    public static final String KEY_PIZZA_ID = "pizza_id";
    public static final String KEY_PIZZA_NAME = "pizza_name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_SIZE = "size";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE_ID = "img_pizza";
    public static final String KEY_IS_FAVORITE = "is_favorite";

    private static final String COLUMN_OFFER_ID = "id";
    private static final String COLUMN_OFFER_NAME = "offerName";
    private static final String COLUMN_PIZZA_TYPE = "pizzaType";
    private static final String COLUMN_PIZZA_SIZE = "pizzaSize";
    private static final String COLUMN_OFFER_PERIOD = "offerPeriod";
    private static final String COLUMN_TOTAL_PRICE = "totalPrice";
    private static final String COLUMN_PIZZA_ID = "pizza_id"; // Used in offer_pizzas table

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseSetup", "Creating tables and inserting static data.");
        createUsersTable(db);
        createOrdersTable(db);
        createPizzasTable(db);
        createFavoritesTable(db);
        createSpecialOffersTable(db);
        createOfferPizzasTable(db);
        insertStaticAdmins(db);
    }

    private void insertStaticAdmins(SQLiteDatabase db) {
        try {
            // Hash the passwords for the admin accounts
            String password1 = User.hashPassword("admin@123");
            String password2 = User.hashPassword("d@123");

            // Admin 1: Joud Hijaz
            String insertAdmin1 = "INSERT INTO " + TABLE_USERS + " ("
                    + KEY_FIRST_NAME + ", "
                    + KEY_LAST_NAME + ", "
                    + KEY_GENDER + ", "
                    + KEY_EMAIL + ", "
                    + KEY_PASSWORD + ", "
                    + KEY_PHONE + ", "
                    + KEY_PERMISSION + ") VALUES ('Joud', 'Hijaz', 'Female', 'admin@gmail.com', '"
                    + password1 + "', '0599999999', 'Admin')";

            // Admin 2: Mohammad Ali
            String insertAdmin2 = "INSERT INTO " + TABLE_USERS + " ("
                    + KEY_FIRST_NAME + ", "
                    + KEY_LAST_NAME + ", "
                    + KEY_GENDER + ", "
                    + KEY_EMAIL + ", "
                    + KEY_PASSWORD + ", "
                    + KEY_PHONE + ", "
                    + KEY_PERMISSION + ") VALUES ('Mohammad', 'Ali', 'Male', 'd1@example.com', '"
                    + password2 + "', '0599999999', 'Admin')";

            // Execute the SQL statements to insert the static admins
            db.execSQL(insertAdmin1);
            db.execSQL(insertAdmin2);

            Log.d("DatabaseSetup", "Static admin users inserted successfully.");
        } catch (Exception e) {
            Log.e("DatabaseError", "Error inserting static admin users", e);
        }
    }

    private void createUsersTable(SQLiteDatabase db) {
        String CREATE_USERS_TABLE  = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_EMAIL + " TEXT PRIMARY KEY,"
                + KEY_PHONE + " TEXT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PERMISSION + " TEXT,"
                + KEY_PROFILE_PICTURE + " BLOB)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    private void createOrdersTable(SQLiteDatabase db) {
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + "order_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_ORDER_DATE + " DATETIME,"
                + KEY_ORDER_DETAILS + " TEXT,"
                + KEY_PIZZA_ID + " INTEGER,"
                + "image_id INTEGER,"
                + "FOREIGN KEY(" + KEY_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + KEY_EMAIL + "),"
                + "FOREIGN KEY(" + KEY_PIZZA_ID + ") REFERENCES " + TABLE_PIZZAS + "(" + KEY_PIZZA_ID + "))";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    private void createPizzasTable(SQLiteDatabase db) {
        String CREATE_PIZZAS_TABLE = "CREATE TABLE " + TABLE_PIZZAS + "("
                + KEY_PIZZA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PIZZA_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_SIZE + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_IMAGE_ID + " INTEGER)";
        db.execSQL(CREATE_PIZZAS_TABLE);
    }

    private void createFavoritesTable(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + " ("
                + "favorite_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_EMAIL + " TEXT, "
                + KEY_PIZZA_ID + " INTEGER, "
                + KEY_IS_FAVORITE + " INTEGER DEFAULT 0, "
                + "FOREIGN KEY(" + KEY_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + KEY_EMAIL + "), "
                + "FOREIGN KEY(" + KEY_PIZZA_ID + ") REFERENCES " + TABLE_PIZZAS + "(" + KEY_PIZZA_ID + "))";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    private void createSpecialOffersTable(SQLiteDatabase db) {
        String CREATE_SPECIAL_OFFERS_TABLE = "CREATE TABLE " + TABLE_SPECIAL_OFFERS + " ("
                + COLUMN_OFFER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_OFFER_NAME + " TEXT, "
                + COLUMN_PIZZA_TYPE + " TEXT, "
                + COLUMN_PIZZA_SIZE + " TEXT, "
                + COLUMN_OFFER_PERIOD + " TEXT, "
                + COLUMN_TOTAL_PRICE + " REAL)";
        db.execSQL(CREATE_SPECIAL_OFFERS_TABLE);
    }

    private void createOfferPizzasTable(SQLiteDatabase db) {
        String CREATE_OFFER_PIZZAS_TABLE = "CREATE TABLE " + TABLE_OFFER_PIZZAS + " ("
                + COLUMN_OFFER_ID + " INTEGER, "
                + COLUMN_PIZZA_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_OFFER_ID + ") REFERENCES " + TABLE_SPECIAL_OFFERS + "(" + COLUMN_OFFER_ID + "), "
                + "FOREIGN KEY(" + COLUMN_PIZZA_ID + ") REFERENCES " + TABLE_PIZZAS + "(" + KEY_PIZZA_ID + "))";
        db.execSQL(CREATE_OFFER_PIZZAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_PERMISSION + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_PROFILE_PICTURE + " BLOB");

            // Recreate orders table
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            createOrdersTable(db);
        }
        if (oldVersion < 4) {
            createSpecialOffersTable(db);
            createOfferPizzasTable(db);
        }
        // Ensure you add any future upgrades here
    }

    public boolean insertSpecialOffer(SpecialOffer specialOffer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_NAME, specialOffer.getOfferName());
        values.put(COLUMN_PIZZA_TYPE, specialOffer.getPizzaType());
        values.put(COLUMN_PIZZA_SIZE, specialOffer.getPizzaSize());
        values.put(COLUMN_OFFER_PERIOD, specialOffer.getOfferPeriod());
        values.put(COLUMN_TOTAL_PRICE, specialOffer.getTotalPrice());
        long result = db.insert(TABLE_SPECIAL_OFFERS, null, values);
        db.close();
        return result != -1;  // Return true if insert is successful
    }


    public boolean insertOfferPizza(int offerId, int pizzaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_ID, offerId);
        values.put(COLUMN_PIZZA_ID, pizzaId);
        long result = db.insert(TABLE_OFFER_PIZZAS, null, values);
        db.close();
        return result != -1;
    }

    // Retrieve all special offers


    public List<Pizza> getPizzasByOfferId(int offerId) {
        List<Pizza> pizzas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT DISTINCT p.*, IFNULL(f." + KEY_IS_FAVORITE + ", 0) AS is_favorite FROM " + TABLE_PIZZAS + " p "
                + "INNER JOIN " + TABLE_OFFER_PIZZAS + " op ON p." + KEY_PIZZA_ID + " = op." + COLUMN_PIZZA_ID
                + " LEFT JOIN " + TABLE_FAVORITES + " f ON p." + KEY_PIZZA_ID + " = f." + KEY_PIZZA_ID
                + " WHERE op." + COLUMN_OFFER_ID + " = ?";
        Log.d("DataBaseHelper", "Executing query for offer ID: " + offerId);
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(offerId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(KEY_PIZZA_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(KEY_PIZZA_NAME));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY));
                @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(KEY_SIZE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                @SuppressLint("Range") int imgPizza = cursor.getInt(cursor.getColumnIndex(KEY_IMAGE_ID));
                @SuppressLint("Range") boolean isFavorite = cursor.getInt(cursor.getColumnIndex("is_favorite")) == 1;

                pizzas.add(new Pizza(id, name, price, size, category, description, imgPizza, isFavorite));
                Log.d("DataBaseHelper", "Retrieved pizza: " + name + " for offer ID: " + offerId);
            } while (cursor.moveToNext());
        } else {
            Log.d("DataBaseHelper", "No pizzas found for offer ID: " + offerId);
        }
        cursor.close();
        return pizzas;
    }





    // Update a special offer
    public boolean updateSpecialOffer(int id, String offerName, String pizzaType, String pizzaSize, String offerPeriod, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_NAME, offerName);
        values.put(COLUMN_PIZZA_TYPE, pizzaType);
        values.put(COLUMN_PIZZA_SIZE, pizzaSize);
        values.put(COLUMN_OFFER_PERIOD, offerPeriod);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);

        int result = db.update(TABLE_SPECIAL_OFFERS, values, COLUMN_OFFER_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Delete a special offer
    public void deleteSpecialOffer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SPECIAL_OFFERS, COLUMN_OFFER_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Implement CRUD operations and other methods as previously defined

    // Method to insert a new user into the database
    public boolean insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_FIRST_NAME, user.getFirstName());
            contentValues.put(KEY_LAST_NAME, user.getLastName());
            contentValues.put(KEY_EMAIL, user.getEmail());
            contentValues.put(KEY_PHONE, user.getPhoneNumber());
            contentValues.put(KEY_GENDER, user.getGender());
            contentValues.put(KEY_PASSWORD, user.getPassword());
            contentValues.put(KEY_PERMISSION, user.getPermission());
            contentValues.put(KEY_PROFILE_PICTURE, user.getProfilePicture());

            long result = sqLiteDatabase.insert(TABLE_USERS, null, contentValues);
            Log.d("DatabaseOperation", "Insert result: " + result);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseError", "Error inserting user", e);
            return false;
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
    }

    @SuppressLint("Range")
    public User authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = getUserByEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(KEY_PASSWORD);
                if (columnIndex != -1) {
                    String passwordFromDb = cursor.getString(columnIndex);
                    if (User.checkPassword(password, passwordFromDb)) {
                        return new User(
                                cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)),
                                cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)),
                                email,
                                cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                                cursor.getString(cursor.getColumnIndex(KEY_GENDER)),
                                passwordFromDb,
                                cursor.getString(cursor.getColumnIndex(KEY_PERMISSION)),
                                cursor.getBlob(cursor.getColumnIndex(KEY_PROFILE_PICTURE))
                        );
                    }
                } else {
                    Log.e("DatabaseError", "Column not found in cursor");
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error authenticating user", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL + " = ?";
        Log.d("DatabaseQuery", "Executing query: " + selectQuery + " with email: " + email);
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email.trim()});
        Log.d("DatabaseQuery", "Query completed. Found: " + cursor.getCount() + " results.");
        return cursor;
    }

    public int updateUser(String email, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_USERS, values, KEY_EMAIL + " = ?", new String[]{email});
    }

    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_EMAIL + " = ?", new String[]{email});
        db.close();
    }

    public boolean insertPizzas(Pizza pizza) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PIZZA_NAME, pizza.getName());
        values.put(KEY_PRICE, pizza.getPrice());
        values.put(KEY_SIZE, pizza.getSize());
        values.put(KEY_CATEGORY, pizza.getCategory());
        values.put(KEY_DESCRIPTION, pizza.getDescription());
        values.put(KEY_IMAGE_ID, pizza.getImgPizza());

        long result = db.insert(TABLE_PIZZAS, null, values);
        db.close();
        return result != -1;
    }

    public long insertOrder(String userEmail, String orderDetails, String orderDate, int imageId, int pizzaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, userEmail);
        values.put(KEY_ORDER_DATE, orderDate);
        values.put(KEY_ORDER_DETAILS, orderDetails);
        values.put("image_id", imageId);
        values.put("pizza_id", pizzaId);
        long id = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return id;
    }

    private int getImageIdForPizza(int pizzaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PIZZAS, new String[]{KEY_IMAGE_ID}, KEY_PIZZA_ID + "=?", new String[]{String.valueOf(pizzaId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex(KEY_IMAGE_ID));
            cursor.close();
            return imageId;
        }
        return -1;
    }

    public void insertFavorite(String email, int pizzaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PIZZA_ID, pizzaId);
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public Cursor getFavoritesWithPizzaInfoByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p." + KEY_PIZZA_ID + ", p." + KEY_PIZZA_NAME + ", p." + KEY_PRICE + ", "
                + "p." + KEY_SIZE + ", p." + KEY_CATEGORY + ", p." + KEY_DESCRIPTION + ", "
                + "p." + KEY_IMAGE_ID + " "
                + "FROM " + TABLE_PIZZAS + " p JOIN " + TABLE_FAVORITES + " f ON p." + KEY_PIZZA_ID + " = f." + KEY_PIZZA_ID
                + " WHERE f." + KEY_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{email});
    }

    public Cursor getAllPizzas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PIZZAS;
        return db.rawQuery(selectQuery, null);
    }

    public List<Order> getAllOrders(String email) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDERS + " WHERE " + KEY_EMAIL + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int orderId = cursor.getInt(cursor.getColumnIndex("order_id"));
                @SuppressLint("Range") String orderDetails = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DETAILS));
                @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE));
                @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex("image_id"));
                orderList.add(new Order(orderId, orderDetails, orderDate, imageId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public Cursor getAllOrdersWithCustomerDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT orders.order_id, orders.order_details, orders.order_date, orders.image_id, users.first_name, users.last_name "
                + "FROM orders "
                + "JOIN users ON orders.email = users.email";
        return db.rawQuery(query, null);
    }

    public void deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, "order_id = ?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    public void updateUserInfo(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", user.getFirstName());
        contentValues.put("last_name", user.getLastName());
        contentValues.put("phone", user.getPhoneNumber());
        sqLiteDatabase.update(TABLE_USERS, contentValues, "Email = '" + user.getEmail() + "'", null);
    }

    public void updateUserPassword(String email, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Password", password);
        sqLiteDatabase.update(TABLE_USERS, contentValues, "Email = '" + email + "'", null);
    }

    public boolean updateUserProfilePicture(String email, byte[] profilePicture) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PROFILE_PICTURE, profilePicture);
        int rowsAffected = sqLiteDatabase.update(TABLE_USERS, contentValues, KEY_EMAIL + " = ?", new String[]{email});
        sqLiteDatabase.close();
        return rowsAffected > 0;
    }

    public void removeFavorite(String email, int pizzaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_EMAIL + " = ? AND " + KEY_PIZZA_ID + " = ?", new String[]{email, String.valueOf(pizzaId)});
        db.close();
    }
    public List<Pizza> getPizzasByTypeAndSize(String type, String size) {
        List<Pizza> pizzas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT p.*, IFNULL(f.is_favorite, 0) AS is_favorite FROM " + TABLE_PIZZAS + " p "
                + "LEFT JOIN " + TABLE_FAVORITES + " f ON p." + KEY_PIZZA_ID + " = f." + KEY_PIZZA_ID
                + " WHERE p." + KEY_CATEGORY + " = ? AND p." + KEY_SIZE + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{type, size});

        if (cursor != null) {
            Log.d("DataBaseHelper", "Cursor column names: " + Arrays.toString(cursor.getColumnNames()));
            Log.d("DataBaseHelper", "Cursor column count: " + cursor.getColumnCount());

            if (cursor.moveToFirst()) {
                do {
                    Log.d("DataBaseHelper", "Processing row: " + cursor.getPosition());

                    // Fetch column indices once outside the loop to avoid repeated calls
                    int idIndex = cursor.getColumnIndex(KEY_PIZZA_ID);
                    int nameIndex = cursor.getColumnIndex(KEY_PIZZA_NAME);
                    int priceIndex = cursor.getColumnIndex(KEY_PRICE);
                    int sizeIndex = cursor.getColumnIndex(KEY_SIZE);
                    int categoryIndex = cursor.getColumnIndex(KEY_CATEGORY);
                    int descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
                    int imgPizzaIndex = cursor.getColumnIndex(KEY_IMAGE_ID);
                    int isFavoriteIndex = cursor.getColumnIndex("is_favorite");

                    // Log all column indices
                    Log.d("DataBaseHelper", "Column indices - id: " + idIndex + ", name: " + nameIndex +
                            ", price: " + priceIndex + ", size: " + sizeIndex + ", category: " + categoryIndex +
                            ", description: " + descriptionIndex + ", img_pizza: " + imgPizzaIndex +
                            ", is_favorite: " + isFavoriteIndex);

                    try {
                        // Ensure all indices are valid
                        if (idIndex != -1 && nameIndex != -1 && priceIndex != -1 && sizeIndex != -1 &&
                                categoryIndex != -1 && descriptionIndex != -1 && imgPizzaIndex != -1 &&
                                isFavoriteIndex != -1) {

                            @SuppressLint("Range") int id = cursor.getInt(idIndex);
                            @SuppressLint("Range") String name = cursor.getString(nameIndex);
                            @SuppressLint("Range") double price = cursor.getDouble(priceIndex);
                            @SuppressLint("Range") String pizzaSize = cursor.getString(sizeIndex);
                            @SuppressLint("Range") String category = cursor.getString(categoryIndex);
                            @SuppressLint("Range") String description = cursor.getString(descriptionIndex);
                            @SuppressLint("Range") int imgPizza = cursor.getInt(imgPizzaIndex);
                            @SuppressLint("Range") boolean isFavorite = cursor.getInt(isFavoriteIndex) == 1;

                            pizzas.add(new Pizza(id, name, price, pizzaSize, category, description, imgPizza, isFavorite));
                        } else {
                            Log.e("DataBaseHelper", "One or more column indices are invalid.");
                        }
                    } catch (Exception e) {
                        Log.e("DataBaseHelper", "Error reading cursor data: ", e);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.d("DataBaseHelper", "No matching records found.");
            }
            cursor.close();
        } else {
            Log.e("DataBaseHelper", "Cursor is null.");
        }
        return pizzas;
    }



    public long addSpecialOffer(SpecialOffer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_NAME, offer.getOfferName());
        values.put(COLUMN_PIZZA_TYPE, offer.getPizzaType());
        values.put(COLUMN_PIZZA_SIZE, offer.getPizzaSize());
        values.put(COLUMN_OFFER_PERIOD, offer.getOfferPeriod());
        values.put(COLUMN_TOTAL_PRICE, offer.getTotalPrice());

        return db.insert(TABLE_SPECIAL_OFFERS, null, values);
    }

    public void addPizzaToOffer(int offerId, int pizzaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_ID, offerId);
        values.put(COLUMN_PIZZA_ID, pizzaId);

        db.insert(TABLE_OFFER_PIZZAS, null, values);
    }

    public SpecialOffer getSpecialOfferByAttributes(String offerName, String pizzaType, String pizzaSize, String offerPeriod) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SPECIAL_OFFERS
                + " WHERE " + COLUMN_OFFER_NAME + " = ?"
                + " AND " + COLUMN_PIZZA_TYPE + " = ?"
                + " AND " + COLUMN_PIZZA_SIZE + " = ?"
                + " AND " + COLUMN_OFFER_PERIOD + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{offerName, pizzaType, pizzaSize, offerPeriod});

        SpecialOffer specialOffer = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_OFFER_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_NAME));
            @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_TYPE));
            @SuppressLint("Range") String size = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_SIZE));
            @SuppressLint("Range") String period = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_PERIOD));
            @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));

            specialOffer = new SpecialOffer(id, name, type, size, period, price);
        }
        cursor.close();
        return specialOffer;
    }

    public void updateSpecialOffer(SpecialOffer specialOffer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL_PRICE, specialOffer.getTotalPrice());

        db.update(TABLE_SPECIAL_OFFERS, values, COLUMN_OFFER_ID + " = ?", new String[]{String.valueOf(specialOffer.getId())});
    }

    public List<SpecialOffer> getAllSpecialOffers() {
        List<SpecialOffer> specialOffers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SPECIAL_OFFERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_OFFER_ID));
                @SuppressLint("Range") String offerName = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_NAME));
                @SuppressLint("Range") String pizzaType = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_TYPE));
                @SuppressLint("Range") String pizzaSize = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_SIZE));
                @SuppressLint("Range") String offerPeriod = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_PERIOD));
                @SuppressLint("Range") double totalPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));

                SpecialOffer specialOffer = new SpecialOffer(id, offerName, pizzaType, pizzaSize, offerPeriod, totalPrice);

                // Fetch pizzas associated with this special offer
                List<Pizza> pizzas = getPizzasByOfferId(id);
                specialOffer.setPizzas(pizzas);

                specialOffers.add(specialOffer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return specialOffers;
    }
    public void applyOffer(SpecialOffer offer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String whereClause = KEY_CATEGORY + " = ? AND " + KEY_SIZE + " = ?";
        String[] whereArgs = new String[]{offer.getPizzaType(), offer.getPizzaSize()};

        Cursor cursor = db.query(TABLE_PIZZAS, null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(KEY_PIZZA_ID));
                @SuppressLint("Range") double originalPrice = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                double discountedPrice = originalPrice - (originalPrice * (offer.getTotalPrice() / 100));  // Assuming totalPrice is a percentage discount

                values.put(KEY_PRICE, discountedPrice);
                db.update(TABLE_PIZZAS, values, KEY_PIZZA_ID + " = ?", new String[]{String.valueOf(id)});
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
    }

}

