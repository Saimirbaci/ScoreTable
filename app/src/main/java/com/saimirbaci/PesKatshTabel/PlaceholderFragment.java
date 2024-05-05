/*
* Created By Saimir Baci
*
* */

package com.saimirbaci.PesKatshTabel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    //ToDo Add the handling for the screen rotation. Right now it goes back to main screen

    static boolean automaticValueSet = false;
    static int editTextMaxLength = 3;
    static Map<Integer, Coordinates> EditTextViewId = new HashMap<Integer, Coordinates>();
    static Map<Integer, Integer> playerViews = new HashMap<Integer, Integer>();
    static View rootView = null;
    static boolean gameStarted = false;
    private MainGameData gameData;

    public void setGameData(MainGameData gameData){
        this.gameData = gameData;
    }

    public PlaceholderFragment() {
    }

    public boolean isGameStarted(){
        return gameStarted;
    }

    public void setGameStarted(boolean value){
        gameStarted = value;
    }

    TableRow CreateTextViewPlayerNames(){
        TableRow rowPlayerNames = new TableRow(rootView.getContext());
        for (int i=0; i < gameData.getNumberOfPlayers(); i++){
            TextView playerName = new TextView(rootView.getContext());
            playerName.setText(gameData.getPlayerNames()[i]);
            playerName.setId(View.generateViewId());
            playerName.setTextSize(24);
            playerViews.put(i,playerName.getId());
            rowPlayerNames.addView(playerName);
        }
        rowPlayerNames.setBackgroundColor(2);
        return rowPlayerNames;
    }

    void SetTotalResult(int column){
        TextView tv = (TextView)rootView.findViewById(playerViews.get(column));
        int total = 0;
        int numberOfTurns = Integer.parseInt(getString(R.string.Total_Number_Of_Turns));
        for(int l=0; l< numberOfTurns; l++){
            total += gameData.getData(l,column).getValue();
        }
        tv.setText(tv.getText().toString().split(" ")[0] + " " + total);
    }

    private void HandleTextChange(EditText editText){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        Main.UserName = sharedPref.getString(SettingsActivity.KEY_PREF_USER_NAME, "");
        Main.AutomaticCalculations = sharedPref.getBoolean(SettingsActivity.KEY_PREF_AUTOMATIC_CALCULATIONS, true);
        Main.SevenRound = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SEVEN_ROUND, true);
        Main.TotalSpadesHands = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_SPADES_ROUND_TOTAL,"-32"));
        Main.TotalSevenHands = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_SEVEN_ROUND_TOTAL,"60"));

        int data, numberOfValuesToSet = 0, indexOfValueNotSet=0, totalRound=0;
        Coordinates coordinate = EditTextViewId.get(editText.getId());
        if(gameData.getData(coordinate.getRow(),coordinate.getColumn()).isAutomaticValueSet())
        {
            gameData.getData(coordinate.getRow(),coordinate.getColumn()).setAutomaticValueSet(false);
            automaticValueSet = false;
            SetTotalResult(coordinate.getColumn());
            return;
        }
        try {
            data = Integer.parseInt(editText.getText().toString());
        }
        catch(Exception e){
            return;
        }
        //Get the edittext box which is edited
        gameData.getData(coordinate.getRow(),coordinate.getColumn()).setValue(data);
        gameData.getData(coordinate.getRow(),coordinate.getColumn()).setValue_set();
        SetTotalResult(coordinate.getColumn());

        if(Main.AutomaticCalculations){
            for (int l=0; l<gameData.getNumberOfPlayers(); l++){
                if (!(gameData.getData(coordinate.getRow(),l).isValue_set())){
                    numberOfValuesToSet++;
                    indexOfValueNotSet = l;
                    continue;
                }
                totalRound +=  gameData.getData(coordinate.getRow(),l).getValue();
            }


            if (numberOfValuesToSet == 1){
                for (Object o : EditTextViewId.entrySet()) {
                    Map.Entry pairs = (Map.Entry) o;
                    Coordinates coor = (Coordinates) pairs.getValue();
                    Integer editTextId = (Integer) pairs.getKey();
                    if ((coor.getRow() == coordinate.getRow()) && (coor.getColumn() == indexOfValueNotSet)) {
                        EditText tmpEditText = (EditText) rootView.findViewById(editTextId);
                        Integer tmpInt;
                        if (Main.SevenRound) {
                            if ((coor.getRow() % 2) == 0) {
                                if (gameData.getNumberOfPlayers() == 3)
                                    tmpInt = Main.TotalSpadesHands - totalRound; //Integer.valueOf(totalRound);
                                else
                                    tmpInt = Main.TotalSpadesHands - totalRound; //Integer.valueOf(totalRound);
                            } else {
                                if (gameData.getNumberOfPlayers() == 3)
                                    tmpInt = Main.TotalSevenHands - totalRound; //Integer.valueOf(totalRound);
                                else
                                    tmpInt = Main.TotalSevenHands - totalRound; //Integer.valueOf(totalRound);
                            }
                        } else {
                            if (gameData.getNumberOfPlayers() == 3)
                                tmpInt = Main.TotalSpadesHands - totalRound; //Integer.valueOf(totalRound);
                            else
                                tmpInt = Main.TotalSpadesHands - totalRound;//Integer.valueOf(totalRound);
                        }

                        gameData.getData(coor.getRow(),coor.getColumn()).setAutomaticValueSet(true);
                        gameData.getData(coor.getRow(),coor.getColumn()).setValue(tmpInt);
                        //editText.setText(tmpInt.toString());
                        tmpEditText.setText(tmpInt.toString());
                        automaticValueSet = true;
                        break;

                    }
                }
            }
        }
        //Setting up the total on top close to the name
        gameData.getData(coordinate.getRow(),coordinate.getColumn()).setValue_set();
    }

    @Override
    public void onResume(){
        super.onResume();
        LoadDataIntoFragment();
    }
    //Creates a dynamic view based on the numberOfPlayers the result is being kept.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        EditTextViewId.clear();
        playerViews.clear();
        //LoadDataIntoFragment();

        return rootView;
    }
    public String getRoundText(int index){
        String res = null;
        switch(index){
            case 1:{
                res= getString(R.string.Round_1);
                break;
            }
            case 2:{
                res= getString(R.string.Round_2);
                break;
            }
            case 3:{
                res= getString(R.string.Round_3);
                break;
            }
            case 4:{
                res= getString(R.string.Round_4);
                break;
            }
            case 5:{
                res= getString(R.string.Round_5);
                break;
            }
        }
        return res;
    }

    public void LoadDataIntoFragment(){
        int numberOfRounds, numberOfSubRounds;
        try {
            numberOfRounds = Integer.parseInt(getString(R.string.Number_Of_Rounds));
            numberOfSubRounds = Integer.parseInt(getString(R.string.Number_Of_Sub_Rounds));
        }
        catch (Exception e){
            numberOfRounds = 5;
            numberOfSubRounds = 2;
        }
            gameStarted = true;
        RelativeLayout layout = (RelativeLayout)rootView.findViewById(R.id.fragment_results_layout);
        //layout.getRootView();
        layout.removeAllViews();
        EditTextViewId.clear();
        TableLayout table = new TableLayout(rootView.getContext());

        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(true);
        ScrollView scrollView = new ScrollView(rootView.getContext());
        scrollView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        scrollView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT));
        table.addView(CreateTextViewPlayerNames(), new TableLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        for ( int i=0; i<numberOfRounds; i++) {
            TableRow rowFirst = new TableRow(rootView.getContext());
            TextView firstFloor = new TextView(rootView.getContext());
            firstFloor.setText(getRoundText(i + 1));
            rowFirst.addView(firstFloor);
            table.addView(rowFirst, new TableLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            for(int j=0; j<numberOfSubRounds; j++){
                TableRow resultRows = new TableRow(rootView.getContext());
                for(int k=0; k<gameData.getNumberOfPlayers(); k++){
                    final EditText editText = new EditText(rootView.getContext());
                    editText.setId(View.generateViewId());
                    editText.setText((Integer.valueOf(gameData.getData(2 * i + j,k).getValue())).toString());
                    InputFilter[] filter = new InputFilter[1];
                    filter[0] = new InputFilter.LengthFilter(editTextMaxLength);
                    editText.setFilters(filter);

                    resultRows.addView(editText);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setKeyListener(DigitsKeyListener.getInstance(true, false));
                    Coordinates coordinate = new Coordinates();
                    coordinate.setColumn(k);
                    coordinate.setRow(2*i+j);
                    EditTextViewId.put(editText.getId(), coordinate);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            HandleTextChange(editText);
                        }
                    });

                }
                table.addView(resultRows, new TableLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            }

        }
        scrollView.addView(table, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layout = (RelativeLayout)rootView.findViewById(R.id.fragment_results_layout);
        layout.addView(scrollView,new TableLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
    }
}

