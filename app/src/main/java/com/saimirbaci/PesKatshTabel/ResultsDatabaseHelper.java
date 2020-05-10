/**
 * Made by saimirbaci on 27/04/15.
 */

package com.saimirbaci.PesKatshTabel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResultsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ResultsDatabase.Results.TABLE_NAME + " (" +
                    ResultsDatabase.Results._ID + " INTEGER PRIMARY KEY," +
                    ResultsDatabase.Results.GAME_ID + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.Results.COLUMN_NAME_TURN + INTEGER_TYPE + COMMA_SEP +
                    ResultsDatabase.Results.COLUMN_NAME_PLAYER_1_NAME + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.Results.COLUMN_NAME_PLAYER_2_NAME + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.Results.COLUMN_NAME_PLAYER_3_NAME + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.Results.COLUMN_NAME_PLAYER_4_NAME + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ResultsDatabase.Results.TABLE_NAME;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Results.db";

    public ResultsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}