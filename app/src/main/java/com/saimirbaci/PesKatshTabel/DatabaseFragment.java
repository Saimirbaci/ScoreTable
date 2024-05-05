/**
 * Made by saimirbaci on 01/05/15.
 */
package com.saimirbaci.PesKatshTabel;

import android.app.Activity;
import android.view.View;
import androidx.fragment.app.Fragment;

import java.util.List;


public abstract class DatabaseFragment extends Fragment {

    static View rootView = null;

    Activity activity;

    public DatabaseFragment(){
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

    public void setListGameNames(List<GameFileName> listGameNames){

    }

    public interface OnLoadGameNames{
        public void loadGameNames(DatabaseFragment obj);
    }
}


