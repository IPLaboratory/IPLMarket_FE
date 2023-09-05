package com.example.iplmarket_fe.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.request.ArLoadRequest;
import com.example.iplmarket_fe.server.response.ArLoadResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModelingLoad extends AppCompatActivity {

    ServiceApi serviceApi;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.serverIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(ServiceApi.class);

        // 게시글 번호를 가져오는 코드
        int postNum = HomeFrag.getNum();
        Log.d("ModelingLoad", "게시글 번호: " + postNum); // 게시글 번호 출력

        // 실제로 존재하는 게시글 번호가 있을 경우(-1이 아닐 경우)
        if (postNum != -1) {
            // 서버로 게시글 번호와 함께 요청 보내기
            sendModelingLoadRequest(postNum);
        } else {
            Log.e("ModelingLoad", "게시글 번호가 올바르지 않습니다.");
        }
        Toast.makeText(getApplicationContext(), "잠시만 기다려주세요...", Toast.LENGTH_LONG).show();
    }

    // 서버로 AR LOAD 요청을 보내는 메서드
    private void sendModelingLoadRequest(int postNum) {
        // JSON 데이터 생성
        ArLoadRequest arLoadRequest = new ArLoadRequest(postNum);

        serviceApi.getArLoad(arLoadRequest).enqueue(new Callback<ArLoadResponse>() {
            @Override
            public void onResponse(Call<ArLoadResponse> call, Response<ArLoadResponse> response) {
                if (response.isSuccessful()) {
                    ArLoadResponse arLoadResponse = response.body();
                    if (arLoadResponse != null) {
                        // 서버 응답을 처리하는 함수 호출
                        handleModelingLoadResponse(arLoadResponse);
                    } else {
                        Log.e("ModelingLoad", "응답 데이터가 비어 있습니다.");
                    }
                } else {
                    Log.e("ModelingLoad", "서버 응답에 실패했습니다. 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArLoadResponse> call, Throwable t) {
                Log.e("ModelingLoad", "서버 요청에 실패했습니다.", t);
            }
        });
    }

    // 서버 응답 처리 및 파일 저장
    private void handleModelingLoadResponse(ArLoadResponse response) {
        boolean success = response.isSuccess();
        if (success) {
            String directory = response.getDirectory();
            createDirectoryIfNotExists(directory);

            String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            // 각 파일을 base64로 디코딩하여 저장
            saveFileFromBase64(folderPath, "mesh.mtl", response.getMtl());
            saveFileFromBase64(folderPath, "mesh.obj", response.getObj());
            saveFileFromBase64(folderPath, "probe.hdr", response.getHdr());
            saveFileFromBase64(folderPath, "texture_kd.png", response.getKdPng());
            saveFileFromBase64(folderPath, "texture_ks.png", response.getKsPng());
            saveFileFromBase64(folderPath, "texture_n.png", response.getnPng());

            // TODO: Start Unity Activity
        } else {
            String errorMessage = response.getError();
            if ("Not Exist Directory".equals(errorMessage)) {
                Log.d("NotModeling", "아직 모델링이 완료되지 않았습니다.");
            } else {
                Log.d("Error", "에러 발생: " + errorMessage);
            }
        }
    }

    // 디렉토리 생성
    private void createDirectoryIfNotExists(String directory) {
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + directory;

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    // base64로 디코딩하여 파일 저장
    private void saveFileFromBase64(String directory, String fileName, String base64File) {
        try {
            byte[] fileData = Base64.getDecoder().decode(base64File.getBytes(StandardCharsets.UTF_8));

            String filePath = directory + File.separator + fileName;

            File file = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileData);
            outputStream.close();

            Log.d("Modeling File", "Download Complete - Path: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
