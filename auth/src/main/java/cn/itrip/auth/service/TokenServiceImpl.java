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
//            redisAPI.set(token,60, JSON.toJSONString(user));//测试时间
        }else{
            redisAPI.set(token, JSON.toJSONString(user));
        }
    }

    /**验证token*/
    @Override
    public boolean validate(String userAgent, String token) throws Exception {
        if(!redisAPI.exist(token))
            return false;
        String tokenInfo[] = token.split("-");
        String genTime = tokenInfo[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        if ((Long.parseLong(sdf.format(new Date()))-Long.parseLong(genTime))>120)//判断是否超时
            return false;
        String agentMD5 =tokenInfo[4];
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

    /**
     *  置换token
     * @param agent
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public String replaceToken(String agent, String token) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        if (!redisAPI.exist(token))
            //token不存在
            throw new Exception ();
        String[] tokenInfo = token.split("-");
        String now = sdf.format(new Date());
        if (((Long.parseLong(now)-Long.parseLong(tokenInfo[3]))-60)>0){
            //可以置换
            Long restTime = redisAPI.ttl(token);
            User user = load(token);
            if (restTime>0||restTime==-1){
                redisAPI.set(token,120,JSON.toJSONString(user));//旧token设置2分钟过期
                String newToken = generateToken(agent,user);
                saveToken(newToken,user);//保存新token
                return newToken;
            }else{
                //token不存在
                throw new Exception ();
            }
        }else{
            throw new Exception ();
//            return "token剩余时间在安全时间内，不必置换";
        }
    }
}
