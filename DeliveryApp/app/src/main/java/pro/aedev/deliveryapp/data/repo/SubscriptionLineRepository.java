package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.SubscriptionLine;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionLineRepository {

    private static final String TAG = "SubscriptionLineRepo";
    private final SQLiteDatabase db;

    public SubscriptionLineRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
        Log.d(TAG, "Repository initialized");
    }

    public long insert(SubscriptionLine sl) {
        Log.d(TAG, "insert: subscription=" + sl.getSubscriptionId() + " product=" + sl.getProductId() + " qty=" + sl.getQuantity());
        ContentValues v = new ContentValues();
        v.put("subscription_id", sl.getSubscriptionId());
        v.put("product_id", sl.getProductId());
        v.put("quantity", sl.getQuantity());
        long id = db.insert("subscription_lines", null, v);
        Log.d(TAG, "insert result id=" + id);
        return id;
    }

    public int update(SubscriptionLine sl) {
        Log.d(TAG, "update id=" + sl.getId() + " subscription=" + sl.getSubscriptionId() + " product=" + sl.getProductId() + " qty=" + sl.getQuantity());
        ContentValues v = new ContentValues();
        v.put("subscription_id", sl.getSubscriptionId());
        v.put("product_id", sl.getProductId());
        v.put("quantity", sl.getQuantity());

        int count = db.update("subscription_lines", v, "id = ?", new String[]{String.valueOf(sl.getId())});
        Log.d(TAG, "update affected rows=" + count);
        return count;
    }

    public int delete(int id) {
        Log.d(TAG, "delete id=" + id);
        int count = db.delete("subscription_lines", "id = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "delete affected rows=" + count);
        return count;
    }

    public SubscriptionLine getById(int id) {
        Log.d(TAG, "getById id=" + id);
        try (Cursor c = db.rawQuery("SELECT * FROM subscription_lines WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) {
                Log.d(TAG, "getById not found");
                return null;
            }

            SubscriptionLine sl = new SubscriptionLine(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("subscription_id")),
                    c.getInt(c.getColumnIndexOrThrow("product_id")),
                    c.getInt(c.getColumnIndexOrThrow("quantity"))
            );

            Log.d(TAG, "getById found subscription=" + sl.getSubscriptionId());
            return sl;
        }
    }

    public List<SubscriptionLine> getAll() {
        Log.d(TAG, "getAll");
        List<SubscriptionLine> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM subscription_lines ORDER BY id", null)) {
            while (c.moveToNext()) {
                SubscriptionLine sl = new SubscriptionLine(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getInt(c.getColumnIndexOrThrow("subscription_id")),
                        c.getInt(c.getColumnIndexOrThrow("product_id")),
                        c.getInt(c.getColumnIndexOrThrow("quantity"))
                );
                list.add(sl);
            }
        }

        Log.d(TAG, "getAll count=" + list.size());
        return list;
    }

    public List<SubscriptionLine> getBySubscription(int subscriptionId) {
        Log.d(TAG, "getBySubscription id=" + subscriptionId);
        List<SubscriptionLine> list = new ArrayList<>();

        try (Cursor c = db.rawQuery(
                "SELECT * FROM subscription_lines WHERE subscription_id = ?",
                new String[]{String.valueOf(subscriptionId)}
        )) {
            while (c.moveToNext()) {
                SubscriptionLine sl = new SubscriptionLine(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getInt(c.getColumnIndexOrThrow("subscription_id")),
                        c.getInt(c.getColumnIndexOrThrow("product_id")),
                        c.getInt(c.getColumnIndexOrThrow("quantity"))
                );
                list.add(sl);
            }
        }

        Log.d(TAG, "getBySubscription count=" + list.size());
        return list;
    }
}
