package com.blanc.event.dao;

import com.blanc.event.main.Application;
import com.blanc.event.manager.impl.AbstractHintManager;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void getEvent() {
        try (final HintManager hintManager = initHint("event",2)) {
            eventDao.getEvent("wms","a","xix",1L);
            assert true;
        }catch (Throwable throwable){
            assert false;
        }
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