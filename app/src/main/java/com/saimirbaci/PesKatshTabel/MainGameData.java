/**
 * Saimir created it on 05/05/15.
 */
package com.saimirbaci.PesKatshTabel;

public class MainGameData {

    private Records[][] data;
    private String[] playerNames;
    private int numberOfTurns, numberOfPlayers;

    public String[] getPlayerNames() {
        return playerNames;
    }

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public MainGameData (String[] playerNames, int rows, int columns){
        this.playerNames = playerNames;
        this.numberOfPlayers = columns;
        this.numberOfTurns = rows;
        data = new Records[rows][columns];
        for (int i=0; i<numberOfTurns;i++)
            for(int j=0; j<numberOfPlayers; j++)
                this.setData(i, j, new Records());

        for (int i=0; i<numberOfTurns; i++)
            for (int j=0; j<numberOfPlayers; j++) {
                this.getData(i, j).init();
            }
    }
    public MainGameData (int rows, int columns){
        data = new Records[rows][columns];
        for (int i=0; i<numberOfTurns;i++)
            for(int j=0; j<numberOfPlayers; j++)
                this.setData(i, j, new Records());

        for (int i=0; i<numberOfTurns; i++)
            for (int j=0; j<numberOfPlayers; j++) {
                this.getData(i,j).init();
            }
    }

    void setData(int row, int column, Records value){
        data[row][column] = value;
    }

    void setData(int row, int column, int value){
        data[row][column].setValue(value);
    }

    Records getData(int row, int column){
        return data[row][column];
    }
}
