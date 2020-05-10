package com.saimirbaci.PesKatshTabel;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

/**
 * Created by saimirbaci on 28/04/15.
 */
public class GameListAdapter extends BaseAdapter {
    private Context mContext;
    private List<GameFileName> mListPhoneBook;

    public GameListAdapter(Context context, List<GameFileName> list) {
        mContext = context;
        mListPhoneBook = list;
    }

    @Override
    public int getCount() {
        return mListPhoneBook.size();
    }

    @Override
    public Object getItem(int pos) {
        return mListPhoneBook.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // get selected entry
        GameFileName entry = mListPhoneBook.get(pos);

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.game_file_name_layout, null);
        }

        // set name
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getName());

        return convertView;
    }


}
