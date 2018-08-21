package cn.itrip.auth.service;

import cn.itrip.beans.pojo.User;

public interface UserService {
    boolean deleteByPrimaryKey(Long id);

    boolean insert(User record);

    boolean insertSelective(User record);

    User selectByPrimaryKey(Long id);

    boolean updateByPrimaryKeySelective(User record);

    boolean updateByPrimaryKey(User record);

    User selectByUserCode(String userCode);

    String sendActivationMail(String email);
}
