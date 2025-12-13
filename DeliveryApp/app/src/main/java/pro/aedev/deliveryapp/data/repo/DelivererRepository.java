package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Deliverer;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing Deliverer entities in the database.
 */
public class DelivererRepository {

    private static final String TAG = "DelivererRepository";
    private final SQLiteDatabase db;

    public DelivererRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
        Log.d(TAG, "Repository initialized");
    }

    public long insert(Deliverer d) {
        Log.d(TAG, "insert: name=" + d.getName());
        ContentValues v = new ContentValues();
        v.put("name", d.getName());
        v.put("address", d.getAddress());
        v.put("phone", d.getPhone());
        long id = db.insert("deliverers", null, v);
        Log.d(TAG, "insert result id=" + id);
        return id;
    }

    public int update(Deliverer d) {
        Log.d(TAG, "update id=" + d.getId() + " name=" + d.getName());
        ContentValues v = new ContentValues();
        v.put("name", d.getName());
        v.put("address", d.getAddress());
        v.put("phone", d.getPhone());

        int count = db.update("deliverers", v, "id = ?", new String[]{String.valueOf(d.getId())});
        Log.d(TAG, "update affected rows=" + count);
        return count;
    }

    public int delete(int id) {
        Log.d(TAG, "delete id=" + id);
        int count = db.delete("deliverers", "id = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "delete affected rows=" + count);
        return count;
    }

    public Deliverer getById(int id) {
        Log.d(TAG, "getById id=" + id);
        try (Cursor c = db.rawQuery("SELECT * FROM deliverers WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) {
                Log.d(TAG, "getById not found");
                return null;
            }

            Deliverer d = new Deliverer(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("phone"))
            );

            Log.d(TAG, "getById found: " + d.getName());
            return d;
        }
    }

    public List<Deliverer> getAll() {
        Log.d(TAG, "getAll");
        List<Deliverer> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM deliverers ORDER BY id", null)) {
            while (c.moveToNext()) {
                Deliverer d = new Deliverer(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getString(c.getColumnIndexOrThrow("address")),
                        c.getString(c.getColumnIndexOrThrow("phone"))
                );
                list.add(d);
            }
        }

        Log.d(TAG, "getAll count=" + list.size());
        return list;
    }
}
