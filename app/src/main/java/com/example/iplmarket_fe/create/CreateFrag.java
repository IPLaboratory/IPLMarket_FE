package com.example.iplmarket_fe.create;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.SocketManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CreateFrag extends Fragment {

    private static Socket mSocket;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 200;
    private EditText createName, createPrice, createContent;
    private TextView lenName, lenPrice, lenContent, createDate;
    private ImageView createImageView;
    private VideoView createVideoView;
    private Button createBtnGallery, createBtnCamera, createBtnUpload;
    Uri imageUri, savedVrPath;
    private int num = 0; // 게시물 번호
    private String savedImagePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.createfrag, container, false);

        createName = fragmentView.findViewById(R.id.createName);
        createPrice = fragmentView.findViewById(R.id.createPrice);
        createContent = fragmentView.findViewById(R.id.createContent);

        lenName = fragmentView.findViewById(R.id.lenName);
        lenPrice = fragmentView.findViewById(R.id.lenPrice);
        lenContent = fragmentView.findViewById(R.id.lenContent);

        // EditText 글자 수 제한
        initializeEditTextWithLimit(fragmentView, R.id.createName, R.id.lenName, 30);
        initializeEditTextWithLimit(fragmentView, R.id.createPrice, R.id.lenPrice, 8);
        initializeEditTextWithLimit(fragmentView, R.id.createContent, R.id.lenContent, 300);

        // 이미지 가져오기
        createImageView = fragmentView.findViewById(R.id.createImageView);
        createBtnGallery = fragmentView.findViewById(R.id.createBtnGallery);
        createBtnGallery.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityResult.launch(intent);
        });

        // 카메라 버튼 클릭 이벤트 처리
        createVideoView = fragmentView.findViewById(R.id.createVideoView);
        createBtnCamera = fragmentView.findViewById(R.id.createBtnCamera);
        createBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    openCamera();
                }
            }
        });

        // 등록 날짜 설정
        createDate = fragmentView.findViewById(R.id.createDate);
        setCurrentDate();

        // 등록 버튼 클릭 시 정보 저장
        createBtnUpload = fragmentView.findViewById(R.id.createBtnUpload);
        createBtnUpload.setOnClickListener(view -> {

            // http


            // 소켓 연결
            try {
                mSocket = SocketManager.getSocket();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            mSocket.on("product vr", sendVrModel);
            mSocket.connect();

            Toast.makeText(getActivity(),"등록되었습니다.", Toast.LENGTH_SHORT).show();
        });

        return fragmentView;
    }

    // EditText 초기화 및 글자 수 제한 설정을 위한 메서드
    private void initializeEditTextWithLimit(View rootView, int editTextId, int counterTextViewId, int maxLength) {
        EditText editText = rootView.findViewById(editTextId);
        TextView counterTextView = rootView.findViewById(counterTextViewId);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                counterTextView.setText(s.length() + "/" + maxLength);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    // 갤러리에서 이미지 가져오기
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK&& result.getData() != null) {
                        imageUri = result.getData().getData();
                        savedImagePath = getImagePathFromUri(imageUri);

                        try {
                            if (savedImagePath != null) {
                                File imageFile = new File(savedImagePath);
                                if (imageFile.exists()) {
                                    createImageView.setImageURI(Uri.fromFile(imageFile));
                                } else {
                                    Log.e("Image Error", "Image file does not exist.");
                                }
                            } else {
                                Log.e("Image Error", "Saved image path is null.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    // uri에서 이미지 파일 경로를 얻는 메서드
    private String getImagePathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }


    // 카메라 실행
    private void openCamera() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }

    // 촬영한 영상 화면에 출력
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            savedVrPath = data.getData();
            createVideoView.setVideoURI(savedVrPath);
            createVideoView.start();
        }
    }

    // 카메라 권한 요청 여부
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getActivity(), "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 현재 날짜를 설정하는 도우미 메서드
    private void setCurrentDate() {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDataFormat.format(new Date());
        createDate.setText(currentDate);
    }

    // VR 파일을 서버에 전송
    private Emitter.Listener sendVrModel = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(Object... args) {
            // 가져올 파일 경로
            File file = new File(savedVrPath + File.separator + "productVr.obj");


            try {
                // 파일 데이터 읽기
                FileInputStream fis;
                fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1){
                    bos.write(buffer, 0, bytesRead);
                }

                fis.close();
                bos.close();

                // 파일 Base64 인코딩
                byte[] fileBytes = bos.toByteArray();
                String encodedFile = Base64.getEncoder().encodeToString(fileBytes);

                // 청크 설정 (한 번씩 보낼 데이터 크기) -> 실제 연결시 500000번으로 줄일것
                int chunk = 800000;
                int numOfChunks = (int)Math.ceil((double)encodedFile.length() / chunk);
                int startIdx = 0, endIdx;

                // 청크 개수 만큼 반복
                for(int i=0; i<numOfChunks; i++){
                    endIdx = Math.min(startIdx + chunk, encodedFile.length());
                    JSONObject data = new JSONObject();

                    // json 파일에 데이터 전체 크기, 현재 보낸 데이터 크기, 데이터 전송
                    try{
                        data.put("total", encodedFile.length());
                        data.put("count", endIdx);
                        data.put("data", encodedFile.substring(startIdx, endIdx));

                        mSocket.emit("sendFile", data);

                        // Thread.sleep(100); // 소켓 이중 연결됨
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    Log.d("Send Data ... ", ""+ Math.round(((double)endIdx / encodedFile.length() * 100.0) * 100) / 100.0 + "%");
                    startIdx += chunk; // 다음 보낼 데이터 이어서 전송
                }
                Log.d("VrUpload", "VR File sent successfully");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };
}
