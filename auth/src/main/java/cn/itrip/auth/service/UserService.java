package cn.itrip.auth.service;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.vo.ItripUserVo;

import java.util.List;

public interface UserService {
    /**
     * 查by UserCode
     */
    User findByUserCode(String userCode) throws Exception;

    /**
     * 查all
     */
    List<User> findAll() throws Exception;

    /**
     * 增 （有事务）
     */
    void itriptxCreateUser(User user) throws Exception;


    /**
     * 删
     */
    void deleteUser(Long userId) throws Exception;

    /**
     * 登录
     *
     */

     User login(String userCode, String password) throws Exception;

     /**
      * 注册
      *
      */
     Dto doRegister(ItripUserVo user)throws Exception;


     /**
      *邮箱激活
      */
    Dto active(String userCode,String activeCode)throws Exception;

}
