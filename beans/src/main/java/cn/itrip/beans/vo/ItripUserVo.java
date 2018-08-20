package cn.itrip.beans.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 接收客户端表单中的用户注册信息VO
 *
 *
 *
 * */
@ApiModel(value="ItripUserVo",description ="用户注册信息" )
public class ItripUserVo {
   @ApiModelProperty("[必填]注册用户名称")
     private String userCode;

     @ApiModelProperty("[必填]注册密码")
     private String userPassword;

     @ApiModelProperty("[非必填]昵称")
     private String userName="";

     public String getUserCode() {
          return userCode;
     }

     public void setUserCode(String userCode) {
          this.userCode = userCode;
     }

     public String getUserPassword() {
          return userPassword;
     }

     public void setUserPassword(String userPassword) {
          this.userPassword = userPassword;
     }

     public String getUserName() {
          return userName;
     }

     public void setUserName(String userName) {
          this.userName = userName;
     }
}
