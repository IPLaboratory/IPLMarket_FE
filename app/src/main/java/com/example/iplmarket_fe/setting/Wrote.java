package com.example.iplmarket_fe.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.SocketManager;
import com.example.iplmarket_fe.create.CreateFrag;
import com.example.iplmarket_fe.home.Detail;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.request.PostListRequest;
import com.example.iplmarket_fe.server.response.PostResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Wrote extends AppCompatActivity {

    private List<ProductItem> productList;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ServiceApi serviceApi;
    private String userId; // 사용자 아이디 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrote);

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.serverIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(ServiceApi.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // 사용자 아이디 가져오기 (로그인 시 호출)
        userId = getUserId();

        fetchProductData(userId);
    }

    private void fetchProductData(String userId) {
        PostListRequest request = new PostListRequest(userId);
        Call<List<PostResponse>> call = serviceApi.getUserPosts(request);
        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful()) {
                    List<PostResponse> postList = response.body();
                    for (PostResponse post : postList) {
                        ProductItem productItem = new ProductItem(
                                post.getPostThumbnail(),
                                post.getPostTitle(),
                                post.getPrice(),
                                post.getNum()
                        );
                        productList.add(productItem);
                    }
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Wrote Activity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                Log.e("Wrote Activity", "Error: " + t.getMessage());
            }
        });
    }

    public static Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private class ProductItem {
        private String thumbnail; // 이미지 URL을 위해 String으로 변경
        private String title;
        private String price;
        private int postNum;

        public ProductItem(String thumbnail, String title, String price, int postNum) {
            this.thumbnail = thumbnail;
            this.title = title;
            this.price = price;
            this.postNum = postNum;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getTitle() {
            return title;
        }

        public String getPrice() {
            return price;
        }

        public int getPostNum() {
            return postNum;
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

            // 이미지 URL을 비트맵으로 변환하여 이미지 뷰에 설정
            Bitmap thumbnailBitmap = Wrote.base64ToBitmap(product.getThumbnail());
            holder.productThumbnail.setImageBitmap(thumbnailBitmap);

            holder.productTitle.setText(product.getTitle());
            holder.productPrice.setText(product.getPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 해당 상품의 게시글 번호를 가져와서 상세 페이지로 전환
                    int clickedPostNum = product.getPostNum();
                    openDetailPage(clickedPostNum);

                    // 게시글 번호를 서버로 전송
                    sendClickedPostNum(clickedPostNum);
                }
            });
        }

        // 새로운 메서드 추가하여 상세 페이지로 전환
        private void openDetailPage(int postNum) {
            Intent intent = new Intent(Wrote.this, Detail.class);
            intent.putExtra("postNum", postNum);
            startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder {
            private ImageView productThumbnail;
            private TextView productTitle;
            private TextView productPrice;
            private ImageButton wroteButton;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                productThumbnail = itemView.findViewById(R.id.productimage1);
                productTitle = itemView.findViewById(R.id.productTitle);
                productPrice = itemView.findViewById(R.id.productPrice);
                wroteButton = itemView.findViewById(R.id.productwrote);

                // productwrote 버튼 클릭 이벤트 처리
                wroteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int clickedPostNum = productList.get(getAdapterPosition()).getPostNum();
                        openCreateFrag(clickedPostNum);
                    }
                });
            }
        }

        private void openCreateFrag(int postNum) {
            Intent intent = new Intent(Wrote.this, CreateFrag.class);
            intent.putExtra("postNum", postNum);
            startActivity(intent);
        }

    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("user_id", "");
    }

    private void sendClickedPostNum(int postNum) {
        try {
            // 소켓 연결
            Socket mSocket = SocketManager.getSocket();
            mSocket.connect();

            // JSON 데이터 생성
            JSONObject data = new JSONObject();
            data.put("postNum", postNum);

            // 소켓 이벤트 발송
            mSocket.emit("clicked_post_num", data);

            // 소켓 연결 종료
            mSocket.disconnect();
        } catch (URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }
}
