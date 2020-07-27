package com.ypsx.event.error;

import com.ypsx.util.model.ExceptionInfo;
import com.ypsx.util.model.Result;

/**
 * @author chuchengyi
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
        result.setErrorCode(exception.getErrorCode());
        result.setErrorMessage(exception.getErrorMessage());
    }


    /**
     * 功能：构造错误信息
     *
     * @param throwable
     * @return
     */
    public static ExceptionInfo genSystemError(Throwable throwable) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setErrorCode(SYSTEM_ERROR);
        exceptionInfo.setErrorMessage(throwable.getMessage());
        return exceptionInfo;
    }
}
