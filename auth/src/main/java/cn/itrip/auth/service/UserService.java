package cn.itrip.auth.service;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;

public interface UserService {


    boolean add(User record)throws Exception ;

    User findByUserCode(String userCode)throws Exception ;

    String sendActivationMail(String email)throws Exception ;

    void addNewUser(User user,int isEmail) throws Exception;

    Integer updateByPrimaryKey(User user) throws Exception;

    Dto ckAcCode(String code,String userCode);
}
