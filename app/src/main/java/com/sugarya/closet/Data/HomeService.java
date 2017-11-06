package com.sugarya.closet.Data;

import com.sugarya.closet.model.response.TestResponse;
import com.sugarya.closet.model.response.UpLoadImageResponse;

import okhttp3.MultipartBody;
import rx.Observable;


/**
 * Created by Ethan on 2017/9/15.
 */

public class HomeService {

    private IHomeAPI mHomeAPI;

    public HomeService() {
        mHomeAPI = NetworkHelper.getHelper().getRetrofit().create(IHomeAPI.class);
    }

    /**
     * 获取主页信息 part1
     *
     * @return
     */
    public Observable<TestResponse> fetchTestMethod() {
        return mHomeAPI.fetchTestMethod();
    }

    public Observable<UpLoadImageResponse> upLoadImage(MultipartBody.Part params) {
        return mHomeAPI.upLoadImage(params);
    }

}
