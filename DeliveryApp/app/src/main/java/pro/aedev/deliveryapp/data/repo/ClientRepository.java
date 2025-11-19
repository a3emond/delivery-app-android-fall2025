package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    private final SQLiteDatabase db;

    public ClientRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();

    }

    public long insert(Client cObj) {
        ContentValues v = new ContentValues();
        v.put("name", cObj.getName());
        v.put("address", cObj.getAddress());
        v.put("phone", cObj.getPhone());
        return db.insert("clients", null, v);
    }

    public int update(Client cObj) {
        ContentValues v = new ContentValues();
        v.put("name", cObj.getName());
        v.put("address", cObj.getAddress());
        v.put("phone", cObj.getPhone());

        return db.update("clients", v, "id = ?", new String[]{String.valueOf(cObj.getId())});
    }

    public int delete(int id) {
        return db.delete("clients", "id = ?", new String[]{String.valueOf(id)});
    }

    public Client getById(int id) {
        try (Cursor c = db.rawQuery("SELECT * FROM clients WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;

            return new Client(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("address")),
                    c.getString(c.getColumnIndexOrThrow("phone"))
            );
        }
    }

    public List<Client> getAll() {
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

        return list;
    }
}
