/*
* Created By Saimir Baci
* */
package com.saimirbaci.PesKatshTabel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;


public class SaveToDatabaseFragment extends DatabaseFragment {

    private EditText FileNameEditText = null;

    public SaveToDatabaseFragment(){
    }

    public EditText getFileNameEditText(){
        return FileNameEditText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_save_to_database, container, false);
        ((OnLoadGameNames) activity).loadGameNames(this);
        FileNameEditText = (EditText) rootView.findViewById(R.id.etSaveToDatabase);
        Button saveToDatabase = (Button)rootView.findViewById(R.id.btnOkSaveToDatabase);
        saveToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnSaveButtonClicked)activity).onSaveToDatabaseClicked();
            }
        });
        Button cancelSaveToDatabase = (Button) rootView.findViewById(R.id.btnCancelSaveToDatabase);
        cancelSaveToDatabase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ((OnSaveButtonClicked)activity).onCancelSaveToDatabaseClicked();
            }
        });
        return rootView;
    }

    public void setListGameNames(List<GameFileName> listGameNames){
        GameListAdapter adapter;
        ListView lvGameFileNames;
        lvGameFileNames = (ListView)rootView.findViewById(R.id.listFileNames);
        if(lvGameFileNames != null){
            adapter = new GameListAdapter(rootView.getContext(), listGameNames);
            lvGameFileNames.setAdapter(adapter);
        }
    }

    public interface OnSaveButtonClicked{
        public void onSaveToDatabaseClicked();
        public void onCancelSaveToDatabaseClicked();
    }


}