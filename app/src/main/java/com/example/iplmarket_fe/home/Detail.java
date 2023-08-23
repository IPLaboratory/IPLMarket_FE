package com.example.iplmarket_fe.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.R;

import com.example.iplmarket_fe.server.request.PostDetailRequest;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.response.PostDetailResponse;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.example.iplmarket_fe.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Detail extends AppCompatActivity {

    private static Socket mSocket;
    private ImageView detailImageView;
    private TextView detailEtName, detailEtPrice, detailEtContent, detailEtDate;
    private Button detailBtnVr, detailBtnBuy;
    private ImageButton detailBtnImage;
    boolean i = true;
    private ServiceApi serviceApi;
    private FileOutputStream outputStream;
    private int num;

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

        // 소켓 연결
        try {
            mSocket = SocketManager.getSocket();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();

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
        detailBtnVr = findViewById(R.id.detail_btn_vr);
        detailBtnVr.setOnClickListener(view -> {
            mSocket.on("VR modeling Rode", RodeVr);    // 소켓 이벤트
        });

        // 구매 버튼 클릭 이벤트 처리 -> 채팅
        detailBtnBuy = findViewById(R.id.detail_btn_buy);
        detailBtnBuy.setOnClickListener(view -> {
            Intent intent = new Intent(Detail.this, Chatting.class);
            startActivity(intent);
        });

        // 관심 버튼 클릭 이벤트 처리
        detailBtnImage = findViewById(R.id.detail_btn_image);
        detailBtnImage.setOnClickListener(view -> {
            if (i) {
                detailBtnImage.setImageResource(R.drawable.heart_after);
                i = false;
                Toast.makeText(this.getApplicationContext(),"관심 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                // 관심 목록 페이지(Wishlist)에 추가
            }else {
                detailBtnImage.setImageResource(R.drawable.heart_before);
                i = true;
                Toast.makeText(this.getApplicationContext(),"관심 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                // 관심 목록 페이지(Wishlist)에서 삭제
            }
        });
    }
    // base64 -> Bitmap
    private Bitmap base64ToBitmap(String base64) {
        byte[] decodedString = Base64.getDecoder().decode(base64);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    // 소켓 통신으로 obj파일 수신
    private Emitter.Listener RodeVr = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(Object... args) {
            // 전달 받은 원본 데이터
            JSONObject data = (JSONObject) args[0];

            try {
                boolean success = data.getBoolean("success");

                if (success){ // 성공적인 전달
                    String base64File = data.getString("base64File");
                    saveVrData(base64File, "modeling.obj");
                } else {
                    Log.e("VR modeling Response", "Err : " + data.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    // 수신한 obj 파일 디코딩 후 저장
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveVrData(String base64File, String fileName){
        try {
            byte[] fileData = Base64.getDecoder().decode(base64File);

            // 저장 경로
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + fileName;

            // 저장
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);
            outputStream.write(fileData);
            outputStream.close();

            // 이미지 파일을 이미지뷰에 출력
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap != null) {
                detailImageView.setImageBitmap(bitmap);
            }

            Log.d("VR modeling", "Download Complete - Path: " + filePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}