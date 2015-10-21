package com.lift.u.ulift.DBObjects;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by balavigneshr on 8/22/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int database_version = 1;

    public DatabaseHelper(Context context) {
        super(context, Tables.database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(DBConstants.create_excercises);
        sdb.execSQL(DBConstants.create_routines);
        sdb.execSQL(DBConstants.create_userWorkout);
        sdb.execSQL(DBConstants.create_userSet);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}