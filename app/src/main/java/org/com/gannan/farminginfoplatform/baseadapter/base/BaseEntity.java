package org.com.gannan.farminginfoplatform.baseadapter.base;

/**
 * Created by Administrator on 2016/11/17.
 */

public class BaseEntity {
    private int itemType;//信息类型，是否为视频itemType，0普通信息，1视频

    public int getItemType() {
        return itemType;
    }
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
