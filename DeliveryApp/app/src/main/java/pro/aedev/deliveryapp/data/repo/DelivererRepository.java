package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Deliverer;

import java.util.ArrayList;
import java.util.List;

public class DelivererRepository {

    private final SQLiteDatabase db;

    public DelivererRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();

    }

    public long insert(Deliverer d) {
        ContentValues v = new ContentValues();
        v.put("name", d.getName());
        v.put("address", d.getAddress());
        v.put("phone", d.getPhone());
        return db.insert("deliverers", null, v);
    }

    public int update(Deliverer d) {
        ContentValues v = new ContentValues();
        v.put("name", d.getName());
        v.put("address", d.getAddress());
        v.put("phone", d.getPhone());

        return db.update("deliverers", v, "id = ?", new String[]{String.valueOf(d.getId())});
    }

    public int delete(int id) {
        return db.delete("deliverers", "id = ?", new String[]{String.valueOf(id)});
    }

    public Deliverer getById(int id) {
        try (Cursor c = db.rawQuery("SELECT * FROM deliverers WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;

            return new Deliverer(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("phone"))
            );
        }
    }

    public List<Deliverer> getAll() {
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

        return list;
    }
}
