package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.MainActivity;
import com.example.iplmarket_fe.R;

public class Login extends AppCompatActivity {
    private EditText login_id, login_pw;
    private Button login_btn_lo, login_btn_re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 값을 가져옴
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        // Enter key 방지
        login_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        login_pw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });


        // 로그인 버튼 클릭 시 수행
        login_btn_lo = findViewById(R.id.login_btn_lo);
        login_btn_lo.setOnClickListener(view -> {
            // EditText에 현재 입력되어있는 값을 가져옴
            String userID = login_id.getText().toString();
            String userPW = login_pw.getText().toString();

                /*
                 서버 연결
                 입력된 값이 DB에 저장된 값과
                 같으면 -> Toast.makeText(this.getApplicationContext(),"로그인 성공!", Toast.LENGTH_SHORT).show();
                 다르면 -> Toast.makeText(this.getApplicationContext(),"아이디 혹은 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                 */

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        });

        // 회원가입 버튼 클릭 시 수행
        login_btn_re = findViewById(R.id.login_btn_re);
        login_btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
