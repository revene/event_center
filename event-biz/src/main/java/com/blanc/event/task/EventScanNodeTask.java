package com.blanc.event.task;

import com.blanc.event.manager.EventScanNodeManager;
import com.blanc.event.model.EventScanNode;
import com.blanc.event.service.EventScanService;
import com.google.common.base.Throwables;
import com.blanc.event.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.List;

/**
 * 整个方法的入口
 * ApplicationContext事件机制是观察者设计模式的实现，通过ApplicationEvent类和ApplicationListener接口，可以实现ApplicationContext事件处理；
 * 一旦监听到ApplicationContextEvent,则出发onApplicationEvent方法
 *
 * @author wangbaoliang
 */
@Slf4j(topic = "taskLog")
@Component
public class EventScanNodeTask implements ApplicationListener<ApplicationContextEvent> {

    @Resource
    private EventScanNodeManager eventScanNodeManager;
    @Resource
    private EventScanService eventScanService;

    /**
     * cron表达式：30s刷新一次
     */
    private static final String SCHEDULED_CRON = "0/30 * * * * ? ";

    /**
     * 这个是整个事件中心的入口
     * 功能：30s 一次扫描本级的ip地址是否在数据库中event_scan表的节点列表中
     * ，如果该机器在节点列表中，则扫描scanEvent扫描事件
     */
    @Scheduled(cron = SCHEDULED_CRON)
    private void eventScan() {
        try {
            //获取到本机的ip地址信息,内网ip
            InetAddress address = IpUtil.getLocalHostDress();
            String ip = address.getHostAddress();
            log.info("EvenScanNodeTask [eventScanNode] local ip is : {} " + ip);
            //获取到具体的扫描节点
            List<EventScanNode> nodeList = eventScanNodeManager.listAll();
            if (CollectionUtils.isEmpty(nodeList)) {
                log.warn("eventScanNodeTask is warning! scanNodeList is empty, please check");
            }
            //获取到所有的事件中心中所设置的扫描节点,本机判断是否在扫描节点的表中,如果不在,要cancelScanEvent多次?
            //即一台机器可以配置多个扫描节点
            for (EventScanNode node : nodeList) {
                if (node.getIp().equals(ip)) {
                    eventScanService.scanEvent(node);
                } else {
                    eventScanService.cancelScanEvent(node);
                }
            }
        } catch (Throwable throwable) {
            log.error("EvenScanNodeTask[eventScan] is error, caused by {}", Throwables.getStackTraceAsString(throwable));
        }
    }


    /**
     * 搜索出所有的扫描节点并全部停止
     */
    private void stopEventScan() {
        try {
            //获取到扫描节点的信息
            List<EventScanNode> nodeList = eventScanNodeManager.listAll();
            //对每个节点进行扫描
            for (EventScanNode node : nodeList) {
                eventScanService.cancelScanEvent(node);
            }
            //停止扫描数据信息
            eventScanService.stop();
        } catch (Throwable throwable) {
            log.error("EvenScanNodeTask[stopEventScan] is error, caused by {}", Throwables.getStackTraceAsString(throwable));
        }
    }

    /**
     * 监听到ContextRefreshedEvent或者ContextClosedEvent,都会主动执行eventScan定时任务方法一次
     *
     * @param event 监听到的事件
     */
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            if (event.getApplicationContext().getParent() == null) {
                eventScan();
            }
        } else if (event instanceof ContextClosedEvent) {
            if (event.getApplicationContext().getParent() == null) {
                eventScan();
            }
        }
    }
}
