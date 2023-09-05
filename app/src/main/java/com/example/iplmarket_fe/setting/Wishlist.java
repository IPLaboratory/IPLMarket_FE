package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.home.Detail;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity {

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
                    // Navigate to ProductPage
                    Intent intent = new Intent(Wishlist.this, Detail.class);
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
