package com.barrrettt.androidjwtrestapiexample.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.barrrettt.androidjwtrestapiexample.data.user.User;

/**
 * Created by javier fernandez barreiro on 08/12/2015.
 */
public class DataBase extends SQLiteOpenHelper{

    public final static String NOMBRE_DB = "jwt_res_api_example";
    public final static int VERSION_DB = 1;

    public DataBase (Context context){
        super(context,NOMBRE_DB,null,VERSION_DB);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_APP);
        db.execSQL("INSERT INTO app (username,jwt) VALUES ('','')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //SQL
    public final static String TABLA_APP =
            "CREATE TABLE app (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "jwt TEXT NOT NULL" +
                    ")";


    //OPERACIONES CON LA DB
    public static void resetDB(Context context){
        SQLiteDatabase db = new DataBase(context).getWritableDatabase();
        db.execSQL("drop table IF EXISTS app");
        db.execSQL(DataBase.TABLA_APP);
        db.execSQL("INSERT INTO app (username,jwt) VALUES ('null','null')");
        db.close();
    }

    public static User getUserJWT (Context context){
        SQLiteDatabase db = new DataBase(context).getReadableDatabase();
        Cursor cursor = db.query(
                "app",   // The table to query
                null,             // The array of columns to return (pass null to get all)
                "id = ?",              // The columns for the WHERE clause
                new String[]{"1"},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        User user = null;
        while(cursor.moveToNext()) {
            user = new User(
              cursor.getString(cursor.getColumnIndexOrThrow("username")),
              cursor.getString(cursor.getColumnIndexOrThrow("jwt")));
        }

        cursor.close();
        db.close();

        return user;
    }

    public static void saveJWT(Context context, User user){
        SQLiteDatabase db = new DataBase(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username",user.getName());
        cv.put("jwt",user.getJwt());
        db.update("app", cv, "id=1", null);

        db.close();
    }
}
