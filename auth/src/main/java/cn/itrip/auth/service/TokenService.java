package cn.itrip.auth.service;

import cn.itrip.beans.pojo.User;

public interface TokenService {
    /**
     * 会话超时时间
     */
    public final static int SESSION_TIMEOUT=2*60*60;//默认2h
    /**
     * 测试用会话超时时间
     */
    public final static int TEST_TIMEOUT=60;
    /**
     * 置换保护时间
     */
    public final static int REPLACEMENT_PROTECTION_TIMEOUT=60*60;//默认1h
    /**
     * 旧token延迟过期时间
     */
    public final static int REPLACEMENT_DELAY=2*60;//默认2min
    /**生成token*/
    String generateToken(String userAgent,User user)throws Exception;
    /**保存token*/
    void saveToken(String token,User user)throws Exception;
    /**验证Token*/
    boolean validate(String userAgent,String token)throws Exception;

    /**
     * 从redis里获取用户
     * @param token
     * @return
     */
    User load(String token)throws Exception;

    /**
     * 删除token
     * @param token
     */
    void delete(String token)throws Exception;

    /**
     * 置换token
     * @param agent
     * @param token
     * @return
     * @throws Exception
     */
    String replaceToken(String agent,String token) throws Exception;
}