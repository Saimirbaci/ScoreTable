/*
* Created By Saimir Baci
* */
package com.saimirbaci.PesKatshTabel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class StartFragment extends Fragment {
    static View rootView = null;
    Activity activity;

   @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button newGameButton = (Button)rootView.findViewById(R.id.newButton);
        final Button openLastGameButton = (Button)rootView.findViewById(R.id.openButton);
        EditText userName = (EditText) rootView.findViewById(R.id.edit_name_1);
        userName.setText(Main.UserName);

        openLastGameButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               try{
                   ((OnOpenLastClicked) activity).onOpenLast();
               }catch (ClassCastException cce){
                   System.out.println("Something went wrong");
               }
           }
        });
        newGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                List<String> playerNames = new ArrayList<String>();
                EditText p1 = (EditText) rootView.findViewById(R.id.edit_name_1);
                EditText p2 = (EditText) rootView.findViewById(R.id.edit_name_2);
                EditText p3 = (EditText) rootView.findViewById(R.id.edit_name_3);
                EditText p4 = (EditText) rootView.findViewById(R.id.edit_name_4);
                if(p1.getText().toString().equals("")){
                    AlertNoPlayerOneInserted("Percakto emrin e pjesemarresit te pare");
                    return;
                }
                if(p2.getText().toString().equals("")){
                    AlertNoPlayerOneInserted("Percakto emrin e pjesemarresit te dyte");
                    return;
                }

                if(!p1.getText().toString().equals("")){
                    playerNames.add(p1.getText().toString());
                }
                if(!p2.getText().toString().equals("")){
                    playerNames.add(p2.getText().toString());
                }
                if(!p3.getText().toString().equals("")){
                    playerNames.add(p3.getText().toString());
                }
                if(!p4.getText().toString().equals("")){
                    playerNames.add(p4.getText().toString());
                }

                try{
                    ((OnNewGameClicked) activity).onNewGame(playerNames);
                }catch (ClassCastException cce){
                    System.out.println("Something went wrong");
                }
            }
        });
        return rootView;
    }
    private void AlertNoPlayerOneInserted(String msg){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(rootView.getContext());

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("5K Pike");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public interface OnNewGameClicked{
        public void onNewGame(List<String> playerNames );
    }

    public interface OnOpenLastClicked{
        public void onOpenLast();
    }
}
