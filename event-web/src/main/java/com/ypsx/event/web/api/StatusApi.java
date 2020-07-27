package com.ypsx.event.web.api;

import com.ypsx.util.model.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chuchengyi
 */
@Controller
public class StatusApi {


    @ResponseBody
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public Result<Boolean> status() {
        Result<Boolean> result = new Result<>();
        result.setSuccess(true);
        return result;
    }
}
