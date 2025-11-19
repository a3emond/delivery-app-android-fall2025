package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.SubscriptionLine;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionLineRepository {

    private final SQLiteDatabase db;

    public SubscriptionLineRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();

    }

    public long insert(SubscriptionLine sl) {
        ContentValues v = new ContentValues();
        v.put("subscription_id", sl.getSubscriptionId());
        v.put("product_id", sl.getProductId());
        v.put("quantity", sl.getQuantity());
        return db.insert("subscription_lines", null, v);
    }

    public int update(SubscriptionLine sl) {
        ContentValues v = new ContentValues();
        v.put("subscription_id", sl.getSubscriptionId());
        v.put("product_id", sl.getProductId());
        v.put("quantity", sl.getQuantity());

        return db.update("subscription_lines", v, "id = ?", new String[]{String.valueOf(sl.getId())});
    }

    public int delete(int id) {
        return db.delete("subscription_lines", "id = ?", new String[]{String.valueOf(id)});
    }

    public SubscriptionLine getById(int id) {
        try (Cursor c = db.rawQuery("SELECT * FROM subscription_lines WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;

            return new SubscriptionLine(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("subscription_id")),
                    c.getInt(c.getColumnIndexOrThrow("product_id")),
                    c.getInt(c.getColumnIndexOrThrow("quantity"))
            );
        }
    }

    public List<SubscriptionLine> getAll() {
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

        return list;
    }

    public List<SubscriptionLine> getBySubscription(int subscriptionId) {
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

        return list;
    }
}
