package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import cn.itrip.common.DtoUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

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

    //登录的方法
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public @ResponseBody Dto login(@RequestParam("userCode")String userCode,
                                   @RequestParam("password")String password,
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
                return DtoUtil.returnSuccess("登录成功");
            }else{
                return DtoUtil.returnFail("用户密码错误","1201");
            }
        }
        return DtoUtil.returnFail("该邮箱或手机号尚未注册，请注册后继续登录","1202");
    }

    //注册的方法
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody Dto register(@RequestParam("userCode")String userCode,
                                      @RequestParam("password")String password,
                                      @RequestParam(value = "checkActivationCode",required = false)String checkActivationCode){
        User user = new User();
        if (userCode.contains("@")){//包含@ 判断是否是邮箱
            if (!userCode.matches(emailReg)){//如果不符合邮箱格式
                return DtoUtil.returnFail("邮箱地址不符合规格","1205");
            }else{
                user.setUserCode(userCode);
                user.setUserPassword(MD5.getMd5(password,32));
                user.setUserName(userCode);
                if (redisAPI.get("activationCode")==null){//验证码为空，发送激活码
//                    userService.sendActivationMail(userCode);
                    redisAPI.set("activationCode",userService.sendActivationMail(userCode));
                    return DtoUtil.returnSuccess("邮件已发送，请输入验证码");
                }else{
                    if (isActivationCodeTrue(checkActivationCode,user)){
                        return DtoUtil.returnSuccess("注册成功，请返回首页登录");
                    }
                    return DtoUtil.returnFail("注册失败，请重试","1204");
                }
            }
        }else{
            if (!userCode.matches(phoneReg)){
                return DtoUtil.returnFail("号码不符合规格","1206");
            }else{
                if (redisAPI.get("activationCode")!=null){
                    isActivationCodeTrue(checkActivationCode,user);
                }else{
                    HashMap<String, Object> result = null;
                    CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
                    restAPI.init("app.cloopen.com", "8883");
                    // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
                    restAPI.setAccount("8aaf07086541761801655a28e4eb0edc", "f8809f8794b2400197a54abdcae0d8dd");
                    // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
                    restAPI.setAppId("AppId");
                    // 请使用管理控制台中已创建应用的APPID。
                    String activationCode2 = MD5.getMd5(new Date().toString(),5);
                    result = restAPI.sendTemplateSMS(userCode,"1",new String[]{"您的验证码为"+activationCode2});
                    return DtoUtil.returnSuccess("验证码已发送，请输入验证码");
                }
            }
        }
        return DtoUtil.returnFail("注册失败","1203");
    }
    //注销的方法
    @RequestMapping(value = "/logOut",method = RequestMethod.POST)
    public @ResponseBody Dto logOut(@RequestParam("id")String userId){
        redisAPI.delete(userId);
        return DtoUtil.returnSuccess();
    }
    //判断验证码是否正确的方法
    private boolean isActivationCodeTrue(String checkActivationCode,User user){
        if (redisAPI.get("activationCode").equals(checkActivationCode)){//判断验证码是否正确
            user.setUserType(0);
            user.setActivated(1);
            try {
                userService.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
}
