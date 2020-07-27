package com.blanc.event.worker;

import com.blanc.event.model.Event;
import com.blanc.event.timer.AbstractTimerTask;
import com.blanc.event.timer.TaskListener;
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
public class EventTimerTask extends AbstractTimerTask<Event> implements TimerTask {

    /**
     * 要执行的event
     */
    private Event event;

    /**
     * 功能：事件监听器
     */
    private EventTaskListener taskListener;

    @Override
    public Event getTask() {
        return event;
    }

    @Override
    public TaskListener getTaskListener() {
        return taskListener;
    }

    @Override
    public String getId() {
        return event.getId() + "";
    }

}
