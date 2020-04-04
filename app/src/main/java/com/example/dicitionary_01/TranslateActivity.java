package com.example.dicitionary_01;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dicitionary_01.baidutranslate.BaiduTranslateService;
import com.example.dicitionary_01.baidutranslate.MD5Utils;
import com.example.dicitionary_01.baidutranslate.RespondBean;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslateActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ERRRRRRRRR";
    EditText editText;
    Button button;
    TextView textView;
    static String to;//目标译文 可变 zh中文 en英文
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = (EditText) findViewById(R.id.edit);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.text);

    }
    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.button:
                //准备请求百度翻译接口需要的参数
                String word = editText.getText().toString();//需查询的单词 q
                String from = "auto";//源语种 en 英语 zh 中文

                //String中英文占用一个字节，中文占用两个字节，
                //利用String的这个存储特性可以用来判断String中有没有中文。
                //原文链接：https://blog.csdn.net/u012005549/article/details/82685063
                if (word.length() == word.getBytes().length) {//成立则说明没有汉字，否则由汉字。
                    to = "zh"; //没有汉字 英译中
                } else {
                    to = "en";//含有汉字 中译英
                }
                String appid = "20181016000220151";//appid 管理控制台有
                String salt = (int) (Math.random() * 100 + 1) + "";//随机数 这里范围是[0,100]整数 无强制要求
                String key = "Ff5DuSF8UqIDk9aNn36g";//密钥 管理控制台有
                String string1 = appid + word + salt + key;// string1 = appid+q+salt+密钥
                String sign = MD5Utils.getMD5Code(string1);// 签名 = string1的MD5加密 32位字母小写
                Retrofit retrofitBaidu = new Retrofit.Builder()
                        .baseUrl("https://fanyi-api.baidu.com/api/trans/vip/")
                        .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                        .build();
                BaiduTranslateService baiduTranslateService = retrofitBaidu.create(BaiduTranslateService.class);
                Call<RespondBean> call = baiduTranslateService.translate(word, from, to, appid, salt, sign);
                call.enqueue(new Callback<RespondBean>() {
                    @Override
                    public void onResponse(Call<RespondBean> call, Response<RespondBean> response) {
                        //请求成功
                        Log.d(TAG, "onResponse: 请求成功");
                        RespondBean respondBean = response.body();//返回的JSON字符串对应的对象
                        String result = respondBean.getTrans_result().get(0).getDst();//获取翻译的字符串String
                        Log.d(TAG, "英译中结果" + result);
                        textView.setText(result);
                    }

                    @Override
                    public void onFailure(Call<RespondBean> call, Throwable t) {
                        //请求失败 打印异常
                        Log.d(TAG, "onResponse: 请求失败 " + t);
                    }
                });

//                String inputText = editText.getText().toString();
//                Log.d("edit",);
//                textView.setText(inputText);
                break;
            default:
                break;
        }
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
