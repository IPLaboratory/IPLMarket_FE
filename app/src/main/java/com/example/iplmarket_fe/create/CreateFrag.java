package com.example.iplmarket_fe.create;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.SocketManager;
import com.example.iplmarket_fe.server.PostData;
import com.example.iplmarket_fe.server.ServiceApi;
import com.example.iplmarket_fe.server.response.WriteContentResponse;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import android.util.Base64;
import java.util.Date;
import java.util.UUID;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class CreateFrag extends Fragment {

    private static Socket mSocket;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 200;
    private static final int BUFFER_SIZE = 65536 * 2;
    private EditText createName, createPrice, createContent;
    private TextView lenName, lenPrice, lenContent, createDate;
    private ImageView createImageView;
    private VideoView createVideoView;
    private Button createBtnGallery, createBtnCamera, createBtnUpload;
    private Uri imageUri, savedVideoUri;
    private String videoFilename;
    private String userId; // 사용자 아이디 변수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.createfrag, container, false);

        try {
            mSocket = SocketManager.getSocket();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("video save complete", videoComplete);

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

        // 이미지 가져오기 버튼 클릭 이벤트 처리
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
        createBtnCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                openCamera();
            }
        });

        // 등록 날짜 설정
        createDate = fragmentView.findViewById(R.id.createDate);
        setCurrentDate();

        // 등록 버튼 클릭 시 정보 저장
        createBtnUpload = fragmentView.findViewById(R.id.createBtnUpload);
        createBtnUpload.setOnClickListener(view -> {
            String title = createName.getText().toString();
            String content = createContent.getText().toString();
            String price = createPrice.getText().toString();

            // 사용자가 입력한 데이터가 하나라도 비어있다면 서버로의 전송을 막음
            if (title.isEmpty() || content.isEmpty() || price.isEmpty() || imageUri == null || savedVideoUri == null) {
                Toast.makeText(getActivity(), "모든 필드를 입력하고 이미지, 비디오를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                // 소켓 연결
                mSocket.connect();
                sendVideo();
                Toast.makeText(getActivity(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
            }
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    // 갤러리에서 이미지 가져오기 결과 처리
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        createImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
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

    // 비트맵 이미지를 Base64로 변환하는 메서드
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  // 이미지 압축
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
            savedVideoUri = data.getData();
            createVideoView.setVideoURI(savedVideoUri);
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

    // 동영상 uri를 file path로 변환
    private String uriToFilePath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null );
        assert cursor != null;

        cursor.moveToNext();
        int index = cursor.getColumnIndex("_data");
        String path = cursor.getString(index);
        cursor.close();

        return path;
    }


    // 동영상 파일을 서버에 전송
    private void sendVideo() {
        // 가져올 파일 경로
        Log.d("savedVideoUri", savedVideoUri.getEncodedPath());
        String savedVideoPath = uriToFilePath(savedVideoUri);
        Log.d("savedPath", savedVideoPath);

        File file = new File(savedVideoPath);
        String prompt = "a drawers";
        long fileLength = file.length();

        Thread thread = new Thread(() -> {
            try {
                // 파일 데이터 읽기
                FileInputStream fis = new FileInputStream(file);

                int count = 0;
                byte[] buffer = new byte[BUFFER_SIZE];

                while (fis.read(buffer) >= 0) {
                    String encodedPart = java.util.Base64.getEncoder().encodeToString(buffer);

                    count += BUFFER_SIZE;
                    JSONObject data = new JSONObject();
                    try{
                        data.put("total", fileLength);
                        data.put("count", Math.min(count, fileLength));
                        data.put("data", encodedPart);
                        data.put("prompt", prompt);

                        mSocket.emit("sendVideo", data);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    Log.d("FileSend", count + "/" + fileLength);

                    Thread.sleep(50);
                }

                fis.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Log.d("FileSend", "success!!!!!");
    }

    Emitter.Listener videoComplete = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            Boolean success = data.getBoolean("success");
            String filename = data.getString("fileName");
            if (success) {
                videoFilename = filename;

                String title = createName.getText().toString();
                String content = createContent.getText().toString();
                String price = createPrice.getText().toString();
                String imageData = "";
                String imageName = "";
                String videoName = "";

                // 이미지가 선택되었을 때만 이미지 데이터를 생성
                if (imageUri != null) {
                    String imagePath = getImagePathFromUri(imageUri);
                    if (imagePath != null) {
                        imageName = UUID.randomUUID().toString() + ".jpg";
                        videoName = videoFilename + ".mp4";
                        Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
                        imageData = bitmapToBase64(imageBitmap); // 이미지를 Base64로 변환하여 imageData에 저장
                    }
                }

                // 사용자 아이디 가져오기
                userId = getUserID();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BuildConfig.serverIP)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceApi serviceApi = retrofit.create(ServiceApi.class);

                PostData sendData = new PostData(title, content, price, imageName, videoName, imageData, userId);
                Call<WriteContentResponse> response = serviceApi.WroteResponse(sendData);

                response.enqueue(new Callback<WriteContentResponse>() {
                    @Override
                    public void onResponse(Call<WriteContentResponse> call, Response<WriteContentResponse> response) {
                        if (response != null) {
                            WriteContentResponse mResult = response.body();
                            if (mResult != null && mResult.isSuccess()) {
                                Log.d("게시물 등록", "게시물 등록 성공");
                            } else {
                                Log.e("게시물 등록", "실패");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<WriteContentResponse> call, Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });

                mSocket.disconnect();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    };

    // 현재 날짜를 설정하는 도우미 메서드
    private void setCurrentDate() {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDataFormat.format(new Date());
        createDate.setText(currentDate);
    };

    // 사용자 아이디 가져오기
    private String getUserID() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", ""); // 두 번째 인자는 기본값 (기본값 없을 시 빈 문자열)
    }
}