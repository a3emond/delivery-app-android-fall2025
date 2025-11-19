package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private final SQLiteDatabase db;

    public ProductRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();

    }

    public long insert(Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("type", p.getType());
        return db.insert("products", null, v);
    }

    public int update(Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("type", p.getType());

        return db.update("products", v, "id = ?", new String[]{String.valueOf(p.getId())});
    }

    public int delete(int id) {
        return db.delete("products", "id = ?", new String[]{String.valueOf(id)});
    }

    public Product getById(int id) {
        try (Cursor c = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;

            return new Product(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("type"))
            );
        }
    }

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM products ORDER BY id", null)) {
            while (c.moveToNext()) {
                Product p = new Product(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getString(c.getColumnIndexOrThrow("type"))
                );
                list.add(p);
            }
        }

        return list;
    }
}
