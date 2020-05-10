/*
* Created By Saimir Baci
* */

package com.saimirbaci.PesKatshTabel;

public class GameResults {

    private Records[][] records;
    private String[] playerNames;
    private int numberOfTurns, numberOfPlayers;

    public GameResults(int numberOfPlayers, int numberOfTurns){
        this.numberOfTurns = numberOfTurns;
        this.numberOfPlayers = numberOfPlayers;
        records = new Records[numberOfTurns][numberOfPlayers];
        playerNames = new String[numberOfPlayers];
    }

    public Records getRecords(int row, int column){
        return records[row][column];
    }

    public void setRecords(int row, int column, Records record){
        records[row][column] = record;
    }

    public void setRecordsValue(int row, int column, int value){
        records[row][column].setValue(value);
    }

    public int getRecordsValue(int row, int column){
        return records[row][column].getValue();
    }

}
