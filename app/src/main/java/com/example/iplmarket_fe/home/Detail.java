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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Detail extends AppCompatActivity {

    private static Socket mSocket;
    private ImageView detailImageView;
    private TextView detailEtName, detailEtPrice, detailEtContent, detailEtDate;
    private Button detailBtnVr, detailBtnBuy;
    private ImageButton detailBtnImage;
    boolean i = true;
    private FileOutputStream outputStream;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 소켓 연결
        try{
            mSocket = IO.socket("Server IP:Port");
            Log.d("Connected", "OK");
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
        mSocket.connect();

        // http
        detailEtName = findViewById(R.id.detailEtName);
        detailEtPrice = findViewById(R.id.detailEtPrice);
        detailEtContent = findViewById(R.id.detailEtContent);
        detailEtDate = findViewById(R.id.detailEtDate);

        // VR Road 버튼 클릭 이벤트 처리
        detailBtnVr = findViewById(R.id.detailBtnVr);
        detailBtnVr.setOnClickListener(view -> {
            mSocket.on("VR modeling Rode", RodeVr);    // 소켓 이벤트
        });

        // 구매 버튼 클릭 이벤트 처리 -> 채팅
        detailBtnBuy = findViewById(R.id.detailBtnBuy);
        detailBtnBuy.setOnClickListener(view -> {
            Intent intent = new Intent(Detail.this, Chatting.class);
            startActivity(intent);
        });

        // 관심 버튼 클릭 이벤트 처리
        detailBtnImage = findViewById(R.id.detailBtnImage);
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