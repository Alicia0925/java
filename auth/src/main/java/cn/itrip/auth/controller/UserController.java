package cn.itrip.auth.controller;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
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
 */
@Controller
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 跳转到注册页面
     */
    @RequestMapping(value="/register",method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    /**
     * 使用邮箱注册
     *
     * @param userVO
     * @return
     */
    @ApiOperation(value = "使用邮箱注册", httpMethod = "POST", protocols = "HTTP", produces = "application/json",
            response = Dto.class)
    @RequestMapping(value = "/doRegister", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Dto doRegister(@ApiParam(name = "userVO", value = "用户实体", required = true)
                   @RequestBody ItripUserVo userVO) {

        Dto dto = new Dto();
        if (userVO != null) {
            try {
                User user = userService.findByUserCode(userVO.getUserCode());
                if (user != null) {
                    dto.setData(user);
                    dto.setErrorCode("10003");
                    dto.setMsg("该账户已存在，请直接登录");
                    return dto;
                } else {
                    dto.setMsg("该账户可用！");
                    dto.setErrorCode("10000");
                    return dto;

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        dto.setErrorCode("10004");
        dto.setMsg("注册账户不合法，请检查后注册！");

        return dto;

    }


}
