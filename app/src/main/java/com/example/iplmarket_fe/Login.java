package com.example.iplmarket_fe;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private EditText login_id, login_pw;
    private Button login_btn_lo, login_btn_re;

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 값을 가져옴
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        // Enter key 방지
        login_id.setOnKeyListener((view, keyCode, event) -> keyCode == KeyEvent.KEYCODE_ENTER);
        login_pw.setOnKeyListener((view, keyCode, event) -> keyCode == KeyEvent.KEYCODE_ENTER);

        // Retrofit을 이용하여 서버와 연결
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.35.91:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ServiceApi.class);

        // 로그인 버튼 클릭 시 수행
        login_btn_lo = findViewById(R.id.login_btn_lo);
        login_btn_lo.setOnClickListener(view -> {
            // EditText에 현재 입력되어있는 값을 가져옴
            String userID = login_id.getText().toString();
            String userPW = login_pw.getText().toString();

            // 로그인 데이터 유효성 검사
            if (userID.isEmpty() || userPW.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 로그인 시도
                startLogin(new LoginData(userID, userPW));
            }
        });

        // 회원가입 버튼 클릭 시 수행
        login_btn_re = findViewById(R.id.login_btn_re);
        login_btn_re.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

    private void startLogin(LoginData data) {
        // 로그인 요청 보내기
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse result = response.body();
                    Toast.makeText(Login.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                    if (result.isSuccess()) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                // 로그인 에러 처리
            }
        });
    }
}
