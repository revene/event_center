package com.ypsx.event.service.handler;

import com.lmax.disruptor.EventFactory;
import com.ypsx.event.model.Event;
import com.ypsx.event.model.EventExecuteResult;
import com.ypsx.event.model.EventResult;

/**
 * @author chuchengyi
 */
public class EventExecuteFactory implements EventFactory<EventExecuteResult> {
    @Override
    public EventExecuteResult newInstance() {
        return new EventExecuteResult();
    }

}
