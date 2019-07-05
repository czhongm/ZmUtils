package net.childman.libmvvm.model;

/**
 * Created by czm on 2017/8/15.
 */

public interface IHttpResult<T> {
    boolean isSuccess();
    int getCode();
    IServerResult<T> getServerResult();
    String getMsg();
    T getData();
}
