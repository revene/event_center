package com.blanc.event.worker;

import com.blanc.event.model.Event;
import com.blanc.event.timer.TaskExecutor;
import com.blanc.event.timer.Timeout;
import com.blanc.event.timer.TimerTask;
import lombok.Data;
import lombok.ToString;

/**
 * 事件的执行Task对象
 *
 * @author blanc
 */
@Data
@ToString
public class EventTimerTask implements TimerTask<Event> {

    /**
     * 要执行的event
     */
    private Event event;

    /**
     * 功能：事件监听器
     */
    private EventTaskExecutor taskExecutor;

    @Override
    public void run(Timeout timeout) throws Exception {
        taskExecutor.executeTask(this);
    }

    @Override
    public Event getTask() {
        return event;
    }

    @Override
    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    @Override
    public String getId() {
        return event.getId() + "";
    }

}
