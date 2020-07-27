package com.ypsx.event.util;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author chuchengyi
 */
public class ParamUtil {


    public static boolean isNULL(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean isNotULL(Object object) {
        if (object != null) {
            return true;
        }
        return false;
    }


    public static boolean isEmpty(String object) {
        if (StringUtils.isEmpty(object)) {

            return true;
        }
        return false;
    }

    public static boolean isEmpty(List object) {
        if (object == null || object.size() == 0) {
            return true;
        }
        return false;
    }
}
