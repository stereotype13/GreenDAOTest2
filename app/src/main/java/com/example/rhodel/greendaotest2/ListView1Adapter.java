package com.example.rhodel.greendaotest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rhodel on 8/16/2017.
 */

public class ListView1Adapter extends ArrayAdapter<User> {
    public ListView1Adapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView)convertView;

        if ( textView == null ) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            textView = (TextView)layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }


        User user = getItem(position);
        textView.setText(user.getName());
        return textView;
    }
}
