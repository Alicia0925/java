package cn.itrip.auth.controller;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.ItripUserVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
/**
 * 用户管理控制器
 *
 * */
@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 跳转到注册页面
     * */
    @RequestMapping("/register")
    public String toRegister(){
        return "register";
    }
    /**
     * 使用邮箱注册
     * @param userVO
     * @return
     *
     * */
    @ApiOperation(value="使用邮箱注册",httpMethod = "POST",protocols = "HTTP", produces = "application/json",
            response = Dto.class)
    @RequestMapping(value = "/doRegister",method = RequestMethod.POST,produces = "application/json")
    public @ResponseBody Dto doRegister(@ApiParam(name="userVO",value="用户实体",required=true)
                                        @RequestBody ItripUserVo userVO){

        if(userVO!=null){
            Dto  dto=  new Dto();

            return dto;
        }


        return null;

    }


}
