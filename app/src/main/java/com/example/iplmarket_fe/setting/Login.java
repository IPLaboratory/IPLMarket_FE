package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.server.request.LoginRequest;
import com.example.iplmarket_fe.server.response.LoginResponse;
import com.example.iplmarket_fe.MainActivity;
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.server.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private EditText login_id, login_pw;
    private Button login_btn_lo, login_btn_re;

    private ServiceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 값을 가져옴
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        // Retrofit을 이용하여 서버와 연결
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.serverIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(ServiceApi.class);

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
                startLogin(new LoginRequest(userID, userPW));
            }
            // Intent intent = new Intent(this, MainActivity.class);
            // startActivity(intent);
        });

        // 회원가입 버튼 클릭 시 수행
        login_btn_re = findViewById(R.id.login_btn_re);
        login_btn_re.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

    // 로그인 요청 보내기
    private void startLogin(LoginRequest data) {
        serviceApi.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("QQQQQQQ", "sssssssss");
                if(response.body() != null) {
                    LoginResponse result = response.body();
                    Log.d("qq", result.getMessage());

                    if (result.isSuccess()) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d("aa", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("ff", "로그인 에러 발생");
            }
        });
    }
}
