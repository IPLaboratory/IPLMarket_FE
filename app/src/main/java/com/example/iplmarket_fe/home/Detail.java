package com.example.iplmarket_fe.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.R;

public class Detail extends AppCompatActivity {

    private ImageView detailImageView;
    private TextView detailEtName, detailEtPrice, detailEtContent, detailEtDate;
    private Button detailBtnVr, detailBtnBuy;
    private ImageButton detailBtnImage;
    boolean i = true;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // http

        // VR Road 버튼 클릭 이벤트 처리
        detailBtnVr = findViewById(R.id.detailBtnVr);
        detailBtnVr.setOnClickListener(view -> {
            Intent intent = new Intent(Detail.this, ModelingRoad.class);
            startActivity(intent);
        });

        // 구매 버튼 클릭 이벤트 처리 -> 채팅
        detailBtnBuy = findViewById(R.id.detailBtnBuy);
        detailBtnBuy.setOnClickListener(view -> {
            Intent intent = new Intent(Detail.this, Chatting.class);
            startActivity(intent);
        });

        // 관심 버튼 클릭 이벤트 처리
        detailBtnImage = findViewById(R.id.detailBtnImage);
        detailBtnImage.setOnClickListener(view -> {
            if (i) {
                detailBtnImage.setImageResource(R.drawable.heart_after);
                i = false;
                Toast.makeText(this.getApplicationContext(),"관심 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }else {
                detailBtnImage.setImageResource(R.drawable.heart_before);
                i = true;
                Toast.makeText(this.getApplicationContext(),"관심 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}