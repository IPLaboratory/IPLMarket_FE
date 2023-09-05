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

        // 게시글 번호를 가져오는 코드
        int postNum = getIntent().getIntExtra("postNum", -1);
        Log.d("ModelingLoad", "게시글 번호: " + postNum); // 게시글 번호 출력

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


    // 서버로 AR LOAD 요청을 보내는 메서드
    private void sendModelingLoadRequest(int postNum) {
        // JSON 데이터 생성
        JSONObject data = new JSONObject();
        try {
            data.put("postNum", postNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
