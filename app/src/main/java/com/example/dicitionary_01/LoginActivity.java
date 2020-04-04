package com.example.dicitionary_01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button button;
    EditText editText1;
    EditText editText2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = (Button)findViewById(R.id.blogin);
        editText1 = (EditText)findViewById(R.id.elogin_1);
        editText2 = (EditText)findViewById(R.id.elogin_2);
    }
}
