package cn.itrip.auth.controller;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DtoUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理控制器
 *
 * */
@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;

    //登录的方法
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public @ResponseBody Dto login(@RequestParam("userCode")String userCode,
                     @RequestParam("password")String password,
                     HttpServletRequest httpServletRequest){
        User user = new User();
        user = userService.selectByUserCode(userCode);
        if (user!=null){
            if (password.equals(user.getUserPassword())){
                httpServletRequest.getSession().getServletContext().setAttribute("user",user);
                return DtoUtil.returnSuccess();
            }else{
                return DtoUtil.returnFail("用户密码错误","1201");
            }
        }
        return DtoUtil.returnFail("该邮箱或手机号尚未注册，请注册后继续登录","1202");
    }
    //注册的方法
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody Dto register(@RequestParam("userCode")String userCode,
                           @RequestParam("password")String password){
        if (userService.selectByUserCode(userCode)!=null){
            return DtoUtil.returnFail("该邮箱或手机号已被注册，请返回登录","1203");
        }else{
            User user = new User();
            user.setUserCode(userCode);
            user.setUserPassword(password);
            if (userService.insert(user)){
                return DtoUtil.returnSuccess("注册成功，请返回登录");
            }else{
                return DtoUtil.returnFail("注册失败","1204");
            }
        }
    }
    //注销的方法
    @RequestMapping(value = "/logOut",method = RequestMethod.POST)
    public @ResponseBody Dto logOut(HttpServletRequest httpServletRequest){
        httpServletRequest.removeAttribute("user");
        return DtoUtil.returnSuccess();
    }

}
