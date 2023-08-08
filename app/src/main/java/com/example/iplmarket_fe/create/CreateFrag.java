package com.example.iplmarket_fe.create;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.iplmarket_fe.R;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import java.io.IOException;

public class CreateFrag extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 2;

    private Uri cameraImageUri;


    private Button create_btn_gallery, create_btn_camera, create_btn_enroll;

    ImageView create_imageView1, create_imageView2;
    Uri uri;

    private EditText create_name, create_price, create_ex;
    private TextView len_name, len_price, len_ex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.createfrag, container, false);

        create_name = fragmentView.findViewById(R.id.create_name);
        create_price = fragmentView.findViewById(R.id.create_price);
        create_ex = fragmentView.findViewById(R.id.create_ex);

        len_name = fragmentView.findViewById(R.id.len_name);
        len_price = fragmentView.findViewById(R.id.len_price);
        len_ex = fragmentView.findViewById(R.id.len_ex);


        //EditText 글자 수 제한
        create_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                len_name.setText(s.length()+"/30");
            }
        });

        create_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                len_price.setText(s.length()+"/8");
            }
        });

        create_ex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                len_ex.setText(s.length()+"/300");
            }
        });

        // Enter key 방지
        create_name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        create_price.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });


        // 이미지 가져오기
        create_imageView1 = fragmentView.findViewById(R.id.create_imageView1);
        Button selectImageBtn = fragmentView.findViewById(R.id.create_btn_gallery);

        selectImageBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityResult.launch(intent);
        });


        // 카메라 버튼 클릭 이벤트 처리
        create_btn_camera = fragmentView.findViewById(R.id.create_btn_camera);
        create_imageView2 = fragmentView.findViewById(R.id.create_imageView2);

        create_btn_camera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                OpenCamera();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);    // 권한 요청
            }
        });

        /*
        서버 연결
        등록 버튼 클릭 시 정보 저장
        et_name = view.findViewById(R.id.et_name);
        et_price = view.findViewById(R.id.et_price);
        et_ex = view.findViewById(R.id.et_ex);

        btn_enroll.setOnClickListener(view -> {
            // EditText에 현재 입력되어있는 값을 가져옴
            String product_name = et_name.getText().toString();
            String price = et_price.getText().toString();
            String product_add = et_ex.getText().toString();
        });

        상품 리스트(HomeFrag), 내가 쓴 글 리스트(Wrote)에 저장
         */

        return fragmentView;
    }


    // 갤러리에서 이미지 가져오기
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK&& result.getData() != null) {
                        uri = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                            create_imageView1.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    // 카메라 실행 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // 권한 허용
                OpenCamera();
            }
        }
    }

    // 카메라 실행
    private void OpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
    }

    // 카메라 실행 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==CAMERA_REQUEST_CODE&& resultCode == AppCompatActivity.RESULT_OK&& data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            create_imageView2.setImageBitmap(photo);
        }
    }
}
