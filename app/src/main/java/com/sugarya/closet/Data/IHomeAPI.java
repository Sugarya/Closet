package com.sugarya.closet.Data;

import com.sugarya.closet.model.response.TestResponse;
import com.sugarya.closet.model.response.UpLoadImageResponse;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Ethan on 2017/9/15.
 */

public interface IHomeAPI {


    @GET("jdp/img/test")
    Observable<TestResponse> fetchTestMethod();

    @Multipart
    @POST("jdp/img/upload")
    Observable<UpLoadImageResponse> upLoadImage(@Part MultipartBody.Part part);
}
