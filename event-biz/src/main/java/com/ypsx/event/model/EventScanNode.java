package com.ypsx.event.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能:扫描分发事件的信息
 *
 * @author chuchengyi
 */
public class EventScanNode implements Serializable {
    /**
     * 数据ID
     */
    private Long id;

    /**
     * 执行机器的地址
     */
    private String ip;

    /**
     * 数据节点的名称
     */
    private String node;

    /**
     * 上次扫描的时间
     */
    private Long scanTime;

    /**
     * 状态0 表示正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;


    /**
     * 功能：表的索引信息
     */
    private int tableIndex;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Long getScanTime() {
        return scanTime;
    }

    public void setScanTime(Long scanTime) {
        this.scanTime = scanTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }


    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ip=").append(ip);
        sb.append(", node=").append(node);
        sb.append(", sacnTime=").append(scanTime);
        sb.append(", status=").append(status);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModify=").append(gmtModify);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", tableIndex=").append(tableIndex);
        sb.append("]");
        return sb.toString();
    }
}

