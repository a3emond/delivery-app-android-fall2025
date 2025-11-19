package pro.aedev.deliveryapp.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AppDatabase {

    private static AppDatabase instance;
    private final SQLiteDatabase db;

    private AppDatabase(Context context) {
        AppDatabaseHelper helper = new AppDatabaseHelper(context.getApplicationContext());
        db = helper.getWritableDatabase();  // Only ONE db instance in the app
    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new AppDatabase(context);
        }
        return instance;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
