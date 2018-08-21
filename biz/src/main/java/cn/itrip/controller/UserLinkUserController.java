package cn.itrip.controller;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.UserLinkUser;
import cn.itrip.beans.vo.UserLinkUserVo;
import cn.itrip.service.userlinkuser.UserLinkUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api
@RequestMapping("/api/userLinkUser")
public class UserLinkUserController {
    @Resource
    private UserLinkUserService userLinkUserService;


    /**查旅客list
     * */
@RequestMapping(value="list",method = RequestMethod.POST)
@ApiOperation(value = "获取常用旅客信息",httpMethod = "post" )
   public @ResponseBody Dto getList(@ApiParam(value = "页面输入") @RequestBody UserLinkUserVo linkUserVo){
    Long userId=linkUserVo.getUserId()!=null? Long.parseLong(linkUserVo.getUserId()): null;
    List<UserLinkUser> linkUsers=null;
    Dto dto= new Dto();
    if(userId==null){
        dto.setErrorCode("100055");
        dto.setMsg("请登录后查看！");
    }
    try {
        linkUsers= userLinkUserService.getUserLinkUser(userId,linkUserVo.getLinkUserName());
        dto.setData(linkUsers);
        dto.setErrorCode("100056");
    } catch (Exception e) {
        e.printStackTrace();
    }
return  dto;
}
}
