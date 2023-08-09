package com.example.iplmarket_fe.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iplmarket_fe.R;

import java.util.ArrayList;
import java.util.List;

public class PageMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public static class ProductPage {
    }

    public static class WishList extends AppCompatActivity {

        private List<Product> productList;
        private RecyclerView recyclerView;
        private ProductAdapter productAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_wishlist);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            productList = new ArrayList<>();
            productAdapter = new ProductAdapter(productList);
            recyclerView.setAdapter(productAdapter);

            // 예시 데이터 추가
            productList.add(new Product(R.drawable.product1, "상품 1", "10,000₩", false));
            productList.add(new Product(R.drawable.product2, "상품 2", "20,000₩", false));
            productList.add(new Product(R.drawable.product3, "상품 3", "30,000₩", false));
            productList.add(new Product(R.drawable.product4, "상품 4", "40,000₩", false));
            productList.add(new Product(R.drawable.product5, "상품 5", "50,000₩", false));
            productList.add(new Product(R.drawable.product6, "상품 6", "60,000₩", false));
            productList.add(new Product(R.drawable.product7, "상품 7", "70,000₩", false));
            productList.add(new Product(R.drawable.product8, "상품 8", "80,000₩", false));
            productList.add(new Product(R.drawable.product9, "상품 9", "90,000₩", false));
            productList.add(new Product(R.drawable.product10, "상품 10", "100,000₩", false));

            productAdapter.notifyDataSetChanged();
        }

        private class Product {
            private int imageResource;
            private String name;
            private String price;
            private boolean liked;

            public Product(int imageResource, String name, String price, boolean liked) {
                this.imageResource = imageResource;
                this.name = name;
                this.price = price;
                this.liked = liked;
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

            public boolean isLiked() {
                return liked;
            }

            public void setLiked(boolean liked) {
                this.liked = liked;
            }
        }

        private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
            private List<Product> productList;

            public ProductAdapter(List<Product> productList) {
                this.productList = productList;
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new ProductViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
                final Product product = productList.get(position);

                holder.productImage.setImageResource(product.getImageResource());
                holder.productName.setText(product.getName());
                holder.productPrice.setText(product.getPrice());

                if (product.isLiked()) {
                    holder.likeButton.setImageResource(R.drawable.heart_after);
                } else {
                    holder.likeButton.setImageResource(R.drawable.heart_before);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ProductPage로 이동
                        Intent intent = new Intent(WishList.this, ProductPage.class);
                        startActivity(intent);
                    }
                });

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (product.isLiked()) {
                            holder.likeButton.setImageResource(R.drawable.heart_before);
                            product.setLiked(false);
                        } else {
                            holder.likeButton.setImageResource(R.drawable.heart_after);
                            product.setLiked(true);
                        }
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
                private ImageButton likeButton;

                public ProductViewHolder(@NonNull View itemView) {
                    super(itemView);
                    productImage = itemView.findViewById(R.id.productimage1);
                    productName = itemView.findViewById(R.id.productName);
                    productPrice = itemView.findViewById(R.id.productPrice);
                    likeButton = itemView.findViewById(R.id.likeButton);
                }
            }
        }
    }
}