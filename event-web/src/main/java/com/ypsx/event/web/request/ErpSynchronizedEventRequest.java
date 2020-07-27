package com.ypsx.event.web.request;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chuchengyi
 */
public class ErpSynchronizedEventRequest implements Serializable {
    private static final long serialVersionUID = 8745097479372818774L;

    /**
     * 功能：事件类型
     */
    @ApiModelProperty(value = "事件类型", example = "ERP_SYN_USER_LIST", required = true)
    private String eventType;


    /**
     * 功能：同步信息的列表
     */
    @ApiModelProperty(value = "同步数据列表", example = "", required = true)
    private List<SynchronizedItem> bizList;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<SynchronizedItem> getBizList() {
        return bizList;
    }

    public void setBizList(List<SynchronizedItem> bizList) {
        this.bizList = bizList;
    }

    @Override
    public String toString() {
        return "ErpSynchronizedEventRequest{" +
                "eventType='" + eventType + '\'' +
                ", bizList=" + bizList +
                '}';
    }

    public static void main(String[] args) {
        ErpSynchronizedEventRequest vo = new ErpSynchronizedEventRequest();
        vo.setEventType("ERP_SYN_USER_LIST");

        SynchronizedItem item = new SynchronizedItem();
        item.setId("1");
        item.setVersion(1);


        SynchronizedItem item1 = new SynchronizedItem();
        item1.setId("9991");
        item1.setVersion(1309);

        List<SynchronizedItem> list = new ArrayList<>();
        list.add(item);
        list.add(item1);
        vo.setBizList(list);

        System.out.println(JSON.toJSONString(vo));

    }
}
