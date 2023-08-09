package com.example.iplmarket_fe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.iplmarket_fe.create.CreateFrag;
import com.example.iplmarket_fe.home.HomeFrag;
import com.example.iplmarket_fe.setting.SettingFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private CreateFrag createfrag;
    private HomeFrag homefrag;
    private SettingFrag settingfrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        // 하단 네비게이션 뷰 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_create) {
                switchToFragment(0);
            } else if (itemId == R.id.action_home) {
                switchToFragment(1);
            } else if (itemId == R.id.action_settings) {
                switchToFragment(2);
            }
            return true;
        });

        createfrag = new CreateFrag();
        homefrag = new HomeFrag();
        settingfrag = new SettingFrag();
        switchToFragment(0); // 첫 프래그먼트 화면 지정
    }

    // 프래그먼트 전환 메소드
    private void switchToFragment(int n) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (n) {
            case 0:
                fragmentTransaction.replace(R.id.main_frame, createfrag);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.main_frame, homefrag);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.main_frame, settingfrag);
                fragmentTransaction.commit();
                break;
        }
    }
}