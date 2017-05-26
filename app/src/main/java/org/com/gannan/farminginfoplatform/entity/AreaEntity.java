package org.com.gannan.farminginfoplatform.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/25.
 */

public class AreaEntity implements Serializable{
    private String id;
    private String name;
    private String parentId;
    private String parentIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

}
