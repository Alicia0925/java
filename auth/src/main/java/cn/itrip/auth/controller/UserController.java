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
import java.util.Set;

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
                    return DtoUtil.returnFail("验证码错误，请重试","1204");
                }
            }
        }else{
            if (!(userCode.trim()).matches(phoneReg)){
                return DtoUtil.returnFail("号码不符合规格","1206");
            }else{
                if (redisAPI.get("activationCode")!=null){
                    if (isActivationCodeTrue(checkActivationCode,user)){
                        return DtoUtil.returnSuccess("注册成功，请返回首页登录");
                    }
                    return DtoUtil.returnFail("验证码输入错误，请重试","1204");
                }else{
                    HashMap<String, Object> result = null;
                    CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
                    restAPI.init("app.cloopen.com", "8883");
                    // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
                    restAPI.setAccount("8aaf07086541761801655a28e4eb0edc", "f8809f8794b2400197a54abdcae0d8dd");
                    // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
                    restAPI.setAppId("8aaf07086541761801655a28e5450ee3");
                    String activationCode = MD5.getMd5(new Date().toString(),4);
                    redisAPI.set("activationCode",120,activationCode);
                    // 请使用管理控制台中已创建应用的APPID。
                    result = restAPI.sendTemplateSMS(userCode,"1" ,new String[]{activationCode,"2"});
                    if("000000".equals(result.get("statusCode"))){
                        //正常返回输出data包体信息（map）
                        HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                        Set<String> keySet = data.keySet();
                        for(String key:keySet){
                            Object object = data.get(key);
                            System.out.println(key +" = "+object);
                        }
                    }else{
                        //异常返回输出错误码和错误信息
                        System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
                    }
                    return DtoUtil.returnSuccess("短信已发送，请输入验证码");
                }
            }
        }
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
