/*
* Created By Saimir Baci
* */
package com.saimirbaci.PesKatshTabel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;


public class SaveToDatabaseFragment extends DatabaseFragment {

    public SaveToDatabaseFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_save_to_database, container, false);
        ((OnLoadGameNames) activity).loadGameNames(this);
        Button saveToDatabase = rootView.findViewById(R.id.btnOkSaveToDatabase);
        saveToDatabase.setOnClickListener(view -> ((OnSaveButtonClicked)activity).onSaveToDatabaseClicked());
        Button cancelSaveToDatabase =  rootView.findViewById(R.id.btnCancelSaveToDatabase);
        cancelSaveToDatabase.setOnClickListener(view -> ((OnSaveButtonClicked)activity).onCancelSaveToDatabaseClicked());
        return rootView;
    }

    public void setListGameNames(List<GameFileName> listGameNames){
        GameListAdapter adapter;
        ListView lvGameFileNames;
        lvGameFileNames = rootView.findViewById(R.id.listFileNames);
        if(lvGameFileNames != null){
            adapter = new GameListAdapter(rootView.getContext(), listGameNames);
            lvGameFileNames.setAdapter(adapter);
        }
    }

    public interface OnSaveButtonClicked{
        void onSaveToDatabaseClicked();
        void onCancelSaveToDatabaseClicked();
    }


}