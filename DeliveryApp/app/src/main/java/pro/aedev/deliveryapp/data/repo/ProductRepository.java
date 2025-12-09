package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static final String TAG = "ProductRepository";
    private final SQLiteDatabase db;

    public ProductRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
        Log.d(TAG, "Repository initialized");
    }

    public long insert(Product p) {
        Log.d(TAG, "insert: name=" + p.getName() + " type=" + p.getType());
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("type", p.getType());
        long id = db.insert("products", null, v);
        Log.d(TAG, "insert result id=" + id);
        return id;
    }

    public int update(Product p) {
        Log.d(TAG, "update id=" + p.getId() + " name=" + p.getName() + " type=" + p.getType());
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("type", p.getType());

        int count = db.update("products", v, "id = ?", new String[]{String.valueOf(p.getId())});
        Log.d(TAG, "update affected rows=" + count);
        return count;
    }

    public int delete(int id) {
        Log.d(TAG, "delete id=" + id);
        int count = db.delete("products", "id = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "delete affected rows=" + count);
        return count;
    }

    public Product getById(int id) {
        Log.d(TAG, "getById id=" + id);
        try (Cursor c = db.rawQuery("SELECT * FROM products WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) {
                Log.d(TAG, "getById not found");
                return null;
            }

            Product p = new Product(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("type"))
            );

            Log.d(TAG, "getById found: " + p.getName());
            return p;
        }
    }

    public List<Product> getAll() {
        Log.d(TAG, "getAll");
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

        Log.d(TAG, "getAll count=" + list.size());
        return list;
    }
}
