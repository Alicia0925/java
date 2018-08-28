package cn.itrip.common;

import cn.itrip.beans.pojo.User;
import com.alibaba.fastjson.JSONObject;

public class ValidationToken {

    private RedisAPI redisAPI;

    public RedisAPI getRedisAPI() {
        return redisAPI;
    }

    public void setRedisAPI(RedisAPI redisAPI) {
        this.redisAPI = redisAPI;
    }

    public User getCurrentUser(String tokenString){
        //根据token从redis中获取用户信息
         User itripUser = null;
        if(null == tokenString || "".equals(tokenString)){
            return null;
        }
        try{
            String userInfoJson = redisAPI.get(tokenString);
            itripUser = JSONObject.parseObject(userInfoJson, User.class);
        }catch(Exception e){
            itripUser = null;
        }
        return itripUser;
    }
}
