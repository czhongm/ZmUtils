package net.childman.libmvvm.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by czm on 2017/8/15.
 */

public class HttpResult<T> {
    @SerializedName(value = "result",alternate = {"code"})
    private int code;
    @SerializedName(value = "data",alternate = {"bodys"})
    private ServerResult<T> data;
    @SerializedName(value = "message",alternate = {"msg"})
    private String msg;

    public HttpResult() {
    }

    public boolean isSuccess() {
        return code == 200 && data != null && data.getCode()==1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServerResult<T> getData() {
        return data;
    }

    public void setData(ServerResult<T> data) {
        this.data = data;
    }

    public String getMsg() {
        if(TextUtils.isEmpty(msg)){
            if(data != null){
                return data.getMsg();
            }else{
                return null;
            }
        }else {
            return msg;
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
