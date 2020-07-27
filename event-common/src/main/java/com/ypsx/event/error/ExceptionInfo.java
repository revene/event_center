package com.ypsx.event.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 异常信息
 *
 * @author wangbaoliang
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionInfo {

    /**
     * 异常编码
     */
    private String exceptionCode;

    /**
     * 异常信息描述
     */
    private String exceptionMessage;


}
