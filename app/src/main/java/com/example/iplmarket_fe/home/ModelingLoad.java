package com.example.iplmarket_fe.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.request.ArLoadRequest;
import com.example.iplmarket_fe.server.response.ArLoadResponse;
import com.unity3d.player.UnityPlayerActivity;

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
import android.content.Intent;

public class ModelingLoad extends AppCompatActivity {

    ServiceApi serviceApi;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // Assuming the layout is activity_detail.xml

        // Find AR Load button and set click listener
        Button arLoadButton = findViewById(R.id.detail_btn_ar);
        arLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUnityActivity();
                {
                    Intent intent = new Intent(ModelingLoad.this, UnityPlayerActivity.class);
                    intent.putExtra("started_from_button", true);  // 이 값을 추가
                    startActivity(intent);
                }
            }
        });

        // Retrofit instance creation
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.serverIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(ServiceApi.class);

        // Get post number
        int postNum = HomeFrag.getNum();
        Log.d("ModelingLoad", "Post number: " + postNum);

        // If a valid post number exists
        if (postNum != -1) {
            sendModelingLoadRequest(postNum);
        } else {
            Log.e("ModelingLoad", "The post number is invalid.");
        }

    }

    private void startUnityActivity() {
        Intent intent = new Intent(ModelingLoad.this, UnityPlayerActivity.class);
        startActivity(intent);
    }

    private void sendModelingLoadRequest(int postNum) {
        ArLoadRequest arLoadRequest = new ArLoadRequest(postNum);
        serviceApi.getArLoad(arLoadRequest).enqueue(new Callback<ArLoadResponse>() {
            @Override
            public void onResponse(Call<ArLoadResponse> call, Response<ArLoadResponse> response) {
                Log.d("gh,,gjh,hgj,ghj", String.valueOf(response.isSuccessful()));
                if (response.isSuccessful()) {
                    ArLoadResponse arLoadResponse = response.body();
                    Log.d("gh,,gjh,hgj,ghj", String.valueOf(arLoadResponse.isSuccess()));
                    if (arLoadResponse.isSuccess()) {
                        handleModelingLoadResponse(arLoadResponse);
                    } else {
                        Toast.makeText(getApplicationContext(), "모델을 생성 중 입니다... 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("ModelingLoad", "Failed to get a server response. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArLoadResponse> call, Throwable t) {
                Log.e("ModelingLoad", "Failed to request the server.", t);
            }
        });
    }

    private void handleModelingLoadResponse(ArLoadResponse response) {
        boolean success = response.isSuccess();
        if (success) {
            String directory = response.getDirectory();
            createDirectoryIfNotExists(directory);

            String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            // Save each file by decoding it from base64
            saveFileFromBase64(folderPath, "mesh.mtl", response.getMtl());
            saveFileFromBase64(folderPath, "mesh.obj", response.getObj());
            saveFileFromBase64(folderPath, "probe.hdr", response.getHdr());
            saveFileFromBase64(folderPath, "texture_kd.png", response.getKdPng());
            saveFileFromBase64(folderPath, "texture_ks.png", response.getKsPng());
            saveFileFromBase64(folderPath, "texture_n.png", response.getnPng());

            // Start UnityPlayerActivity when AR modeling is loaded
            startUnityActivity();
        } else {
            String errorMessage = response.getError();
            if ("Not Exist Directory".equals(errorMessage)) {
                Log.d("NotModeling", "Modeling has not yet been completed.");
            } else {
                Log.d("Error", "An error occurred: " + errorMessage);
            }
        }
    }

    private void createDirectoryIfNotExists(String directory) {
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + directory;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void saveFileFromBase64(String directory, String fileName, String base64File) {
        try {
            byte[] fileData = Base64.getDecoder().decode(base64File.getBytes(StandardCharsets.UTF_8));
            String filePath = directory + File.separator + fileName;
            File file = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileData);
            outputStream.close();
            Log.d("ModelingFile", "Download complete - Path: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
