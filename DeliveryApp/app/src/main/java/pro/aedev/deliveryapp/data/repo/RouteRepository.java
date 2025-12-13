package pro.aedev.deliveryapp.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pro.aedev.deliveryapp.data.db.AppDatabase;
import pro.aedev.deliveryapp.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing Route entities in the database.
 */
public class RouteRepository {

    private static final String TAG = "RouteRepository";

    private final SQLiteDatabase db;

    public RouteRepository(Context ctx) {
        db = AppDatabase.getInstance(ctx).getDb();
        Log.d(TAG, "Repository initialized");
    }

    public long insert(Route r) {
        Log.d(TAG, "insert: " + r.getLabel() + " deliverer=" + r.getDelivererId());
        ContentValues v = new ContentValues();
        v.put("label", r.getLabel());
        v.put("deliverer_id", r.getDelivererId());
        long id = db.insert("routes", null, v);
        Log.d(TAG, "insert result id=" + id);
        return id;
    }

    public int update(Route r) {
        Log.d(TAG, "update id=" + r.getId() + " label=" + r.getLabel() + " deliverer=" + r.getDelivererId());
        ContentValues v = new ContentValues();
        v.put("label", r.getLabel());
        v.put("deliverer_id", r.getDelivererId());

        int count = db.update("routes", v, "id = ?", new String[]{String.valueOf(r.getId())});
        Log.d(TAG, "update affected rows=" + count);
        return count;
    }

    public int delete(int id) {
        Log.d(TAG, "delete id=" + id);
        int count = db.delete("routes", "id = ?", new String[]{String.valueOf(id)});
        Log.d(TAG, "delete affected rows=" + count);
        return count;
    }

    public Route getById(int id) {
        Log.d(TAG, "getById id=" + id);
        try (Cursor c = db.rawQuery("SELECT * FROM routes WHERE id = ?", new String[]{String.valueOf(id)})) {
            if (!c.moveToFirst()) {
                Log.d(TAG, "getById not found");
                return null;
            }

            Route r = new Route(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("label")),
                    c.isNull(c.getColumnIndexOrThrow("deliverer_id")) ? null :
                            c.getInt(c.getColumnIndexOrThrow("deliverer_id"))
            );

            Log.d(TAG, "getById found: " + r.getLabel());
            return r;
        }
    }

    public List<Route> getAll() {
        Log.d(TAG, "getAll");
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

        Log.d(TAG, "getAll count=" + list.size());
        return list;
    }
}
