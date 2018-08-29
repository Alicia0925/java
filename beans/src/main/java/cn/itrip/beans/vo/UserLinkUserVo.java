package cn.itrip.beans.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 接收客户端表单中的用户新增常用旅客信息VO
 *
 *
 *
 * */
@ApiModel(value="UserLinkUserVo",description ="新增旅客信息" )
public class UserLinkUserVo implements Serializable {
    @ApiModelProperty("[必填]注册旅客姓名")
    private String linkUserName;

    @ApiModelProperty("[必填]身份证号")
    private String linkIdCard;

    @ApiModelProperty("[非必填]电话/手机")
    private String linkPhone="";
    @ApiModelProperty("[必填]登录用户Id")
    private String userId;//用户id

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }

    public String getLinkIdCard() {
        return linkIdCard;
    }

    public void setLinkIdCard(String linkIdCard) {
        this.linkIdCard = linkIdCard;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }
}
