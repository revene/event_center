package com.ypsx.event.model;

/**
 * 返回的结果包装类
 *
 * @author ：blanc
 * @date ：Created in 2020/7/27 下午5:34
 */
public class Result<T> {

    /**
     * 是否响应成功
     */
    private boolean success;

    /**
     * 响应结果
     */
    private T data;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 无参构造函数
     */
    public Result() {
        this.success = false;
    }

    /**
     * 设置成功
     */
    public void success() {
        this.success = Boolean.TRUE;
    }

    /**
     * 设置成功 + 响应
     *
     * @param data 返回的响应
     */
    public void success(T data) {
        success();
        this.data = data;
    }

    /**
     * 设置失败
     *
     * @param errorMessage 失败的原因
     */
    public void fail(String errorMessage) {
        this.success = Boolean.FALSE;
        this.errorMessage = errorMessage;
    }

    /**
     * 判断执行结果是否成功
     *
     * @return true or false
     */
    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
