package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.R;

public class Register extends AppCompatActivity {

    private EditText reg_id, reg_pw, reg_name, reg_nick, reg_number;
    private TextView len_id, len_pw, len_nick;
    private Button reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 값을 가져옴
        reg_id = findViewById(R.id.reg_id);
        reg_pw = findViewById(R.id.reg_pw);
        reg_name = findViewById(R.id.reg_name);
        reg_nick = findViewById(R.id.reg_nick);
        reg_number = findViewById(R.id.reg_number);

        len_id = findViewById(R.id.len_id);
        len_pw = findViewById(R.id.len_pw);
        len_nick = findViewById(R.id.len_nick);

        //EditText 글자 수 제한
        reg_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                len_id.setText(s.length()+"/10");
            }
        });

        reg_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                len_pw.setText(s.length()+"/15");
            }
        });

        reg_nick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                len_nick.setText(s.length()+"/12");
            }
        });

        // Enter key 방지
        reg_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        reg_pw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        reg_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }

                return false;
            }
        });

        reg_nick.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }

                return false;
            }
        });

        reg_number.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }

                return false;
            }
        });


        // 회원가입 버튼 클릭 시 수행
        reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(view -> {
            // EditText에 현재 입력되어있는 값을 가져옴
            String userID = reg_id.getText().toString();
            String userPW = reg_pw.getText().toString();
            String userName = reg_name.getText().toString();
            String userNickname = reg_nick.getText().toString();
            int userNumber = Integer.parseInt(reg_number.getText().toString());

           // 입력된 정보를 DB에 저장

            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });
    }
}

