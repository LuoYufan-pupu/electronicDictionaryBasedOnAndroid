package com.example.dicitionary_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordBookActivity extends BaseActivity {
    private List<NewWords> newWords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_book2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //读取数据库
        newWords = DataSupport.findAll(NewWords.class);
        Collections.reverse(newWords);//倒序

        //ListView
        NewWordAdapter adapter = new NewWordAdapter(WordBookActivity.this,
                R.layout.newwords_item,newWords);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
