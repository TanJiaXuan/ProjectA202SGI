package com.example.jooxuan.shoplist;
/**
 * Created by JooXuan on 12/1/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION =1;

    private static final String DATABASE_NAME = "shopsDB",
            TABLE_SHOPS = "shops",
            KEY_ID = "id",
            KEY_NAME = "name",
            KEY_QUANTITY = "quantity",
            KEY_REMARK= "remark",
            KEY_IMAGE = "img";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_SHOPS + "" +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT," + KEY_QUANTITY + " TEXT," + KEY_REMARK + " TEXT,"
                + KEY_IMAGE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);

        onCreate(db);
    }

    public void createShop(Shop shop) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, shop.getName());
        values.put(KEY_QUANTITY, shop.getQuantity());
        values.put(KEY_REMARK, shop.getRemark());
        values.put(KEY_IMAGE, shop.getImg().toString());
        db.insert(TABLE_SHOPS, null, values);
        db.close();
    }


    public void deleteShop(Shop shop) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SHOPS, KEY_ID + "=?", new String[] { String.valueOf(shop.getId()) });
        db.close();
    }

    public int getShopsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHOPS, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public List<Shop> getAllShops() {
        List<Shop> shops = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHOPS, null);

        if (cursor.moveToFirst()) {
            do {
                shops.add(new Shop(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Uri.parse(cursor.getString(4))));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shops;
    }
}

