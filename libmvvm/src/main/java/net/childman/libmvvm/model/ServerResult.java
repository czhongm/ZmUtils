package net.childman.libmvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器返回信息解析
 * Created by czm on 18-3-19.
 */

public class ServerResult<T> {
    private int code;
    @SerializedName(value = "data", alternate = {"bodys"})
    private T data;
    private String msg;
    private int count;
    private String token;
    private int status;

    public ServerResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
