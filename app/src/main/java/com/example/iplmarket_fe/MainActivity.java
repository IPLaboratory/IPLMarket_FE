package com.example.iplmarket_fe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.iplmarket_fe.create.CreateFrag;
import com.example.iplmarket_fe.home.HomeFrag;
import com.example.iplmarket_fe.setting.SettingFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private CreateFrag createfrag;
    private HomeFrag homefrag;
    private SettingFrag settingfrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 바텀 네비게이션 뷰
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_create) {
                    setFrag(0);
                } else if (itemId == R.id.action_home) {
                    setFrag(1);
                } else if (itemId == R.id.action_settings) {
                    setFrag(2);
                }
                return true;
            }
        });

        createfrag = new CreateFrag();
        homefrag = new HomeFrag();
        settingfrag = new SettingFrag();
        setFrag(0); // 첫 프래그먼트 화면 지정
    }

    // 프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, createfrag);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, homefrag);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, settingfrag);
                ft.commit();
                break;
        }
    }
}