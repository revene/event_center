package com.ypsx.event.error;

import com.ypsx.event.model.Result;

/**
 * 异常工具
 *
 * @author wangbaoliang
 */
public class ExceptionUtil {


    /**
     * 功能：构造错误信息
     */
    private static final String SYSTEM_ERROR = "SYSTEM_ERROR";

    /**
     * 功能：设置错误信息
     *
     * @param result
     * @param exception
     */
    public static void setException(Result result, ExceptionInfo exception) {
        result.fail(exception.getExceptionMessage());
    }


    /**
     * 功能：构造错误信息
     *
     * @param throwable
     * @return
     */
    public static ExceptionInfo genSystemError(Throwable throwable) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setExceptionCode(SYSTEM_ERROR);
        exceptionInfo.setExceptionMessage(throwable.getMessage());
        return exceptionInfo;
    }
}
