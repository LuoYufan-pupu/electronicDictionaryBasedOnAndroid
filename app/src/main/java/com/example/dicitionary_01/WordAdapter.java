package com.example.dicitionary_01;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private List<Words> wordsList = new ArrayList<>();



    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        View wordView;
        public ViewHolder(View view){
            super(view);
            wordView = view;
            textView1 = (TextView)view.findViewById(R.id.text_word);
            textView2 = (TextView)view.findViewById(R.id.text_translate);
            textView3 = (TextView)view.findViewById(R.id.text_pronunciation);
        }
    }
    public WordAdapter(List<Words> wordsList) {
        this.wordsList = wordsList;
    }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_item, parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.wordView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Words words = wordsList.get(position);
                    Toast.makeText(v.getContext(),words.getWord(),Toast.LENGTH_SHORT).show();
                }
            });
            return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Words words = wordsList.get(position);
        holder.textView1.setText(words.getWord());
        holder.textView2.setText(words.getTranslate());
        holder.textView3.setText(words.getPronunciation());
    }
    @Override
    public int getItemCount(){
        return wordsList.size();
    }
}
