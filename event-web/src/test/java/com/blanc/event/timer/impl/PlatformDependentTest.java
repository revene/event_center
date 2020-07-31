package com.blanc.event.timer.impl;

import com.blanc.event.main.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author ：blanc
 * @date ：Created in 2020/7/31 上午10:15
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class PlatformDependentTest {

    @Test
    public void normalizeTicksPerWheel() {
        final int i = PlatformDependent.normalizeTicksPerWheel(10);
        assert i ==  16;
    }
}