package com.example.iplmarket_fe;

//import android.service.autofill.UserData;
import com.example.iplmarket_fe.ApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
   @POST("login/userLogin")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("login/registUser")
    Call<RegisterResponse> userRegister(@Body RegisterData data);

    @POST("login/idcheck")
    Call<CheckResponse> checkResponse(@Body UserData userData);

    @POST("posts/boardlist")
    Call<List<Post>> getPosts(@Body UserData userData);

    @POST("posts/detail")
    Call<ApiResponse> sendProductData(@Body int productNumber);
}
