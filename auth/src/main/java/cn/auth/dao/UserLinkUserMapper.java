package cn.auth.dao;

import cn.auth.entity.UserLinkUser;

public interface UserLinkUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLinkUser record);

    int insertSelective(UserLinkUser record);

    UserLinkUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLinkUser record);

    int updateByPrimaryKey(UserLinkUser record);
}