package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.vo.TokenVo;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
public class TokenController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/retoken",method = RequestMethod.POST,produces = "application/json",headers = "token")
    public @ResponseBody Dto validate(HttpServletRequest request){
        try {
          boolean result=  tokenService.validate(request.getHeader("user-agent"),request.getHeader("token"));
        if(result){
            String newToken = tokenService.replaceToken(request.getHeader("user-agent"),request.getHeader("token"));
            TokenVo tokenVo=new TokenVo(newToken,
                    Calendar.getInstance().getTimeInMillis()+TokenService.SESSION_TIMEOUT*1000,//2h有效期
                    Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnSuccess("token已置换",tokenVo);
        }
        tokenService.delete("activationCode");
        return DtoUtil.returnFail("token无效",ErrorCode.AUTH_TOKEN_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_REPLACEMENT_FAILED);
        }

    }

}
