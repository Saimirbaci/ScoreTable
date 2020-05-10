package com.saimirbaci.PesKatshTabel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by saimirbaci on 27/04/15.
 */
public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ResultsDatabase.GameEntry.TABLE_NAME + " (" +
                    ResultsDatabase.GameEntry._ID + " INTEGER PRIMARY KEY," +
                    ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_1 + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_2 + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_3 + TEXT_TYPE + COMMA_SEP +
                    ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_4 + TEXT_TYPE +

                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ResultsDatabase.Results.TABLE_NAME;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Game.db";

    public GameDatabaseHelper(Context context) {
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
