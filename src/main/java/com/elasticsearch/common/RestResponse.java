package com.elasticsearch.common;

import lombok.Data;

@Data
public class RestResponse<T> {

    private Integer code;

    private String message;

    private T data;

    public RestResponse(Integer code) {
        this.code = code;
    }

    public RestResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResponse(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public RestResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static RestResponse success() {
        return new RestResponse(ResponseCode.OK);
    }

    public static <T> RestResponse success(T t) {
        return new RestResponse(ResponseCode.OK, t);
    }

    public static RestResponse failure(String message) {
        return new RestResponse(ResponseCode.SYSERROR, message);
    }

    public static RestResponse failure(Integer code, String message) {
        return new RestResponse(code, message);
    }

}
