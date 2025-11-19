package pro.aedev.deliveryapp.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "delivery.db";
    private static final int DB_VERSION = 1;

    public AppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // ROUTES
    private static final String CREATE_ROUTES =
            "CREATE TABLE routes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "label TEXT," +
                    "deliverer_id INTEGER" +
                    ");";

    // DELIVERERS
    private static final String CREATE_DELIVERERS =
            "CREATE TABLE deliverers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "phone TEXT" +
                    ");";

    // CLIENTS
    private static final String CREATE_CLIENTS =
            "CREATE TABLE clients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "phone TEXT" +
                    ");";

    // PRODUCTS
    private static final String CREATE_PRODUCTS =
            "CREATE TABLE products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "type TEXT NOT NULL" +
                    ");";

    // SUBSCRIPTIONS
    private static final String CREATE_SUBSCRIPTIONS =
            "CREATE TABLE subscriptions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "client_id INTEGER NOT NULL," +
                    "route_id INTEGER NOT NULL DEFAULT 0," +
                    "address TEXT NOT NULL," +
                    "start_date TEXT," +
                    "end_date TEXT" +
                    ");";

    // SUBSCRIPTION_LINES
    private static final String CREATE_SUBSCRIPTION_LINES =
            "CREATE TABLE subscription_lines (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subscription_id INTEGER NOT NULL," +
                    "product_id INTEGER NOT NULL," +
                    "quantity INTEGER NOT NULL" +
                    ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ROUTES);
        db.execSQL(CREATE_DELIVERERS);
        db.execSQL(CREATE_CLIENTS);
        db.execSQL(CREATE_PRODUCTS);
        db.execSQL(CREATE_SUBSCRIPTIONS);
        db.execSQL(CREATE_SUBSCRIPTION_LINES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS subscription_lines");
        db.execSQL("DROP TABLE IF EXISTS subscriptions");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS clients");
        db.execSQL("DROP TABLE IF EXISTS deliverers");
        db.execSQL("DROP TABLE IF EXISTS routes");
        onCreate(db);
    }
}