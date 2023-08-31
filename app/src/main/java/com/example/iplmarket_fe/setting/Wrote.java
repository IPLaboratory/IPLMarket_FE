package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.MainActivity;
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.home.Detail;
import com.example.iplmarket_fe.server.PostData;
import com.example.iplmarket_fe.server.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Wrote extends AppCompatActivity {

    private List<ProductItem> productList;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrote);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // 예시 데이터 추가
        productList.add(new ProductItem(R.drawable.product1, "상품 1", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product2, "상품 2", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product3, "상품 3", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product4, "상품 4", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product5, "상품 5", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product6, "상품 6", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product7, "상품 7", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product8, "상품 8", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product9, "상품 9", "10,000₩", false));
        productList.add(new ProductItem(R.drawable.product10, "상품 10", "100,000₩", false));

        productAdapter.notifyDataSetChanged();
    }

    private class ProductItem {
        private int imageResource;
        private String name;
        private String price;
        private boolean wrote;

        public ProductItem(int imageResource, String name, String price, boolean wrote) {
            this.imageResource = imageResource;
            this.name = name;
            this.price = price;
            this.wrote = wrote;
        }

        public int getImageResource() {
            return imageResource;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private List<ProductItem> productList;

        public ProductAdapter(List<ProductItem> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_itemm, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            final ProductItem product = productList.get(position);

            holder.productImage.setImageResource(product.getImageResource());
            holder.productName.setText(product.getName());
            holder.productPrice.setText(product.getPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 상품 페이지로 이동
                    Intent intent = new Intent(Wrote.this, Detail.class);
                    startActivity(intent);
                }
            });

            holder.wroteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retrofit 설정
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BuildConfig.serverIP)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ServiceApi serviceApi = retrofit.create(ServiceApi.class);

                    // POST 요청 데이터 생성
                    PostData postData = new PostData(product.getName(), "Content", "image_name", "user_id");

                    // POST 요청 보내기
                    Call<PostData> call = serviceApi.WroteResponse(postData);
                    call.enqueue(new Callback<PostData>() {
                        @Override
                        public void onResponse(Call<PostData> call, Response<PostData> response) {
                            if (response.body() != null) {
                                Log.d("success", "등록 성공");
                            } else {
                                Log.d("error", "오류 발생");

                            }
                        }

                        @Override
                        public void onFailure(Call<PostData> call, Throwable t) {
                            Log.d("server error", t.getMessage());
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            private ImageView productImage;
            private TextView productName;
            private TextView productPrice;
            private ImageButton wroteButton;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.productimage1);
                productName = itemView.findViewById(R.id.productName);
                productPrice = itemView.findViewById(R.id.productPrice);
                wroteButton = itemView.findViewById(R.id.productwrote);
            }
        }
    }
}
