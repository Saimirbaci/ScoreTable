/*
* Created By Saimir Baci
* */
package com.saimirbaci.PesKatshTabel;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main extends FragmentActivity
        implements
            StartFragment.OnNewGameClicked,
            StartFragment.OnOpenLastClicked,
            SaveToDatabaseFragment.OnSaveButtonClicked,
            SaveToDatabaseFragment.OnLoadGameNames,
            OpenFromDatabaseFragment.OnOpenButtonClicked
    {
    String fileName = "lastMatch.res";
    int numberOfPlayers = 0;
    String playerNames[] = new String [4];
    int totalNumberOfTurns = 10;
    int maxNumberOfPlayers = 4;

    MainGameData gameData;

    public static String UserName = "";
    public static boolean AutomaticCalculations = false;
    public static boolean SevenRound = false;
    public static int TotalSpadesHands = 32;
    public static int TotalSevenHands = 60;
    List<GameFileName> listGameNames = new ArrayList<GameFileName>();
    public static GameDatabaseHelper gameDbHelper ;
    public static ResultsDatabaseHelper resultDbHelper;
    private PlaceholderFragment placeholderFragment = null;
    private SaveToDatabaseFragment saveToDatabaseFragment = null;
    public int currentFragmentId = 0;
    private boolean openResultsFromInitFragment;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this;
        openResultsFromInitFragment = false;
        gameDbHelper = new GameDatabaseHelper(context);
        resultDbHelper = new ResultsDatabaseHelper(context);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
       if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            //Fragment firstFragment = new PlaceholderFragment();
            Fragment firstFragment = new StartFragment();
            //Fragment saveToDatabse = new SaveToDatabaseFragment();
            currentFragmentId = firstFragment.getId();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment, "StartFragment").commit();

        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        UserName = sharedPref.getString(SettingsActivity.KEY_PREF_USER_NAME, "");
        AutomaticCalculations = sharedPref.getBoolean(SettingsActivity.KEY_PREF_AUTOMATIC_CALCULATIONS, false);
        SevenRound = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SEVEN_ROUND, false);
    }

    @Override
    public void onBackPressed() {
        try{
            if (placeholderFragment != null){
                 if(placeholderFragment.isGameStarted()){
                     openSave();
                 }
                 placeholderFragment.setGameStarted(false);
            }
        }
        catch (NullPointerException e){
            Toast.makeText(this,getString(R.string.PlaceholderFragment_NotInitialised),Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_reset:{
                openReset();
                return true;
            }
            case R.id.action_settings:{
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_OK);
                return true;
            }
            case R.id.action_save: {
                openSave();
                return true;
            }
            case R.id.action_save_to_database: {
                openSaveToDatabase();
                return true;
            }
            case R.id.action_open_from_database: {
                openFromDatabase();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadFromDatabase(DatabaseFragment obj){
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

    public void openSaveToDatabase(){
        try {
            openSave();
            loadSaveFragment();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void openFromDatabase(){
        try {
            loadOpenFragment();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void openSave(){
        SaveToFile();
    }

    void initRecords(){
        int numberOfTurns = Integer.parseInt(getString(R.string.Total_Number_Of_Turns));
        if(numberOfPlayers == 0){
            numberOfPlayers = 4;
        }
        gameData = new MainGameData(playerNames, numberOfTurns, numberOfPlayers);

        if(placeholderFragment != null)
            placeholderFragment.setGameData(gameData);
    }
    //Implements the reset function which sets all the score to zero
    public void openReset(){
        Iterator it = PlaceholderFragment.EditTextViewId.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Integer a = (Integer) pairs.getKey();
            EditText editText = (EditText)PlaceholderFragment.rootView.findViewById(a);
            editText.setText("");
        }
        it = PlaceholderFragment.playerViews.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Integer a = (Integer) pairs.getValue();
            TextView tv = (TextView)PlaceholderFragment.rootView.findViewById(a);
            tv.setText(tv.getText().toString().split(" ")[0]);
        }
        initRecords();
    }

    int ValidateFileInfo(String[] lines){
        String[] playerNames = lines[0].split(",");
        if (playerNames.length == 0)
            return Constants.FILE_HAS_NO_PLAYER_NAMES;
        if (playerNames.length == 1)
            if((playerNames[0].compareTo("")) == 0)
                return Constants.FILE_HAS_EMPTY_PLAYER_NAMES;

        return Constants.FILE_HAS_CORRECT_FORMAT;
    }

    int OpenFile(){
        int playerIndex ;
        File filesDir = this.getFilesDir();
        String[] lines = FileIO.openFile(filesDir,fileName);
        if (lines == null)
            return Constants.FILE_IS_EMPTY;
        int valRes = ValidateFileInfo(lines);
        if(valRes != Constants.FILE_HAS_CORRECT_FORMAT)
            return valRes;
        String[] tmpStrings = lines[0].split(",");
        numberOfPlayers = tmpStrings.length;

        System.arraycopy(tmpStrings,0,playerNames, 0, tmpStrings.length);

        gameData = new MainGameData(totalNumberOfTurns,numberOfPlayers);
        gameData.setPlayerNames(playerNames);
        gameData.setNumberOfPlayers(numberOfPlayers);

        int numberOfTurns = Integer.parseInt(getString(R.string.Total_Number_Of_Turns));
        for (int i=0; i<numberOfTurns;i++)
            for(int j=0; j<numberOfPlayers; j++)
                gameData.setData(i,j, new Records());

        for (int i=1; i<lines.length;i++) {
            playerIndex = 0;
            String[] data = lines[i].split(",");
            for(String tempData : data){
                try {
                    gameData.setData(i-1,playerIndex, Integer.parseInt(tempData));
                }
                catch(NumberFormatException e){
                    gameData.setData(i - 1, playerIndex, 0);
                }
                playerIndex++;
            }
        }
        return 0;
    }

    void SaveToFile(){

        String dataToSave = "";

        for(int index=0; index<numberOfPlayers; index++){
            dataToSave += playerNames[index];
            if(index+1 < maxNumberOfPlayers)
                dataToSave += ",";
        }
        dataToSave +="\n";

        int numberOfTurns = Integer.parseInt(getString(R.string.Total_Number_Of_Turns));
        for (int rowIndex=0; rowIndex < numberOfTurns; rowIndex++){
            for(int colIndex=0; colIndex<numberOfPlayers; colIndex++ ) {
                dataToSave += gameData.getData(rowIndex,colIndex).getValue();
                if(colIndex+1 < maxNumberOfPlayers)
                    dataToSave += ",";
            }
            dataToSave +="\n";
        }
        if (FileIO.saveToFile(this.getFilesDir(),fileName,dataToSave) != Constants.FILE_STATUS_OK){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage(getString(R.string.Operation_Not_Possible));
            dlgAlert.setTitle(getString(R.string.app_name));
            dlgAlert.setPositiveButton(R.string.Positive_Button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

    }
    //Interface Implementations
    // Events called from the fragments
    @Override
    public void loadGameNames(DatabaseFragment obj){
        loadFromDatabase(obj);
    }

    private void AlertMsg(String msg){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(getString(R.string.app_name));
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    @Override
    public void onSaveToDatabaseClicked(){
        //Get the data from the fragment PlaceholderFragment
        {
            String gameName = saveToDatabaseFragment.getFileNameEditText().getText().toString();

            for(GameFileName fileName : listGameNames){
                if (fileName.getName().compareTo(gameName) == 0)
                {
                    AlertMsg("Rezultati me emrin " + gameName + " ekziston. Zgjidh emer tjeter per ta ruajtur rezultatin");
                    return;
                }
            }
            ContentValues values = new ContentValues();
            values.put(ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME, saveToDatabaseFragment.getFileNameEditText().getText().toString());
            values.put(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_1, gameData.getPlayerNames()[0]);
            values.put(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_2, gameData.getPlayerNames()[1]);
            values.put(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_3, gameData.getPlayerNames()[2]);
            values.put(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_4, gameData.getPlayerNames()[3]);
            SQLiteDatabase db ;
            try {
                long newRowId;
                db = gameDbHelper.getWritableDatabase();
                newRowId = db.insert(
                        ResultsDatabase.GameEntry.TABLE_NAME,
                        ResultsDatabase.GameEntry.COLUMN_NAME_NULLABLE,
                        values);
                if(newRowId == 0){
                    Toast.makeText(this, getString(R.string.Operation_Succeeded),Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        String[] resultsDatabaseColumnNames = {
                ResultsDatabase.Results.COLUMN_NAME_PLAYER_1_NAME,
                ResultsDatabase.Results.COLUMN_NAME_PLAYER_2_NAME,
                ResultsDatabase.Results.COLUMN_NAME_PLAYER_3_NAME,
                ResultsDatabase.Results.COLUMN_NAME_PLAYER_4_NAME
        };
        try {
            long newRowId;
            SQLiteDatabase db = Main.resultDbHelper.getWritableDatabase();
            for (int rowIndex = 0; rowIndex < 10; rowIndex++) {
                ContentValues values = new ContentValues();
                values.put(ResultsDatabase.Results.GAME_ID, saveToDatabaseFragment.getFileNameEditText().getText().toString());
                values.put(ResultsDatabase.Results.COLUMN_NAME_TURN, rowIndex);
                for (int colIndex = 0; colIndex < gameData.getNumberOfPlayers(); colIndex++) {
                    values.put(resultsDatabaseColumnNames[colIndex], gameData.getData(rowIndex, colIndex).getValue());
                }
                newRowId = db.insert(
                        ResultsDatabase.Results.TABLE_NAME,
                        ResultsDatabase.Results.COLUMN_NAME_NULLABLE,
                        values);
                if(newRowId == -1){
                    Toast.makeText(this, getString(R.string.Operation_Failed),Toast.LENGTH_SHORT).show();
                }

            }
            //db.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onCancelSaveToDatabaseClicked(){
        getSupportFragmentManager().popBackStackImmediate();
    }

    public List<String> getPlayerNames(String game_id){
        SQLiteDatabase db;
        try {
            db = gameDbHelper.getReadableDatabase();
        }
        catch(Exception e){
            System.out.println("Can't get database. Empty list");
            return null;
        }

        String sortOrder =
                ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME + " DESC";
        //String[] selectionArgs = {"*"};
        Cursor cursor = null;
        String[] projection = {
                ResultsDatabase.GameEntry._ID,
                ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME,
                ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_1,
                ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_2,
                ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_3,
                ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_4
        };
        try {
            cursor = db.query(
                    ResultsDatabase.GameEntry.TABLE_NAME,        // The table to query
                    projection,                                         // The columns to return
                    ResultsDatabase.GameEntry.COLUMN_NAME_GAME_NAME + "=?",  // The columns for the WHERE clause
                    new String[] { game_id },  // The values for the WHERE clause
                    null,                                               // don't group the rows
                    null,                                               // don't filter by row groups
                    sortOrder                                           // The sort order
            );
            cursor.moveToFirst();
            List<String> players = new ArrayList<String>();

            String p1 = cursor.getString(cursor.getColumnIndexOrThrow(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_1));
            if(p1 != null)
                players.add(p1);
            String p2 = cursor.getString(cursor.getColumnIndexOrThrow(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_2));

            if(p2 != null)
                players.add(p2);

            String p3 = cursor.getString(cursor.getColumnIndexOrThrow(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_3));

            if(p3 != null)
                players.add(p3);
            String p4 = cursor.getString(cursor.getColumnIndexOrThrow(ResultsDatabase.GameEntry.COLUMN_NAME_PLAYER_4));

            if(p4 != null)
                players.add(p4);

            String[] tmpPlayers = players.toArray(new String[players.size()]);
            gameData = new MainGameData(tmpPlayers, Integer.parseInt(getString(R.string.Total_Number_Of_Turns)), players.size());

            cursor.close();
            return players;
        }
        catch(Exception e){
            System.out.println("Exception");
            if(cursor != null)
                cursor.close();
            return null;
        }
    }

    @Override
    public void onOpenFromDatabaseClicked(String name){
        List<String> playerNames = getPlayerNames(name);
        int numberOfPlayers = playerNames.size();
        SQLiteDatabase db;
        try {
            db = resultDbHelper.getReadableDatabase();
        }
        catch(Exception e){
            System.out.println("Can't get database. Empty list");
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }

        String sortOrder =
                ResultsDatabase.Results.GAME_ID + " DESC";
        String[] projection = {
            ResultsDatabase.Results._ID,
            ResultsDatabase.Results.GAME_ID,
            ResultsDatabase.Results.COLUMN_NAME_PLAYER_1_NAME,
            ResultsDatabase.Results.COLUMN_NAME_PLAYER_2_NAME,
            ResultsDatabase.Results.COLUMN_NAME_PLAYER_3_NAME,
            ResultsDatabase.Results.COLUMN_NAME_PLAYER_4_NAME
    };
        Cursor cursor ;

        try {
            cursor = db.query(
                    ResultsDatabase.Results.TABLE_NAME,               // The table to query
                    projection,                                               // The columns to return
                    ResultsDatabase.Results.GAME_ID + "=?",      // The columns for the WHERE clause
                    new String[] { name },                                             // The values for the WHERE clause
                    null,                                               // don't group the rows
                    null,                                              // don't filter by row groups
                    sortOrder                                           // The sort order
            );
            cursor.moveToFirst();
            for (int i = 0; i<10;i++){
                for(int j=0; j<numberOfPlayers;j++){
                    String player = "Player_" + Integer.valueOf(j+1).toString();
                    try {
                        int p1 = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(player)));
                        gameData.setData(i,j,p1);
                    }
                    catch(NumberFormatException e){
                        Toast.makeText(this, getString(R.string.Database_Reading_Error),Toast.LENGTH_SHORT).show();
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e){
            System.out.println("Exception");
            getSupportFragmentManager().popBackStackImmediate();
        }
        if(placeholderFragment != null){
            placeholderFragment.setGameData(gameData);
        }
        getSupportFragmentManager().popBackStackImmediate();
        if(openResultsFromInitFragment){
            activateResultsFragment();
            openResultsFromInitFragment = false;
        }

    }

    @Override
    public void onCancelFromDatabaseClicked(){
        getSupportFragmentManager().popBackStackImmediate();
    }

    private void loadOpenFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment newFragment = fm.findFragmentByTag("OpenFromDatabase");
        if (newFragment == null) {
            newFragment = new OpenFromDatabaseFragment();
        }

        Fragment oldFragment = fm.findFragmentByTag("ResultsFrag");
        if (oldFragment != null) {
            ft.replace(R.id.fragment_container, newFragment, "OpenFromDatabase");
        } else {
            oldFragment = fm.findFragmentByTag("StartFragment");
            if(oldFragment != null){
                ft.replace(R.id.fragment_container, newFragment, "OpenFromDatabase");
                openResultsFromInitFragment = true;
            }
            else{
                ft.add(R.id.fragment_container, newFragment, "OpenFromDatabase");
            }
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void loadSaveFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment newFragment = fm.findFragmentByTag("SaveToDatabase");
        if (newFragment == null) {
            newFragment = new SaveToDatabaseFragment();
        }
        saveToDatabaseFragment = (SaveToDatabaseFragment) newFragment;
        Fragment oldFragment = fm.findFragmentByTag("ResultsFrag");
        if (oldFragment != null) {
            ft.replace(R.id.fragment_container, newFragment, "SaveToDatabase");
        } else {
            oldFragment = fm.findFragmentByTag("StartFragment");
            if(oldFragment != null){
                ft.replace(R.id.fragment_container, newFragment, "SaveToDatabase");
            }
            else{
                ft.add(R.id.fragment_container, newFragment, "SaveToDatabase");
            }
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onNewGame(List<String> playerNames ){
        numberOfPlayers = playerNames.size();
        playerNames.toArray(this.playerNames);
        initRecords();
        activateResultsFragment();
    }

    @Override
    public void onOpenLast(){
        int status = OpenFile();

        if (status != Constants.FILE_HAS_CORRECT_FORMAT){
            Toast.makeText(this,R.string.No_Game_Stored,Toast.LENGTH_LONG).show();
            return;
        }
        activateResultsFragment();
    }

    void activateResultsFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment newFragment = fm.findFragmentByTag("ResultsFrag");
        if (newFragment == null) {
            newFragment = new PlaceholderFragment();
        }

        ((PlaceholderFragment)newFragment).setGameData(gameData);
        placeholderFragment = ((PlaceholderFragment)newFragment);
        Fragment oldFragment = fm.findFragmentByTag("StartFragment");

        if (oldFragment != null) {
            ft.replace(R.id.fragment_container, newFragment, "ResultsFrag");
        } else {
            ft.add(R.id.fragment_container, newFragment, "ResultsFrag");
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
