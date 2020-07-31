package com.blanc.event.worker;

import com.blanc.event.service.EventExecuteService;
import com.blanc.event.timer.TaskExecutor;
import com.blanc.event.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 执行任务实现
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "eventLog")
@Component
public class EventTaskExecutor implements TaskExecutor<EventTimerTask> {

    @Resource
    private EventExecuteService eventExecuteService;

    @Override
    public boolean executeTask(EventTimerTask timerTask) {
        try {
            Event event = timerTask.getTask();
            //功能：提交事件任务信息
            eventExecuteService.submitEvent(event);
        } catch (Throwable throwable) {
            log.error("EventTaskListener[executeTask] is error:" + throwable.getMessage());
        }
        return true;

    }
}
