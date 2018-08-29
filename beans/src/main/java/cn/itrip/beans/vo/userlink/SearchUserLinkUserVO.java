package cn.itrip.beans.vo.userlink;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询常用联系人(模糊姓名)
 */
@ApiModel(value = "SearchUserLinkUserVO",description = "查询常用联系人")
public class SearchUserLinkUserVO {

    private String linkUserName;

    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }
}
