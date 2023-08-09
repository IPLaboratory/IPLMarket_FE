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
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CreateFrag createFragment;
    private HomeFrag homeFragment;
    private SettingFrag settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 하단 네비게이션 뷰 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_create) {
                    switchToFragment(0);
                } else if (itemId == R.id.action_home) {
                    switchToFragment(1);
                } else if (itemId == R.id.action_settings) {
                    switchToFragment(2);
                }
                return true;
            }
        });

        createFragment = new CreateFrag();
        homeFragment = new HomeFrag();
        settingFragment = new SettingFrag();
        switchToFragment(0); // Set the initial fragment screen
    }

    // 프래그먼트 전환 메소드
    private void switchToFragment(int fragmentIndex) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (fragmentIndex) {
            case 0:
                fragmentTransaction.replace(R.id.main_frame, createFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.main_frame, homeFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.main_frame, settingFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}
