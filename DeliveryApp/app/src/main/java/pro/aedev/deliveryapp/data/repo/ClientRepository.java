package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing Client entities in the database.
 */
public class ClientRepository {

    private static final String TAG = "ClientRepository";
    private final SQLiteDatabase db;

    public ClientRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
        Log.d(TAG, "Repository initialized");
    }

    public long insert(Client cObj) {
        Log.d(TAG, "insert: name=" + cObj.getName());
        ContentValues v = new ContentValues();
        v.put("name", cObj.getName());
        v.put("address", cObj.getAddress());
        v.put("phone", cObj.getPhone());
        long id = db.insert("clients", null, v);
        Log.d(TAG, "insert result id=" + id);
        return id;
    }

    public int update(Client cObj) {
        Log.d(TAG, "update id=" + cObj.getId() + " name=" + cObj.getName());
        ContentValues v = new ContentValues();
        v.put("name", cObj.getName());
        v.put("address", cObj.getAddress());
        v.put("phone", cObj.getPhone());

        int count = db.update("clients", v, "id = ?", new String[]{String.valueOf(cObj.getId())});
        Log.d(TAG, "update affected rows=" + count);
        return count;
    }

    public int delete(int id) {
        Log.d(TAG, "delete id=" + id);
        int count = db.delete("clients", "id = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "delete affected rows=" + count);
        return count;
    }

    public Client getById(int id) {
        Log.d(TAG, "getById id=" + id);
        try (Cursor c = db.rawQuery("SELECT * FROM clients WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) {
                Log.d(TAG, "getById not found");
                return null;
            }

            Client cli = new Client(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("phone"))
            );

            Log.d(TAG, "getById found: " + cli.getName());
            return cli;
        }
    }

    public List<Client> getAll() {
        Log.d(TAG, "getAll");
        List<Client> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM clients ORDER BY id", null)) {
            while (c.moveToNext()) {
                Client cli = new Client(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getString(c.getColumnIndexOrThrow("address")),
                        c.getString(c.getColumnIndexOrThrow("phone"))
                );
                list.add(cli);
            }
        }

        Log.d(TAG, "getAll count=" + list.size());
        return list;
    }
}
