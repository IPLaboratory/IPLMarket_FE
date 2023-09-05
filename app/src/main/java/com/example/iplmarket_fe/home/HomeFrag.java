package com.example.iplmarket_fe.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iplmarket_fe.BuildConfig;
import com.example.iplmarket_fe.server.response.PostResponse;
import com.example.iplmarket_fe.R;
import com.example.iplmarket_fe.server.ServiceApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFrag extends Fragment {

    private PostAdapter adapter;
    private ServiceApi serviceApi;
    private static int postNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.homefrag, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostAdapter();

        recyclerView.setAdapter(adapter);

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.serverIP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        serviceApi = retrofit.create(ServiceApi.class);

        fetchPosts();

        // 아이템 클릭 리스너 설정
        adapter.setOnItemClickListener((View view, int position) -> {
            // 클릭한 아이템의 번호를 가져와서 서버로 전달하고 Detail 화면을 띄워줌
            postNum = adapter.getPostNumAt(position);
            Log.d("product id", String.valueOf(postNum));
            openDetailActivity(postNum);
        });

        return fragmentView;
    }

    // 게시물 번호 리턴
    public static int getNum() {
        return postNum;
    }

    private void fetchPosts() {
        // 사용자 정보 객체 생성
//        PostListRequest postListRequest = new PostListRequest("aaa");
//        Call<List<PostResponse>> call = serviceApi.getUserPosts(postListRequest);
        Call<List<PostResponse>> call = serviceApi.getAllPosts();


        call.enqueue(new Callback<List<PostResponse>>() {
            @Override
            public void onResponse(Call<List<PostResponse>> call, Response<List<PostResponse>> response) {
                if (response.isSuccessful()) {
                    List<PostResponse> postResponses = response.body();

                    for (PostResponse postResponse : postResponses) {
                        Log.d("a", postResponse.getPostTitle() +", " + postResponse.getPostContent() +", " + postResponse.getPrice() + " " + postResponse.getUserId() + ", " + postResponse.getNum() + ", " + postResponse.getPostRegistDate());
                    }


                    // 받아온 데이터를 어댑터에 설정
                    adapter.setPosts(postResponses);

                    // 각각의 게시물에 대해 썸네일 이미지를 디코딩하여 저장
                    for (PostResponse postResponse : postResponses) {
                        Log.d("디코딩", postResponse.getPostThumbnail());
                        // saveTestData(post.getPostThumbnail(), "thumbnail_" + post.getNum() + ".jpg");
                    }
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Toast.makeText(getContext(), "서버 응답 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostResponse>> call, Throwable t) {
                // 네트워크 실패 처리
                Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.d("error", t.getMessage());
            }
        });
    }

    // 수신한 obj 파일 디코딩 후 저장
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveTestData(String base64File, String fileName){
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

            Log.d("File", "Download Complete - Path: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDetailActivity(int postNum) {
        // Detail 액티비티로 화면 전환
        Intent intent = new Intent(getContext(), Detail.class);
        intent.putExtra("postNum", postNum);
        startActivity(intent);
    }
}