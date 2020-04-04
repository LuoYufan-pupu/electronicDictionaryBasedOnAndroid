package com.example.dicitionary_01;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {


    protected Toolbar myToolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    //private List<Words> wordsList = new ArrayList<>();
    protected List<Words> wordList = new ArrayList<>();
    /*** 文件内容字符串*/
    protected String content;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity",getClass().getSimpleName());


        //使json数据读取和对象进入数据库只执行一次
        SharedPreferences pre = getSharedPreferences("data",MODE_PRIVATE);
        boolean flag = pre.getBoolean("flag", false);
        if(flag == false){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    first();
                }
            }).start();
            final ProgressDialog myDialog = ProgressDialog.show(BaseActivity.this, "正在导入词典", "大概需要一分钟", true, false);
            new Thread() {
                public void run() {
                    try{
                        sleep(60000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    myDialog.dismiss();
                }}.start();
        }
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean("flag",true);
        editor.apply();
    }


    public void first(){
        // 读取外部json文件到string流
        try {
            // 读取assets目录里的test.json文件，获取字节输入流
            InputStream is = getResources().getAssets().open("englishwords.json");
            // 获取字节输入流长度
            int length = is.available();
            // 定义字节缓冲区
            byte[] buffer = new byte[length];
            // 读取字节输入流，存放到字节缓冲区里
            is.read(buffer);
            // 将字节缓冲区里的数据转换成utf-8字符串
            content = EncodingUtils.getString(buffer, "utf-8");
            parseJSONWithGSON(content);
            // 关闭输入流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建litepal操作数据库
        LitePal.getDatabase();
        //添加数据
//        Words words1 =new Words("apple","挨炮","苹果");
//        words1.save();
//        Words words2 =new Words("banana","不拉了","香蕉");
//        words2.setWord("ab");
//        words2.save();
        // TextView textView = findViewById(R.id.text);
        DataSupport.saveAll(wordList);
    }

    //将json格式的字符串自动映射为一个对象
    private void parseJSONWithGSON(String content1){
        Gson gson = new Gson();
        wordList = gson.fromJson(content1,new TypeToken<List<Words>>(){}.getType());
    }



    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



}
