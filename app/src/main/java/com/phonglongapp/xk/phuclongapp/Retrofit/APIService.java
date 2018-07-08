package com.phonglongapp.xk.phuclongapp.Retrofit;

import com.phonglongapp.xk.phuclongapp.Model.MyResponse;
import com.phonglongapp.xk.phuclongapp.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAArLLIDsM:APA91bENTbEHZOomeboc4PLMurGu80LMVEXog8vHPq2HFrQJh2pIbMToeEUf0h15KV9CzGeeewiGtci2Ax9uSE90B2hKgDvl4dtDqTCPsTFuk_jcfyNB6WeukZmgwAHt_GRawsWIg7ZK"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNoti(@Body Sender body);
}
