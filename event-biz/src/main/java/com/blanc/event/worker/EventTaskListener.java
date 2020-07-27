package com.blanc.event.worker;

import com.blanc.event.service.EventExecuteService;
import com.blanc.event.timer.TaskListener;
import com.blanc.event.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 执行一个具体的任务
 * @author chuchengyi
 */
@Component
public class EventTaskListener implements TaskListener<EventTimerTask> {

    @Resource
    private EventExecuteService eventExecuteService;

    /**
     * 功能：定义日志信息
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @Override
    public boolean executeTask(EventTimerTask timerTask) {
        try {
            Event event = timerTask.getTask();
            //功能：提交事件任务信息
            eventExecuteService.submitEvent(event);
        } catch (Throwable throwable) {
            logger.error("EventTaskListener[executeTask] is error:" + throwable.getMessage());
        }
        return true;

    }
}
