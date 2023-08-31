package com.example.iplmarket_fe.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iplmarket_fe.server.response.PostResponse;
import com.example.iplmarket_fe.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public interface OnItemClickEventListener {
        void onItemClick(View view, int position);
    }

    private List<PostResponse> postResponses = new ArrayList<>();
    private OnItemClickEventListener itemClickEventListener;

    public void setPosts(List<PostResponse> postResponses) {
        this.postResponses = postResponses;
        notifyDataSetChanged();
    }

    public void addItem(PostResponse postResponse) {
        postResponses.add(postResponse);
        notifyDataSetChanged();
    }

    public int getPostNumAt(int position) {
        if (postResponses != null && position >= 0 && position < postResponses.size()) {
            return postResponses.get(position).getNum();
        }
        return -1;
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
        PostResponse postResponse = postResponses.get(position);
        holder.setPost(postResponse);
    }

    @Override
    public int getItemCount() {
        return postResponses.size();
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
        TextView textPrice;

        public ViewHolder(View itemView, OnItemClickEventListener listener) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
            textUserId = itemView.findViewById(R.id.textUserId);
            textCreateDate = itemView.findViewById(R.id.textCreateDate);
            textPrice = itemView.findViewById(R.id.textPrice);

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

        // base64 -> Bitmap
        private Bitmap base64ToBitmap(String base64){
            byte[] decodedString = Base64.getDecoder().decode(base64);

            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }

        public void setPost(PostResponse postResponse) {

            Date postDate = postResponse.getPostRegistDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            String postCreateDate = format.format(postDate);

            Bitmap thumbnailImage = base64ToBitmap(postResponse.getPostThumbnail());
            thumbnail.setImageBitmap(thumbnailImage);
            textTitle.setText(postResponse.getPostTitle());
            textContent.setText(postResponse.getPostContent());
            textUserId.setText(postResponse.getUserId());
            textCreateDate.setText(postCreateDate);
            textPrice.setText(postResponse.getPrice());
        }
    }
}
