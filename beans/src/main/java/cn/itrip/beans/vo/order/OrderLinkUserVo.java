package cn.itrip.beans.vo.order;


import java.io.Serializable;

/**
 * 根据订单查询联系人返回VO
 *
 */
public class OrderLinkUserVo implements Serializable {

    private Long linkUserId;
    private String linkUserName;

    public Long getLinkUserId() {
        return linkUserId;
    }

    public void setLinkUserId(Long linkUserId) {
        this.linkUserId = linkUserId;
    }

    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }
}