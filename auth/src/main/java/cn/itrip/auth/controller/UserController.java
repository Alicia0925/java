package cn.itrip.auth.controller;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.common.MD5;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DtoUtil;

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

    //邮箱验证
    private static String emailReg = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    //手机号码验证
    private static String phoneReg = "1[358]\\d{9}";

    //登录的方法
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public @ResponseBody Dto login(@RequestParam("userCode")String userCode,
                                   @RequestParam("password")String password,
                                   HttpServletRequest httpServletRequest){
        User user = new User();
        user = userService.selectByUserCode(userCode);
        if (user!=null){
            if (password.equals(user.getUserPassword())){
                httpServletRequest.getSession().setAttribute("user",user);
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
                                      @RequestParam("password")String password,
                                      @RequestParam(value = "activationCode",required = false)String activationCode,
                                      @RequestParam(value = "checkActivationCode",required = false)String checkActivationCode){
        User user = new User();
        if (userCode.contains("@")){//包含@ 判断是否是邮箱
            if (!userCode.matches(emailReg)){//如果不符合邮箱格式
                return DtoUtil.returnFail("邮箱不符合规格","1205");
            }else{
                if (activationCode==null){//验证码为空，发送激活码
                    DtoUtil.returnSuccess("activationCode","userService.sendActivationMail(userCode)");
                }else{
                    if (activationCode.equals(checkActivationCode)){//判断验证码是否正确
                        user.setUserCode(userCode);
                        user.setUserPassword(password);
                        user.setUserType(0);
                        user.setActivated(1);
                        userService.insert(user);
                        return DtoUtil.returnSuccess("注册成功，请返回首页登录");
                    }else{
                        return DtoUtil.returnFail("验证码输入错误","1206");
                    }
                }
            }
        }else{
            if (!userCode.matches(phoneReg)){
                return DtoUtil.returnFail("号码不符合规格","1206");
            }else{
                if (activationCode!=null){
                    if (activationCode.equals(checkActivationCode)){//判断验证码是否正确
                        user.setUserCode(userCode);
                        user.setUserPassword(password);
                        user.setUserType(0);
                        user.setActivated(1);
                        userService.insert(user);
                        return DtoUtil.returnSuccess("注册成功，请返回首页登录");
                    }else{
                        return DtoUtil.returnFail("验证码输入错误","1206");
                    }
                }else{
                    HashMap<String, Object> result = null;
                    CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
                    restAPI.init("app.cloopen.com", "8883");
                    // 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
                    restAPI.setAccount("8aaf07086541761801655a28e4eb0edc", "f8809f8794b2400197a54abdcae0d8dd");
                    // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
                    restAPI.setAppId("AppId");
                    // 请使用管理控制台中已创建应用的APPID。
                    result = restAPI.sendTemplateSMS(userCode,"1",new String[]{"您的验证码为"+MD5.getMd5(new Date().toString(),6)});
                    System.out.println("SDKTestGetSubAccounts result=" + result);
                    if("000000".equals(result.get("statusCode"))){
                        //正常返回输出data包体信息（map）
                        HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                        Set<String> keySet = data.keySet();
                        for(String key:keySet){
                            Object object = data.get(key);
                            System.out.println(key +" = "+object);
                        }
                    }
                    return DtoUtil.returnSuccess("注册成功，请返回首页登录");
                }
            }
        }
        return DtoUtil.returnFail("注册失败","1203");
    }
    //注销的方法
    // @RequestMapping(value = "/logOut",method = RequestMethod.POST)
    public @ResponseBody Dto logOut(HttpServletRequest httpServletRequest){
        httpServletRequest.removeAttribute("user");
        return DtoUtil.returnSuccess();
    }

}
