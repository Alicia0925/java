package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.beans.dto.Dto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import utils.DtoUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class TokenController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/validateToken",method = RequestMethod.GET,produces = "application/json",headers = "token")
    public @ResponseBody Dto validate(HttpServletRequest request){
        try {
          boolean result=  tokenService.validate(request.getHeader("user-agent"),request.getHeader("token"));
        if(result)
            return DtoUtil.returnSuccess("token有效");
        else
            return DtoUtil.returnSuccess("token无效");


        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),"10034");
        }

    }

}
