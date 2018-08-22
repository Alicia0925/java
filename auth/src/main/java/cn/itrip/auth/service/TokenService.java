package cn.itrip.auth.service;

import cn.itrip.beans.pojo.User;

public interface TokenService {
   /**生成token*/
    String generateToken(String userAgent,User user)throws Exception;

    /**保存token*/
    void saveToken(String token,User user)throws Exception;
/**验证Token*/
    boolean validate(String userAgent,String token)throws Exception;

    void delete(String token);
}