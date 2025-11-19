package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepository {

    private final SQLiteDatabase db;

    public SubscriptionRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
    }

    public long insert(Subscription s) {
        ContentValues v = new ContentValues();
        v.put("client_id", s.getClientId());
        v.put("route_id", s.getRouteId());
        v.put("address", s.getAddress());
        v.put("start_date", s.getStartDate());
        v.put("end_date", s.getEndDate());
        return db.insert("subscriptions", null, v);
    }

    public int update(Subscription s) {
        ContentValues v = new ContentValues();
        v.put("client_id", s.getClientId());
        v.put("route_id", s.getRouteId());
        v.put("address", s.getAddress());
        v.put("start_date", s.getStartDate());
        v.put("end_date", s.getEndDate());

        return db.update("subscriptions", v, "id = ?", new String[]{String.valueOf(s.getId())});
    }

    public int delete(int id) {
        return db.delete("subscriptions", "id = ?", new String[]{String.valueOf(id)});
    }

    public Subscription getById(int id) {
        try (Cursor c = db.rawQuery("SELECT * FROM subscriptions WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;

            return new Subscription(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("client_id")),
                    c.getInt(c.getColumnIndexOrThrow("route_id")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("start_date")),
                    c.getString(c.getColumnIndexOrThrow("end_date"))
            );
        }
    }

    public List<Subscription> getAll() {
        List<Subscription> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM subscriptions ORDER BY id", null)) {
            while (c.moveToNext()) {
                Subscription s = new Subscription(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getInt(c.getColumnIndexOrThrow("client_id")),
                        c.getInt(c.getColumnIndexOrThrow("route_id")),
                        c.getString(c.getColumnIndexOrThrow("address")),
                        c.getString(c.getColumnIndexOrThrow("start_date")),
                        c.getString(c.getColumnIndexOrThrow("end_date"))
                );
                list.add(s);
            }
        }

        return list;
    }
}
