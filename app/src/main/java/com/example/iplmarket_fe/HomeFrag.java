package com.example.iplmarket_fe;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFrag extends Fragment {

    private PostAdapter adapter;
    private ServiceApi service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.homefrag, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recyclerView);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AAAAAAA", view.toString());
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostAdapter();

        recyclerView.setAdapter(adapter);

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.46:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ServiceApi.class);

        List<Post> posts = fetchPosts();

        adapter.setPosts(posts);
        adapter.setOnItemClickListener(new PostAdapter.OnItemClickEventListener() {
            @Override
            public void onItemClick(View view, int position) {
                int postId = posts.get(position).getNum();
                Log.d("test 123", "Clicked item's post id: " + postId);
            }
        });

        return fragmentView;
    }

    private List<Post> fetchPosts() {
        List<Post> postList = new ArrayList<>();
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "phs0345", 1));
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "phs", 2));
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "phs0", 4));
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "phs03", 6));
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "phs034", 7));
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "hs0345", 8));
        postList.add(new Post("PostTitle", "PostContent", "PostThumbnail", new Date(), "s0345", 9));

        return postList;

        // 사용자 정보 객체 생성
        // UserData userData = new UserData("phs0345");

        // Call<List<Post>> call = service.getPosts(userData);

        /*call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.body() != null) {
                    List<Post> posts = response.body();

                    // 받아온 데이터를 어댑터에 설정
                    adapter.setPosts(posts);

                    // 각각의 게시물에 대해 썸네일 이미지를 디코딩하여 저장
                    for (Post post : posts) {
                        saveTestData(post.getPostThumbnail(), "thumbnail_" + post.getNum() + ".jpg");

                        int productId = post.getNum();
                        adapter.addItem(new Product(post.getPostTitle(), post.getPostContent(), post.getUser_id(), productId));

                    }

                } else {
                    // 서버 응답이 실패한 경우 처리
                    Toast.makeText(getContext(), "서버 응답 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // 네트워크 실패 처리
                // Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.d("error", t.getMessage());
            }
        });*/
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
}

