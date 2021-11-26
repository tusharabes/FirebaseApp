package com.example.firebaseapp.SqliteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    

    private static final String DATABASE_NAME="Users";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME="user_data";

    public DbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       String query="CREATE TABLE "+"USER_DATA "+"(id integer PRIMARY KEY , name TEXT , email TEXT , profile_id BLOB)";
        sqLiteDatabase.execSQL(query);
        Log.d("Key","Table created");
    }



    public void updateImage()
    {

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void deleteTable(SQLiteDatabase db)
    {
        String query ="DROP TABLE  IF EXISTS USER_DATA";
        db.execSQL(query);

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
