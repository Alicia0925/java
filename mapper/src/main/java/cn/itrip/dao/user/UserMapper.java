package cn.itrip.dao.user;

import cn.itrip.beans.pojo.User;

public interface UserMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}