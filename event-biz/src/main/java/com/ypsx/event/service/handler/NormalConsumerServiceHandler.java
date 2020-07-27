package com.ypsx.event.service.handler;

import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventResult;
import com.ypsx.event.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author chuchengyi
 */
@Component("normalConsumerServiceHandler")
public class NormalConsumerServiceHandler implements EventConsumerServiceHandler {


    @Resource

    private EventResultHandler eventResultHandler;
    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");


    @Override
    public void process(Event event) {

    }

    @Override
    public void processResult(EventResult result, Event event) {
        try {
            eventResultHandler.publishEventResult(event, result);
        } catch (Throwable throwable) {
            logger.error("NormalConsumerServiceHandler[processResult] is error:" + throwable.getMessage());
        }

    }


    @Override
    public void processException(Throwable ex, Event event) {
        try {
            EventResult result = new EventResult();
            //设置未知的处理机器
            result.setClientIp(IpUtil.UN_KNOWN_HOST);
            //设置失败标志
            result.setSuccess(false);
            //设置错误信息
            result.setErrorMessage(ex.getMessage());
            eventResultHandler.publishEventResult(event, result);
        } catch (Throwable throwable) {
            logger.error("NormalConsumerServiceHandler[processException] is error:" + throwable.getMessage());
        }

    }
}
