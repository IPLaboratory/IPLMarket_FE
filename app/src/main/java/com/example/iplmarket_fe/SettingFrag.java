package com.example.iplmarket_fe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFrag extends Fragment {
    private Button set_btn_logout;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.settingfrag, container, false);

        TextView textView2 = fragmentView.findViewById(R.id.textView2);
        TextView textView3 = fragmentView.findViewById(R.id.textView3);
        TextView textView5 = fragmentView.findViewById(R.id.textView5);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWishListActivity();
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWroteActivity();
            }
        });

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuestionActivity();
            }
        });

        // 로그아웃 버튼 클릭 이벤트 처리
        set_btn_logout = fragmentView.findViewById(R.id.set_btn_logout);
        set_btn_logout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });

        return fragmentView;
    }

    private void openWishListActivity() {
        Intent intent = new Intent(getActivity(), Wishlist.class);
        startActivity(intent);
    }

    private void openWroteActivity() {
        Intent intent = new Intent(getActivity(), Wrote.class);
        startActivity(intent);
    }

    private void openQuestionActivity() {
        Intent intent = new Intent(getActivity(), Question.class);
        startActivity(intent);
    }
}