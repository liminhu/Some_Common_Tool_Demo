package com.lmhu.advancelight.book.chapter5.Retrofit;

import com.lmhu.advancelight.book.chapter5.Retrofit.model.IpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by hulimin on 2018/3/15.
 */

public interface IpService {

    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent:HuRetrofit"
    }

    )

    @GET("getIpInfo.php?ip=59.108.54.37")
    Call<IpModel> getIpMsg();
}
