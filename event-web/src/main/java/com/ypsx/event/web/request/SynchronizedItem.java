package com.ypsx.event.web.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * 功能：数据同步项目
 *
 * @author chuchengyi
 */
public class SynchronizedItem {

    /**
     * 功能：数据ID
     */
    @ApiModelProperty(value = "同步数据ID", example = "1", required = true)
    private String id;

    /**
     * 功能：数据版本号
     */
    @ApiModelProperty(value = "同步数据的版本号", example = "0", required = true)
    private long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SynchronizedItem{" +
                "id='" + id + '\'' +
                ", version=" + version +
                '}';
    }
}
