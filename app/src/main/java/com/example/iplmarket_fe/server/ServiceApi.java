package com.example.iplmarket_fe.server;

import com.example.iplmarket_fe.server.request.ArLoadRequest;
import com.example.iplmarket_fe.server.request.IdValidationRequest;
import com.example.iplmarket_fe.server.request.LoginRequest;
import com.example.iplmarket_fe.server.request.PostDetailRequest;
import com.example.iplmarket_fe.server.request.PostListRequest;
import com.example.iplmarket_fe.server.request.RegistRequest;
import com.example.iplmarket_fe.server.response.ArLoadResponse;
import com.example.iplmarket_fe.server.response.IdValidationResponse;
import com.example.iplmarket_fe.server.response.LoginResponse;
import com.example.iplmarket_fe.server.response.PostDetailResponse;
import com.example.iplmarket_fe.server.response.PostResponse;
import com.example.iplmarket_fe.server.response.RegisterResponse;
import com.example.iplmarket_fe.server.response.WriteContentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
 @POST("login/userLogin")
 Call<LoginResponse> userLogin(@Body LoginRequest data);

 @POST("login/registUser")
 Call<RegisterResponse> userRegister(@Body RegistRequest data);

 @POST("login/idcheck")
 Call<IdValidationResponse> checkResponse(@Body IdValidationRequest idValidationRequest);

 @POST("posts/boardlist")
 Call<List<PostResponse>> getUserPosts(@Body PostListRequest postListRequest);

 @POST("posts/boardlist")
 Call<List<PostResponse>> getAllPosts();
 @POST("posts/regist")
 Call<WriteContentResponse> WroteResponse(@Body PostData postData);

 @POST("posts/detail")
 Call<PostDetailResponse> getPostDetail(@Body PostDetailRequest postDetailRequest);

 @POST("model/vrload")
 Call<ArLoadResponse> getArLoad(@Body ArLoadRequest arLoadRequest);
}
