package cn.itrip.dao.userlinkuser;

import cn.itrip.beans.pojo.UserLinkUser;

public interface UserLinkUserMapper {

    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(UserLinkUser record);

    int insertSelective(UserLinkUser record);

    UserLinkUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLinkUser record);

    int updateByPrimaryKey(UserLinkUser record);
}