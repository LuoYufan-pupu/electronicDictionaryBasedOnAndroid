package com.example.dicitionary_01;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private List<Words> word1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);//自定义toolbar

        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);//侧边栏
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24);
        }
        //navigationView.setCheckedItem(R.id.nav_new_words);//预选
        //监听头像
        View head = navigationView.inflateHeaderView(R.layout.nav_header);
        ImageView head_iv= (ImageView) head.findViewById(R.id.icon_image);
        head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "您点击了头像",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

       //监听menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_new_words:
                        //drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this,WordBookActivity.class));
                        break;
                    case R.id.nav_about_me:
                        startActivity(new Intent(MainActivity.this,AboutMeActivity.class));
                        //Toast.makeText(MainActivity.this,"about_me",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

                return true;
            }
        });



        //圆巴巴
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.translate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TranslateActivity.class);
                startActivity(intent);
            }
        });


        /////////////////////////////////////逻辑部分/////////////////////////////////////


//        // 读取外部json文件到string流,转移至baseactivity.frist方法中
//        try {
//            // 读取assets目录里的test.json文件，获取字节输入流
//            InputStream is = getResources().getAssets().open("englishwords4.json");
//            // 获取字节输入流长度
//            int length = is.available();
//            // 定义字节缓冲区
//            byte[] buffer = new byte[length];
//            // 读取字节输入流，存放到字节缓冲区里
//            is.read(buffer);
//            // 将字节缓冲区里的数据转换成utf-8字符串
//            content = EncodingUtils.getString(buffer, "utf-8");
//            parseJSONWithGSON(content);
//            // 关闭输入流
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//        //创建litepal操作数据库
//        LitePal.getDatabase();
//        //添加数据
////        Words words1 =new Words("apple","挨炮","苹果");
////        words1.save();
////        Words words2 =new Words("banana","不拉了","香蕉");
////        words2.setWord("ab");
////        words2.save();
//        // TextView textView = findViewById(R.id.text);
//        DataSupport.saveAll(wordList);
        //将数据库中的内容添加到list数组
        //word1 = DataSupport.findAll(Words.class);
        //wordList.clear();

        //RV
        //initWord();
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        WordAdapter adapter = new WordAdapter(word1);
//        recyclerView.setAdapter(adapter);














    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView)mSearch.getActionView();
        mSearchView.setIconifiedByDefault(true);//设置搜索框展开时是否显示按钮
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//设置键盘回车键为搜索
        EditText editText = (EditText)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this,R.color.white));//输入框文字颜色

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()){
                    word1.clear();
                    //Toast.makeText(MainActivity.this,newText,Toast.LENGTH_LONG).show();
                }else {
                    //将查询框中的字符进行查询，模糊搜索20条
                    word1 = DataSupport.where("word like ?",newText+"%").limit(20).find(Words.class);
                    //Toast.makeText(MainActivity.this,"111",Toast.LENGTH_LONG).show();

                }
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                WordAdapter adapter = new WordAdapter(word1);
                recyclerView.setAdapter(adapter);
                //Toast.makeText(MainActivity.this,newText,Toast.LENGTH_LONG).show();
                return true;
            }
        });




        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings_button:
                //添加点击动作
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);//侧边导航栏
                break;
            default:
                break;
        }
        return true;
    }

//    private void initWord(){
//        for(int i = 0;i<30;i++){
//            Words word = new Words("apple","超级无敌宇宙第一他妈的贼大的大大大大大大大苹果","a");
//            wordsList.add(word);
//        }
//    }
//    private void parseJSONWithGSON(String content1){
//        Gson gson = new Gson();
//        wordList = gson.fromJson(content1,new TypeToken<List<Words>>(){}.getType());
//
//}




}
