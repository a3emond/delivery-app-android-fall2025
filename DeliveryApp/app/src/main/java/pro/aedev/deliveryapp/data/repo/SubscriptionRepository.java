package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepository {

    private static final String TAG = "SubscriptionRepository";
    private final SQLiteDatabase db;

    public SubscriptionRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
        Log.d(TAG, "Repository initialized");
    }

    // INSERT (product_id MUST be set here)
    public long insert(Subscription s) {
        Log.d(TAG, "insert client=" + s.getClientId() +
                " route=" + s.getRouteId() +
                " product=" + s.getProductId() +
                " qty=" + s.getQuantity());

        ContentValues v = new ContentValues();
        v.put("client_id", s.getClientId());
        v.put("route_id", s.getRouteId());
        v.put("address", s.getAddress());
        v.put("start_date", s.getStartDate());
        v.put("end_date", s.getEndDate());
        v.put("product_id", s.getProductId());
        v.put("quantity", s.getQuantity());

        long id = db.insert("subscriptions", null, v);
        Log.d(TAG, "insert result id=" + id);
        return id;
    }

    // UPDATE (product_id is NOT updated — immutable by RULE)
    public int update(Subscription s) {
        Log.d(TAG, "update id=" + s.getId() +
                " client=" + s.getClientId() +
                " route=" + s.getRouteId() +
                " qty=" + s.getQuantity());

        ContentValues v = new ContentValues();
        v.put("client_id", s.getClientId());
        v.put("route_id", s.getRouteId());
        v.put("address", s.getAddress());
        v.put("start_date", s.getStartDate());
        v.put("end_date", s.getEndDate());
        v.put("quantity", s.getQuantity());

        // product_id intentionally omitted

        int count = db.update("subscriptions", v, "id = ?", new String[]{String.valueOf(s.getId())});
        Log.d(TAG, "update affected rows=" + count);
        return count;
    }

    public int delete(int id) {
        Log.d(TAG, "delete id=" + id);
        int count = db.delete("subscriptions", "id = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "delete affected rows=" + count);
        return count;
    }

    public Subscription getById(int id) {
        Log.d(TAG, "getById id=" + id);

        try (Cursor c = db.rawQuery("SELECT * FROM subscriptions WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) {
                Log.d(TAG, "getById not found");
                return null;
            }

            Subscription s = new Subscription(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("client_id")),
                    c.getInt(c.getColumnIndexOrThrow("route_id")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("start_date")),
                    c.getString(c.getColumnIndexOrThrow("end_date")),
                    c.getInt(c.getColumnIndexOrThrow("product_id")),
                    c.getInt(c.getColumnIndexOrThrow("quantity"))
            );

            Log.d(TAG, "getById found client=" + s.getClientId() +
                    " route=" + s.getRouteId() +
                    " product=" + s.getProductId() +
                    " qty=" + s.getQuantity());

            return s;
        }
    }

    public List<Subscription> getAll() {
        Log.d(TAG, "getAll");
        List<Subscription> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM subscriptions ORDER BY id", null)) {
            while (c.moveToNext()) {
                Subscription s = new Subscription(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getInt(c.getColumnIndexOrThrow("client_id")),
                        c.getInt(c.getColumnIndexOrThrow("route_id")),
                        c.getString(c.getColumnIndexOrThrow("address")),
                        c.getString(c.getColumnIndexOrThrow("start_date")),
                        c.getString(c.getColumnIndexOrThrow("end_date")),
                        c.getInt(c.getColumnIndexOrThrow("product_id")),
                        c.getInt(c.getColumnIndexOrThrow("quantity"))
                );
                list.add(s);
            }
        }

        Log.d(TAG, "getAll count=" + list.size());
        return list;
    }
}
