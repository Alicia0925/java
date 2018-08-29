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
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
public class TokenController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/retoken",method = RequestMethod.POST,produces = "application/json",headers = "token")
    public @ResponseBody Dto retoken(HttpServletRequest request) {
        try {
            String newToken = tokenService.replaceToken(request.getHeader("user-agent"),request.getHeader("token"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            TokenVo tokenVo = new TokenVo(newToken,
                    Calendar.getInstance().getTimeInMillis()+2*60*60*1000,
                    Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnDataSuccess(tokenVo);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }
    }

}
