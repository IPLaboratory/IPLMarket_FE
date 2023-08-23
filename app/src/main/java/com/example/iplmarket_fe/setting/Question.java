package com.example.iplmarket_fe.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.R;
import com.google.android.material.chip.Chip;

public class Question extends AppCompatActivity {

    private Chip chip;
    private TextView textView8;
    private boolean isTextViewVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        chip = findViewById(R.id.chip);
        textView8 = findViewById(R.id.textView8);
        textView8.setVisibility(View.GONE); // 처음에는 textView8을 안 보이도록 설정

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTextViewVisibility();
            }
        });
    }

    private void toggleTextViewVisibility() {
        if (isTextViewVisible) {
            textView8.setVisibility(View.GONE);
        } else {
            textView8.setVisibility(View.VISIBLE);
        }
        isTextViewVisible = !isTextViewVisible; // 가시성 상태를 토글
    }
}