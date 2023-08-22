package com.example.iplmarket_fe.home;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.home.OnProductItemClickListener;
import com.example.iplmarket_fe.home.Product;
import com.example.iplmarket_fe.home.ProductAdapter;
import com.example.iplmarket_fe.home.Detail;

public class HomeFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.homefrag, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = fragmentView.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        final ProductAdapter adapter = new ProductAdapter();

        // 예시
        adapter.addItem(new Product("책상", "3만" , "거의 새 상품", R.drawable.desk));
        adapter.addItem(new Product("의자", "2만" , "거의 새 상품", R.drawable.chair));
        adapter.addItem(new Product("책장", "4만" , "거의 새 상품", R.drawable.bookshelf));
        adapter.addItem(new Product("화장대", "5만" , "거의 새 상품", R.drawable.dressing_table));
        adapter.addItem(new Product("소파", "3만" , "거의 새 상품", R.drawable.sofa));
        adapter.addItem(new Product("화병", "1만" , "거의 새 상품", R.drawable.vase));
        adapter.addItem(new Product("옷장", "6만" , "거의 새 상품", R.drawable.closet));
        adapter.addItem(new Product("휴대폰", "50만" , "거의 새 상품", R.drawable.phone));
        adapter.addItem(new Product("청소기", "1만" , "거의 새 상품", R.drawable.cleaner));

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {
                Product item = adapter.getItem(position);
                Toast.makeText(getContext(), "이름 : " + item.getName() + "\n 가격 : " + item.getCost() +
                        "\n 설명 : " + item.getNotification(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), Detail.class);
                startActivity(intent);
            }
        });

        return fragmentView;
    }
}
