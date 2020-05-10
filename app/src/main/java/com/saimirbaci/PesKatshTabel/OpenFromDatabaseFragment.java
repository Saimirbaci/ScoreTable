/**
 * Made by saimirbaci on 01/05/15.
 */
package com.saimirbaci.PesKatshTabel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;

public class OpenFromDatabaseFragment extends DatabaseFragment{

    GameFileName selectedGame;

    public OpenFromDatabaseFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_open_from_database, container, false);
        ((OnLoadGameNames) activity).loadGameNames(this);

        Button openFromDatabase = (Button)rootView.findViewById(R.id.btnOpenFromDatabase);
        openFromDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnOpenButtonClicked) activity).onOpenFromDatabaseClicked(selectedGame.getName());
            }
        });

        Button cancelFromDatabase = (Button) rootView.findViewById(R.id.btnCancelOpenFromDatabase);
        cancelFromDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnOpenButtonClicked) activity).onCancelFromDatabaseClicked();
            }
        });

        ListView gameList = (ListView) rootView.findViewById(R.id.gamesList);
        try {
        gameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                try{
                    Object o = a.getItemAtPosition(position);
                    selectedGame = (GameFileName)o;
                    setFocus(position);
                }
                catch (Exception e){
                    System.out.println("Exception");
                     //Toast.makeText(ListViewImagesActivity.this, "You have chosen : " + " " + obj_itemDetails.getName(), Toast.LENGTH_LONG).show();
                }
            }

        });
        }
        catch(Exception e){
            System.out.println("Exception");
        }
        return rootView;
    }
    public void setListGameNames(List<GameFileName> listGameNames){
        GameListAdapter adapter;
        ListView lvGameFileNames;
        lvGameFileNames = (ListView)rootView.findViewById(R.id.gamesList);
        if(lvGameFileNames != null){
            adapter = new GameListAdapter(rootView.getContext(), listGameNames);
            lvGameFileNames.setAdapter(adapter);
        }
    }

    private void setFocus(int position){
        ListView lvGameFileNames;
        lvGameFileNames = (ListView)rootView.findViewById(R.id.gamesList);
        lvGameFileNames.setSelection(position);
        lvGameFileNames.requestFocus();
    }

    public interface OnOpenButtonClicked{
        public void onOpenFromDatabaseClicked(String name);
        public void onCancelFromDatabaseClicked();
    }

}
