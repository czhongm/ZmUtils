package net.childman.libmvvm.model;

/**
 * 服务器返回信息解析
 * Created by czm on 18-3-19.
 */

public interface ServerResult<T> {
    boolean isSuccess();
    int getCount();
    int getCode();
    T getData();
    String getMsg();
}
