/*
* Created By Saimir Baci
* */
package com.saimirbaci.PesKatshTabel;

import android.provider.BaseColumns;

public final class ResultsDatabase {

    public ResultsDatabase() {}

    /* Inner class that defines the table contents */
    public static abstract class Results implements BaseColumns {
        public static final String TABLE_NAME = "Results";
        public static final String _ID = "ResultId";
        public static final String GAME_ID = "Game_Id";
        public static final String COLUMN_NAME_TURN = "Turn";
        public static final String COLUMN_NAME_PLAYER_1_NAME = "Player_1";
        public static final String COLUMN_NAME_PLAYER_2_NAME = "Player_2";
        public static final String COLUMN_NAME_PLAYER_3_NAME = "Player_3";
        public static final String COLUMN_NAME_PLAYER_4_NAME = "Player_4";
        public static final String COLUMN_NAME_NULLABLE = null;
    }

    public static abstract class GameEntry implements BaseColumns {
        public static final String TABLE_NAME = "Games";
        public static final String _ID = "Game_Id";
        public static final String COLUMN_NAME_GAME_NAME = "GameName";
        public static final String COLUMN_NAME_PLAYER_1 = "Player_1";
        public static final String COLUMN_NAME_PLAYER_2 = "Player_2";
        public static final String COLUMN_NAME_PLAYER_3 = "Player_3";
        public static final String COLUMN_NAME_PLAYER_4 = "Player_4";
        public static final String COLUMN_NAME_NULLABLE = null;
    }
}
