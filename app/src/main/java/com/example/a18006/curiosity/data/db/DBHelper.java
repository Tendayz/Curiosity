package com.example.a18006.curiosity.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table images ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "img text" + ");");
    }

    public String[] getImages() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from images where img != 'deleted' order by name", new String[]{});
        String[] images = new String[c.getCount()];

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int imgColIndex = c.getColumnIndex("img");

            int i = 0;
            do {
                if (!c.getString(imgColIndex).equals("deleted")) {
                    images[i] = c.getString(imgColIndex);
                    i++;
                }
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", img = " + c.getString(imgColIndex));

            } while (c.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
            return null;
        }
        c.close();

        return images;
    }

    public long put(String name, String img) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        Cursor mCursor = db.rawQuery("select * from images where name=?", new String[]{name});

        if (mCursor.getCount() == 0)
        {
            cv.put("name", name);
            cv.put("img", img);

            long rowID = db.insert("images", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);

            return rowID;
        }
        else
        {
            return 0;
        }
    }

    public long delete(String url) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put("img", "deleted");

        long rowID = db.update("images", cv, "img="+"=\""+url+"\"", null);
        Log.d(LOG_TAG, "image deleted, ID = " + rowID);
        return rowID;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
