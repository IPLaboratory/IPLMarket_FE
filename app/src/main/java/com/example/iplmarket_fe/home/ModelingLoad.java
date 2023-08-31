package com.example.iplmarket_fe.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

public class ModelingLoad extends AppCompatActivity {
    private static Socket mSocket;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 게시글 번호를 가져오는 코드 (http)
        int postNum = getIntent().getIntExtra("postNum", -1);

        // 실제로 존재하는 게시글 번호가 있을 경우(-1이 아닐 경우)
        if (postNum != -1) {
            // 서버로 게시글 번호와 함께 요청 보내기
            sendModelingLoadRequest(postNum);

            // 소켓 연결
            try {
                mSocket = SocketManager.getSocket();
                mSocket.on("Modeling File Rode", LodeFile);
                mSocket.connect();
                Log.d("ModelingLoad", "Socket 연결 성공");
            } catch (URISyntaxException e) {
                Log.e("ModelingLoad", "Socket 연결 실패");
                throw new RuntimeException(e);
            }

            // 6개의 파일 요청
            for (int i = 1; i <= 6; i++) {
                JSONObject requestObject = new JSONObject();
                try {
                    requestObject.put("fileIndex", i);
                    mSocket.emit("Request Modeling File", requestObject);
                    Log.d("ModelingLoad", "모델링 파일 요청 - Index: " + i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("ModelingLoad", "게시글 번호가 올바르지 않습니다.");
        }
        Toast.makeText(getApplicationContext(), "잠시만 기다려주세요...", Toast.LENGTH_LONG).show();
    }


    // 서버로 VR LOAD 요청을 보내는 메서드
    private void sendModelingLoadRequest(int postNum) {
        // JSON 데이터 생성
        JSONObject data = new JSONObject();
        try {
            data.put("postNum", postNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 소켓 통신으로 obj파일 수신
    private Emitter.Listener LodeFile = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(Object... args) {
            // 전달 받은 원본 데이터
            JSONObject data = (JSONObject) args[0];

            try {
                boolean success = data.getBoolean("success");

                if (success) { // 성공적인 전달
                    String base64File = data.getString("base64File");
                    int fileIndex = data.getInt("fileIndex");

                    saveModelingData(base64File, "modeling" + fileIndex + ".obj");
                } else {
                    Log.e("Modeling File Response", "Err : " + data.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    // 수신한 obj 파일 디코딩 후 저장
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveModelingData(String base64File, String fileName){
        try {
            byte[] fileData = Base64.getDecoder().decode(base64File);

            // 폴더 경로
            String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "modeling_files";

            // 폴더 생성
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 파일 경로
            String filePath = folderPath + File.separator + fileName;

            // 저장
            File file = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileData);
            outputStream.close();

            Log.d("Modeling File", "Download Complete - Path: " + filePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}