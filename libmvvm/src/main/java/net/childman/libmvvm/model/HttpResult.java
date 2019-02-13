package net.childman.libmvvm.model;

/**
 * Created by czm on 2017/8/15.
 */

public interface HttpResult<T> {
    boolean isSuccess();
    int getCode();
    ServerResult<T> getServerResult();
    String getMsg();
}
