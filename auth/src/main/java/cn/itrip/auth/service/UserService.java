package cn.itrip.auth.service;

import cn.itrip.beans.pojo.User;

public interface UserService {

    boolean deleteByPrimaryKey(Long id)throws Exception ;


    boolean add(User record)throws Exception ;


    boolean addSelective(User record) throws Exception;


    User findtByPrimaryKey(Long id) throws Exception;


    boolean modifyByPrimaryKeySelective(User record)throws Exception;


    boolean modifyByPrimaryKey(User record)throws Exception ;



    User findByUserCode(String userCode)throws Exception ;

    /**
     * login
     */

    public User login(String userCode, String userPassword) throws Exception;

    String sendActivationMail(String email)throws Exception ;

    boolean isActivationCodeTrue(String checkActivationCode,User user)throws Exception ;
    void addNewUser(User user,int isEmail) throws Exception;
}
