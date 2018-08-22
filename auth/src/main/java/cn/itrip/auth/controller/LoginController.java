package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.vo.TokenVo;
import cn.itrip.common.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import utils.DtoUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
public class LoginController {
    @Resource
    private TokenService tokenService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Dto login(@RequestParam String name,
              @RequestParam String password,
              HttpServletRequest request) {
        try {
            User user = userService.login(name, MD5.getMd5(password, 32));

            if (null != user) {
                String userAgent = request.getHeader("user-agent");
                String token = tokenService.generateToken(userAgent, user);
                tokenService.saveToken(token, user);
                TokenVo vo = new TokenVo(token, Calendar.getInstance().getTimeInMillis() + 2 * 60 * 60 * 1000, Calendar.getInstance().getTimeInMillis());
                return DtoUtil.returnDataSuccess(vo);

            } else {
                return DtoUtil.returnFail("用户密码错误", "10034");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), "10034");
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, headers = "token", produces = "application/json")
    public @ResponseBody
    Dto logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            boolean result = tokenService.validate(request.getHeader("user-agent"), token);
            if (result) {
                tokenService.delete(token);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
