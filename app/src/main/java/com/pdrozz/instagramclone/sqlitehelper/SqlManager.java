package com.pdrozz.instagramclone.sqlitehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlManager extends SQLiteOpenHelper {

    public final static String DB_NAME="instaSQL";
    public final static int DB_VERSION=1;

    public SqlManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            String tableSeguindo="CREATE TABLE IF NOT EXISTS seguindo (id VARCHAR NOT NULL);";

            db.execSQL(tableSeguindo);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
