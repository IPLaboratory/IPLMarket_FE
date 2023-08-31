package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.server.response.IdValidationResponse;
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.server.request.RegistRequest;
import com.example.iplmarket_fe.server.response.RegisterResponse;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.request.IdValidationRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private EditText reg_id, reg_pwd, reg_name, reg_nickname, reg_number;
    private TextView len_id, len_pw, len_nick;
    private Button reg_btn, reg_idCheck;

    private ProgressBar progressBar;

    private ServiceApi serviceApi;

    private boolean isIdAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 값을 가져옴
        reg_id = findViewById(R.id.reg_id);
        reg_pwd = findViewById(R.id.reg_pw);
        reg_name = findViewById(R.id.reg_name);
        reg_nickname = findViewById(R.id.reg_nick);
        reg_number = findViewById(R.id.reg_number);

        len_id = findViewById(R.id.len_id);
        len_pw = findViewById(R.id.len_pw);
        len_nick = findViewById(R.id.len_nick);

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.serverIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        serviceApi = retrofit.create(ServiceApi.class);

        progressBar = findViewById(R.id.progressBar);

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
                len_id.setText(s.length() + "/10");
            }
        });

        reg_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                len_pw.setText(s.length() + "/15");
            }
        });

        reg_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                len_nick.setText(s.length() + "/12");
            }
        });

        // 아이디 중복 검사 버튼 클릭 시 수행
        reg_idCheck = findViewById(R.id.reg_idCheck);
        reg_idCheck.setOnClickListener(view -> {
            String userID = reg_id.getText().toString();
            IdValidationRequest idValidationRequest = new IdValidationRequest(userID);

            serviceApi.checkResponse(idValidationRequest).enqueue(new Callback<IdValidationResponse>() {
                @Override
                public void onResponse(Call<IdValidationResponse> call, Response<IdValidationResponse> response) {
                    if (response.isSuccessful()) {
                        IdValidationResponse idValidationResponse = response.body();

                        if (response.code() == 200) {
                            if (idValidationResponse.isSuccess()) {
                                // 해당 아이디 사용 가능
                                isIdAvailable = true;
                                Toast.makeText(Register.this, idValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                // 해당 아이디 이미 존재
                                isIdAvailable = false;
                                Toast.makeText(Register.this, idValidationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.code() == 500) {
                            // 내부 서버 오류
                            Toast.makeText(Register.this, "내부 서버 오류", Toast.LENGTH_SHORT).show();
                        } else {
                            // 다른 응답 코드
                            Toast.makeText(Register.this, "서버 응답 오류1", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 서버 응답 오류
                        Toast.makeText(Register.this, "서버 응답 오류2", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<IdValidationResponse> call, Throwable t) {
                    // 네트워크 오류 처리
                    Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        // 회원가입 버튼 클릭 시 수행
        reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(view -> {
            if (isIdAvailable) {
                String userId = reg_id.getText().toString();
                String userPwd = reg_pwd.getText().toString();
                String userName = reg_name.getText().toString();
                String userNickname = reg_nickname.getText().toString();
                String userNumberStr = reg_number.getText().toString();

                String formattedNumber = formatPhoneNumber(userNumberStr);

                RegistRequest registRequest = new RegistRequest(userId, userPwd, userName, userNickname, formattedNumber);

                // 회원가입 수행
                registerRun(registRequest);

                // 로그인 화면으로 이동
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            } else {
                Toast.makeText(Register.this, "아이디 사용 가능 여부를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 전화번호 형식 변경 함수
    private String formatPhoneNumber(String phoneNumber) {
        String strippedNumber = phoneNumber.replaceAll("-", "");

        if (strippedNumber.length() == 11) {
            return strippedNumber.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else {
            return phoneNumber;
        }
    }

    private void registerRun(RegistRequest data) {
        serviceApi.userRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    int responseCode = registerResponse.getCode(); // 응답 코드 가져오기

                    if (responseCode == 201) {
                        // 회원가입 성공 처리
                        Toast.makeText(Register.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (responseCode == 400) {
                        // 회원가입 실패 처리
                        Toast.makeText(Register.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (responseCode == 500){
                        // 오류 발생 처리
                        Toast.makeText(Register.this, "알 수 없는 오류 발생", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 서버 오류 처리
                    Toast.makeText(Register.this, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }

                showProgress(false);
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(Register.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
                showProgress(false);
            }
        });
    }


    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}

