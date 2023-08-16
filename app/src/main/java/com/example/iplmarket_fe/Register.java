package com.example.iplmarket_fe;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private EditText reg_id, reg_pw, reg_name, reg_nick, reg_number;
    private TextView len_id, len_pw, len_nick;
    private Button reg_btn;

    private ProgressBar progressBar;

    private ServiceApi service;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.35.91:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ServiceApi.class);

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

        reg_pw.addTextChangedListener(new TextWatcher() {
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

        reg_nick.addTextChangedListener(new TextWatcher() {
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

        // 회원가입 버튼 클릭 시 수행
        reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(view -> {
            // EditText에 현재 입력되어있는 값을 가져옴
            String userID = reg_id.getText().toString();
            String userPW = reg_pw.getText().toString();
            String userName = reg_name.getText().toString();
            String userNickname = reg_nick.getText().toString();
            int userNumber = Integer.parseInt(reg_number.getText().toString());

            // 아이디 중복 검사 수행
            performIdCheck();

            // 회원가입 데이터 생성
            RegisterData registerData = new RegisterData(userID, userPW, userName, userNickname, userNumber);

            // 회원가입 수행
            회원가입수행(registerData);

            // 로그인 화면으로 이동
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });


    }
    private void performIdCheck() {
        String userID = reg_id.getText().toString();
        String userPW = reg_pw.getText().toString();
        String userName = reg_name.getText().toString();
        String userNickname = reg_nick.getText().toString();
        String userNumber = reg_number.getText().toString();
        String id = reg_id.getText().toString();

        // 아이디 중복 검사 수행
        service.checkDuplicateId(id).enqueue(new Callback<IdCheckResponse>() {
            @Override
            public void onResponse(Call<IdCheckResponse> call, Response<IdCheckResponse> response) {
                if (response.isSuccessful()) {
                    IdCheckResponse result = response.body();
                    if (result != null) {
                        if (result.isSuccess()) {
                            // 사용 가능한 아이디입니다.
                            Toast.makeText(Register.this, "사용 가능한 아이디", Toast.LENGTH_SHORT).show();
                        } else {
                            // 아이디 중복인 경우
                            Toast.makeText(Register.this, "이미 사용 중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Register.this, "서버 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
                // 중복 검사 결과에 상관없이 UI 상태를 원래대로 복구
                // showProgress(false);
            }

            @Override
            public void onFailure(Call<IdCheckResponse> call, Throwable t) {
                Toast.makeText(Register.this, "아이디 중복 확인 에러", Toast.LENGTH_SHORT).show();
                Log.e("아이디 중복 확인 에러", t.getMessage());
                // showProgress(false);
            }
        });
    }



    private void 회원가입수행(RegisterData data) {
        service.userRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse result = response.body();
                    if (result != null) {
                        int responseCode = result.getCode(); // 응답 코드 가져오기

                        if (responseCode == 201) {
                            // 회원가입 성공 처리
                            Toast.makeText(Register.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (responseCode == 400) {
                            // 회원가입 실패 처리
                            Toast.makeText(Register.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (responseCode == 500){
                            // 오류 발생 처리
                            Toast.makeText(Register.this, "알 수 없는 오류 발생", Toast.LENGTH_SHORT).show();
                        }
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

