package com.saimirbaci.PesKatshTabel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class GameDataLoader {
    private final GameDatabaseHelper gameDbHelper;
    private final List<GameFileName> listGameNames;

    public GameDataLoader(GameDatabaseHelper gameDbHelper, List<GameFileName> listGameNames) {
        this.gameDbHelper = gameDbHelper;
        this.listGameNames = listGameNames;
    }

    void loadFromDatabase(DatabaseFragment obj){
        SQLiteDatabase db;
        try {
            db = gameDbHelper.getReadableDatabase();
        }
        catch(Exception e){
            System.out.println("Can't get database. Empty list");
            return;
        }

        String sortOrder =
                ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME + " DESC";
        Cursor cursor ;

        try {
            cursor = db.query(
                    ResultsDatabase.GameEntry.TABLE_NAME,        // The table to query
                    null,                                         // The columns to return
                    null,    // The columns for the WHERE clause
                    null,   // The values for the WHERE clause
                    null,                                               // don't group the rows
                    null,                                               // don't filter by row groups
                    sortOrder                                           // The sort order
            );
            cursor.moveToFirst();
            listGameNames.clear();
            if(cursor.getCount() > 0){
                while(true) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME));
                    GameFileName gameName = new GameFileName(name);
                    listGameNames.add(gameName);
                    if(cursor.isLast()){
                        break;
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        catch(Exception e){
            System.out.println("Exception");
        }
        obj.setListGameNames(listGameNames);
    }
}
