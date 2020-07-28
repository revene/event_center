package com.blanc.event.web.config;

import com.blanc.event.main.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author ：blanc
 * @date ：Created in 2020/7/28 上午12:04
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class DatabaseConfigurationTest {

    @Test
    public void dataSource() {
        File file = new File(Thread.currentThread().getClass().getResource("sharding-config.yml").getFile());
        assertNotNull(file);
    }
}