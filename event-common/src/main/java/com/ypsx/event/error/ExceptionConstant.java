package com.ypsx.event.error;

import com.ypsx.util.model.ExceptionInfo;

/**
 * 功能：系统异常的常量类
 *
 * @author chuchengyi
 */
public class ExceptionConstant {


    public static final ExceptionInfo OK = new ExceptionInfo("OK", "正确");


    public static final ExceptionInfo PARAM_IS_NULL = new ExceptionInfo("PARAM_IS_NULL", "参数为空");

    public static final ExceptionInfo APP_CODE_IS_NULL = new ExceptionInfo("APP_CODE_IS_NULL", "app code 参数为空");

    public static final ExceptionInfo EVENT_TYPE_IS_NULL = new ExceptionInfo("EVENT_TYPE_IS_NULL", "eventType 参数为空");

    public static final ExceptionInfo BIZ_ID_IS_NULL = new ExceptionInfo("BIZ_ID_IS_NULL", "bizId 参数为空");


    public static final ExceptionInfo EVENT_TYPE_IS_EXIST = new ExceptionInfo("EVENT_TYPE_IS_EXIST", "事件类型已经存在");


    public static final ExceptionInfo EVENT_TYPE_IS_NOT_EXIST = new ExceptionInfo("EVENT_TYPE_IS_NOT_EXIST", "事件类型不存在");


    public static final ExceptionInfo EVENT_EXIST = new ExceptionInfo("EVENT_EXIST", "事件已经存在");


    public static final ExceptionInfo BIZ_LIST_IS_NULL = new ExceptionInfo("BIZ_LIST_IS_NULL", "bizList 参数为空");
}
