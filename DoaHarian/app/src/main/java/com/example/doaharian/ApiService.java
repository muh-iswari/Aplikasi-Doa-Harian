package com.example.doaharian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
public interface ApiService {
    @GET("api/")
//    Call<ContentResponse> getContent(@Query("id") int id);
    Call<List<Content>> getContent();

}

