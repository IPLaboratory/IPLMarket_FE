package com.example.iplmarket_fe.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.server.request.PostDetailRequest;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.response.PostDetailResponse;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Detail extends AppCompatActivity {

    private Button detail_btn_vr, detail_btn_buy;
    private ImageButton detail_btn_image;
    boolean i = true;
    private ServiceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.14:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(ServiceApi.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView thumbnail = findViewById(R.id.detail_image);
        TextView title = findViewById(R.id.detail_et_name);
        TextView price = findViewById(R.id.detail_et_price);
        TextView nickname = findViewById(R.id.detail_et_nick);
        TextView date = findViewById(R.id.detail_et_date);
        TextView description = findViewById(R.id.detail_et_ex);

        int postNum = getIntent().getExtras().getInt("postNum");
        PostDetailRequest postDetailRequest = new PostDetailRequest(postNum, "???");
        Call<PostDetailResponse> call = serviceApi.getPostDetail(postDetailRequest);


        call.enqueue(new Callback<PostDetailResponse>() {
            @Override
            public void onResponse(Call<PostDetailResponse> call, Response<PostDetailResponse> response) {
                if(response.isSuccessful()) {
                    PostDetailResponse postDetailResponse = response.body();

                    Date registDate = postDetailResponse.getRegistDate();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                    String dateRegist = formatter.format(registDate);

                    title.setText(postDetailResponse.getTitle());
                    price.setText(postDetailResponse.getPrice());
                    nickname.setText(postDetailResponse.getUserId());
                    date.setText(dateRegist);
                    description.setText(postDetailResponse.getContent());
                    Bitmap thumbnailImage = base64ToBitmap(postDetailResponse.getOriginalImage());
                    thumbnail.setImageBitmap(thumbnailImage);
                } else {
                    // 오류 응답 처리
                    Toast.makeText(Detail.this, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostDetailResponse> call, Throwable t) {
                // 네트워크 오류 처리
                Log.d("error", t.getMessage());
            }
        });

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
    // base64 -> Bitmap
    private Bitmap base64ToBitmap(String base64){
        byte[] decodedString = Base64.getDecoder().decode(base64);

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}