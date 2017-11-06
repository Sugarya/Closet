package com.sugarya.closet.model.response;

import com.sugarya.closet.model.base.BaseResponse;

/**
 * Created by Ethan on 2017/9/23.
 */

public class TestResponse extends BaseResponse {


    /**
     * content : 测试 nice
     * success : true
     */

    private String content;
    private boolean success;

    @Override
    public String toString() {
        return "TestResponse{" +
                "content='" + content + '\'' +
                ", success=" + success +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
