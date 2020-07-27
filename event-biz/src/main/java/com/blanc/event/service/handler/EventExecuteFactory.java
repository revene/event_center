package com.blanc.event.service.handler;

import com.lmax.disruptor.EventFactory;
import com.blanc.event.model.EventExecuteResult;

/**
 * @author chuchengyi
 */
public class EventExecuteFactory implements EventFactory<EventExecuteResult> {
    @Override
    public EventExecuteResult newInstance() {
        return new EventExecuteResult();
    }

}
