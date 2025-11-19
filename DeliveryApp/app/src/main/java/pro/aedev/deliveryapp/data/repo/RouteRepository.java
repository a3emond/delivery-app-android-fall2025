package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteRepository {

    private final SQLiteDatabase db;

    public RouteRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();

    }

    public long insert(Route r) {
        ContentValues v = new ContentValues();
        v.put("label", r.getLabel());
        v.put("deliverer_id", r.getDelivererId());
        return db.insert("routes", null, v);
    }

    public int update(Route r) {
        ContentValues v = new ContentValues();
        v.put("label", r.getLabel());
        v.put("deliverer_id", r.getDelivererId());

        return db.update("routes", v, "id = ?", new String[]{String.valueOf(r.getId())});
    }

    public int delete(int id) {
        return db.delete("routes", "id = ?", new String[]{String.valueOf(id)});
    }

    public Route getById(int id) {
        try (Cursor c = db.rawQuery("SELECT * FROM routes WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) return null;

            return new Route(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("label")),
                    c.isNull(c.getColumnIndexOrThrow("deliverer_id")) ? null :
                            c.getInt(c.getColumnIndexOrThrow("deliverer_id"))
            );
        }
    }

    public List<Route> getAll() {
        List<Route> list = new ArrayList<>();

        try (Cursor c = db.rawQuery("SELECT * FROM routes ORDER BY id", null)) {
            while (c.moveToNext()) {
                Route r = new Route(
                        c.getInt(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("label")),
                        c.isNull(c.getColumnIndexOrThrow("deliverer_id")) ? null :
                                c.getInt(c.getColumnIndexOrThrow("deliverer_id"))
                );
                list.add(r);
            }
        }

        return list;
    }
}
