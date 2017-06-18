package com.example.vartikasharma.bookingpage;


import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("booking/slots/all")
    Call<JsonElement> getBookingSlots(@Query("username") String username, @Query("api_key") String apiKey, @Query("vc") int vc,
                                      @Query("expert_username") String expertUserName, @Query("format") String format);

}
