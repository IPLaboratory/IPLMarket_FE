package com.example.iplmarket_fe;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<RegisterResponse> userRegister(@Body RegisterData data);

    @GET("checkDuplicateId")
    Call<IdCheckResponse> checkDuplicateId(@Query("id") String id);

}