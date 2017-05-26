package org.com.gannan.farminginfoplatform.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/25.
 */

public class MapEntity implements Serializable{
    private Map<String, String> pathMap = new HashMap<String, String>();

    public Map<String, String> getPathMap() {
        return pathMap;
    }

    public void setPathMap(Map<String, String> pathMap) {
        this.pathMap = pathMap;
    }
}
