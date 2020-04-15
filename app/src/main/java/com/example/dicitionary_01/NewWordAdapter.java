package com.example.dicitionary_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewWordAdapter extends ArrayAdapter<NewWords> {
    private int resourceId;
    public  NewWordAdapter(Context context, int textViewResourceId,
                           List<NewWords> object){
        super(context,textViewResourceId,object);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        NewWords newWords = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView wordtext = (TextView) view.findViewById(R.id.list_word);
        TextView timetext = (TextView) view.findViewById(R.id.list_time);
        wordtext.setText(newWords.getWord());
        timetext.setText(newWords.getTime());
        return view;
    }
}
