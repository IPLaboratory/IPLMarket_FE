package com.example.iplmarket_fe.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.iplmarket_fe.R;

public class Detail extends AppCompatActivity {

    private ImageView detail_imageView;
    private TextView detail_et_name, detail_et_price, detail_et_ex, detail_et_date;
    private Button detail_btn_vr, detail_btn_buy;
    private ImageButton detail_btn_image;
    boolean i = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 정보를 서버에서 가져와 출력


        // VR Road 버튼 클릭 이벤트 처리
        detail_btn_vr = findViewById(R.id.detail_btn_vr);
        detail_btn_vr.setOnClickListener(view -> {

        });

        // 구매 버튼 클릭 이벤트 처리 -> 채팅
        detail_btn_buy = findViewById(R.id.detail_btn_buy);
        detail_btn_buy.setOnClickListener(view -> {
            Intent intent = new Intent(Detail.this, Chatting.class);
            startActivity(intent);
        });

        // 관심 버튼 클릭 이벤트 처리
        detail_btn_image = findViewById(R.id.detail_btn_image);
        detail_btn_image.setOnClickListener(view -> {
            if (i) {
                detail_btn_image.setImageResource(R.drawable.heart_after);
                i = false;
                Toast.makeText(this.getApplicationContext(),"관심 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                // 관심 목록 페이지(Wishlist)에 추가
            }else {
                detail_btn_image.setImageResource(R.drawable.heart_before);
                i = true;
                Toast.makeText(this.getApplicationContext(),"관심 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                // 관심 목록 페이지(Wishlist)에서 삭제
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 뒤로 가기 버튼을 눌렀을 때 처리할 코드 추가
                setHomeFrag();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setHomeFrag() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HomeFrag homeFrag = new HomeFrag();
        ft.replace(R.id.main_frame, homeFrag);
        ft.commit();
    }
}