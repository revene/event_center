package com.ypsx.event;

import lombok.Data;
import lombok.ToString;

/**
 * 返回的结果包装类
 *
 * @author ：blanc
 * @date ：Created in 2020/7/27 下午5:34
 */
@Data
@ToString
public class Result<T> {

    /**
     * 是否响应成功
     */
    private boolean isSuccess;

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
        this.isSuccess = false;
    }

    /**
     * 设置成功
     */
    public void success() {
        this.isSuccess = Boolean.TRUE;
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
        this.isSuccess = Boolean.FALSE;
        this.errorMessage = errorMessage;
    }
}
