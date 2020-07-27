package com.ypsx.event.worker;

import com.ypsx.event.model.Event;
import com.ypsx.event.timer.AbstractTimerTask;
import com.ypsx.event.timer.TaskListener;
import com.ypsx.event.timer.TimerTask;
import com.ypsx.event.timer.WorkerStatus;
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
