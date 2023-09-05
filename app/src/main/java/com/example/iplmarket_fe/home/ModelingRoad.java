package com.example.iplmarket_fe.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ModelingRoad extends AppCompatActivity {
    private static Socket mSocket;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 소켓 연결
        try {
            mSocket = SocketManager.getSocket();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.on("VR modeling Rode", RodeVr);    // 소켓 이벤트
        mSocket.connect();
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
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileData);
            outputStream.close();

            Log.d("VR modeling", "Download Complete - Path: " + filePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
