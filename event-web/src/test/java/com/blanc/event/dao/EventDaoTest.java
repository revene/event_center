package com.blanc.event.dao;

import com.blanc.event.main.Application;
import com.blanc.event.manager.impl.AbstractHintManager;
import com.blanc.event.model.Event;
import com.blanc.event.model.EventStatus;
import com.sun.tools.corba.se.idl.EnumEntry;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.Test;
import org.junit.internal.Throwables;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * sharding-test
 *
 * @author ：blanc
 * @date ：Created in 2020/7/28 下午4:05
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class EventDaoTest extends AbstractHintManager {


    @Autowired
    private EventDao eventDao;

    @Test
    public void insertEvent() {
    }

    @Test
    public void getEventWithHint() {
        try (final HintManager hintManager = initHint("event", 13)) {
            eventDao.getEvent("wms", "a", "xix", 1L);
            assert true;
        } catch (Throwable throwable) {
            assert false;
        }
    }

    /**
     * 批量测试插入
     */
    @Test
    public void testBatchInsert() {
        List<Event> events = new ArrayList<>();
        for (int i = 0 ; i < 10000 ; i++){
            Event event = new Event();
            event.setEventType("testType");
            event.setAppCode("testApp");
            event.setStatus(EventStatus.ACTIVE.getStatus());
            event.setExecuteSize(0L);
            event.setExecuteTime(new Date().getTime());
            event.setBizId(i + "");
            event.setGmtCreate(new Date());
            event.setGmtModify(new Date());
            event.setVersion(0L);
            events.add(event);
        }
        for (Event event : events){
            try (HintManager hintManager = initHint("event", Integer.valueOf(event.getBizId()))){
                eventDao.insertEvent(event);
            }catch (Throwable throwable){
                System.out.println(Throwables.getStacktrace(throwable));
            }
        }

    }

    /**
     * 不initHint会怎么走?直接全表扫描了
     */
    @Test
    public void getEventWithOutHint() {
        final Event event = eventDao.getEvent("wms", "a", "xix", 1L);
    }

    @Test
    public void scanEvent() {
    }

    @Test
    public void updateEvent() {
    }

    @Test
    public void listEvent() {
    }

    @Test
    public void countEvent() {
    }
}