package com.example.iplmarket_fe;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }
    private List<Post> posts = new ArrayList<>();
    private OnItemClickEventListener itemClickEventListener;

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.post_list, parent, false);
        return new ViewHolder(itemView, itemClickEventListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.setPost(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setOnItemClickListener(OnItemClickEventListener listener) {
        itemClickEventListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView textTitle;
        TextView textContent;
        TextView textUserId;
        TextView textCreateDate;

        public ViewHolder(View itemView, OnItemClickEventListener listener) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
            textUserId = itemView.findViewById(R.id.textUserId);
            textCreateDate = itemView.findViewById(R.id.textCreateDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(view, position);
                    }
                }
            });
        }

        public void setPost(Post post) {

            Date postDate = post.getPostRegistDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String postCreateDate = format.format(postDate);

            textTitle.setText(post.getPostTitle());
            textContent.setText(post.getPostContent());
            textUserId.setText(post.getUserId());
            textCreateDate.setText(postCreateDate);
        }
    }

    private void sendProductNumberToServer(int productNumber) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.46:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceApi service = retrofit.create(ServiceApi.class);

        // 상품 번호만 전달
        Call<ApiResponse> call = service.sendProductData(productNumber);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null) {
                    // 성공적으로 응답을 받았을 경우 처리
                    Log.d("ApiCall", "상품 정보 전송 성공");
                } else {
                    // 서버 응답이 실패한 경우 처리
                    Log.d("ApiCall", "상품 정보 전송 실패");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // 네트워크 실패 처리
                Log.d("error", t.getMessage());
            }
        });
    }

}
