package cn.itrip.auth.service;

import cn.itrip.beans.pojo.User;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import com.alibaba.fastjson.JSON;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {
    @Resource
    private RedisAPI redisAPI;

    /**
     * 生成Token方法
     * 返回值的格式
     * 前缀：PC-USERCODE-USERID-CREATEDATE-RANDOM[6位]
     */
    @Override
    public String generateToken(String userAgent, User user) throws Exception {
        StringBuilder sb = new StringBuilder();
        //前缀
        sb.append("token:");
        //判断用户登录平台PC还是mobile
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        if (agent.getOperatingSystem().isMobileDevice())
            sb.append("MOBILE-");
        else sb.append("PC-");
        //MD5加密用户账户
        sb.append(MD5.getMd5(user.getUserCode(), 32) + "-");
        //userId
        sb.append(user.getId().toString()+"-");
        //
        //生成时间
        sb.append(new SimpleDateFormat("yyyyMMddHHmm").format(new Date())+"-");
        //6位随机
        sb.append(MD5.getMd5(userAgent,6));

        return sb.toString();
    }

    /**
     * 保存token
     * */
    @Override
    public void saveToken(String token, User user) throws Exception {
        //判断
        if(token.startsWith("token:PC-")){
            redisAPI.set(token,2*60*60, JSON.toJSONString(user));
        }else{
            redisAPI.set(token, JSON.toJSONString(user));
        }
    }
    /**验证token*/
    @Override
    public boolean validate(String userAgent, String token) throws Exception {

        if(!redisAPI.exist(token))
            return false;
        String agentMD5 =token.split("-")[4];
        if(!MD5.getMd5(userAgent,6).equals(agentMD5))
            return false;
        return true;
    }

    @Override
    public User load(String token) throws Exception{
        if (!redisAPI.exist(token))
            return null;
        return JSON.parseObject(redisAPI.get(token),User.class);
    }

    /**删除token*/
    @Override
    public void delete(String token) throws Exception{
        redisAPI.delete(token);
    }

    @Override
    public String replaceToken(String agent, String token) throws Exception {
        return null;
    }
}
