package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.vo.TokenVo;
import cn.itrip.common.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * 用户管理控制器
 * */
@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisAPI redisAPI;
    @Resource
    private TokenService tokenService;
    //邮箱验证
    private static String emailReg = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    //手机号码验证
    private static String phoneReg = "1[358]\\d{9}";

    private SendMessage sendMessage = new SendMessage();

    //登录的方法
    @RequestMapping(value = "/dologin",method = RequestMethod.POST)
    public @ResponseBody Dto login(@RequestParam("name")String userCode,
                                   @RequestParam(value = "password",required = false)String password,
                                   HttpServletRequest request){
        User user = null;
        String token = null;
        try {
            user = userService.findByUserCode(userCode.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user!=null){
            if ((MD5.getMd5(password.trim(),32)).equals(user.getUserPassword())){
                try {
                    token = tokenService.generateToken(request.getHeader("user-agent"),user);
                    tokenService.saveToken(token,user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//返回ItripTokenVO
                TokenVo tokenVo=new TokenVo(token,
                        Calendar.getInstance().getTimeInMillis()+TokenService.SESSION_TIMEOUT*1000,//2h有效期
                        Calendar.getInstance().getTimeInMillis());

                return DtoUtil.returnDataSuccess(tokenVo);
            }else{
                return DtoUtil.returnFail("用户密码错误","1201");
            }
        }
        return DtoUtil.returnFail("该邮箱或手机号尚未注册，请注册后继续登录","1202");
    }

    //判断该userCode是否已被注册
    @RequestMapping(value = "/ckusr",method=RequestMethod.GET)
    public @ResponseBody Dto ckusr(@RequestParam("name")String userCode){
        try {
            if (userService.findByUserCode(userCode)!=null){
                return DtoUtil.returnFail("已被注册","");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DtoUtil.returnSuccess();
    }

    //注册的方法
    @RequestMapping(value = "/doregister",method = RequestMethod.POST)
    public @ResponseBody Dto doregister(@RequestBody User user){
        int isEmail = 0;
        if (!user.getUserCode().matches(emailReg)){//如果不符合邮箱格式
            return DtoUtil.returnFail("邮箱地址不符合规格","1205");
        }else{
            try {
                userService.addNewUser(user,isEmail);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("","");
            }
            return DtoUtil.returnSuccess("注册成功");
        }
    }

   // 通过手机号码注册
    @RequestMapping(value = "/registerbyphone",method=RequestMethod.POST)
    public @ResponseBody Dto registerbyphone(@RequestBody User user){
        int isEmail=1;
        if (!(user.getUserCode().trim()).matches(phoneReg)){
            return DtoUtil.returnFail("号码不符合规格","1206");
        }else{
            try {
                userService.addNewUser(user,isEmail);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("","");
            }
            return DtoUtil.returnSuccess("注册成功");
        }
    }

    //判断验证码是否匹配的方法（邮箱）
    @RequestMapping(value="/activate",method = RequestMethod.PUT)
    public @ResponseBody Dto activate(@RequestParam("code")String code,
                                      @RequestParam("user")String userCode){
        return ckAcCode(code,userCode);
    }
    //判断验证码是否匹配的方法(手机)
    @RequestMapping(value = "/validatephone",method = RequestMethod.PUT)
    public @ResponseBody Dto validatephone(@RequestParam("code")String code,
                                      @RequestParam("user")String userCode){
        return ckAcCode(code,userCode);
    }
    //注销的方法
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public @ResponseBody Dto logout(HttpServletRequest request){

        String token = request.getHeader("token");
        try {
            if(!tokenService.validate(request.getHeader("user-agent"), token))
                return DtoUtil.returnFail("token无效", ErrorCode.AUTH_TOKEN_INVALID);
            //删除token和信息
            tokenService.delete(token);
            return DtoUtil.returnSuccess("注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("注销失败", ErrorCode.AUTH_UNKNOWN);
        }
       // return null;
    }
    public Dto ckAcCode(String code,
                        String userCode){
        User user = null;
        if (code.equals(redisAPI.get("activationCode"))){
            try {
                user = userService.findByUserCode(userCode);
                user.setActivated(1);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("","");
            }
            redisAPI.delete("activationCode");
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("","");
    }

}
