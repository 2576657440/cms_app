package org.com.gannan.farminginfoplatform.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/25.
 */

public class ClassifyEntity implements Serializable {
    private String id;
    private String name;
    private String type;
    private String enName;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }
}
