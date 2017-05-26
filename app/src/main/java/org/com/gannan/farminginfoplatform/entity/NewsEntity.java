package org.com.gannan.farminginfoplatform.entity;

import org.com.gannan.farminginfoplatform.baseadapter.base.BaseEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/16.
 */

public class NewsEntity extends BaseEntity implements Serializable {
    private String id;
    private String updateDate;//---发布时间
    private String content;//----文章标题
    private String title;//----文章标题
    private String image;//----图片
    private String state;//----审核流程状态(信息员审核：1；系统管理员审核：2；发布：0；)
    private String cName;//----栏目名称
    private String expiryDate;//-----截止日期
    private Integer categoryType;//-----0,1是供求信息，2是其他
    private String remarks;//-----驳回原因
    private String infoArea;//-----所属乡镇
    private int isCheck;//批处理被选中标识，0隐藏，1显示，2选中
    private String stickTop;//（0：已经置顶；1：未置顶）
    private String video;//视频播放地址

    public String getStickTop() {
        return stickTop;
    }

    public String getVideo() {
        return video;
    }
    public void setVideo(String video) {
        this.video = video;
    }
    public void setStickTop(String stickTop) {
        this.stickTop = stickTop;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getInfoArea() {
        return infoArea;
    }

    public void setInfoArea(String infoArea) {
        this.infoArea = infoArea;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
