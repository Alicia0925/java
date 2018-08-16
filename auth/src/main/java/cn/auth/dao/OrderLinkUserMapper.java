package cn.auth.dao;

import cn.auth.entity.OrderLinkUser;

public interface OrderLinkUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderLinkUser record);

    int insertSelective(OrderLinkUser record);

    OrderLinkUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderLinkUser record);

    int updateByPrimaryKey(OrderLinkUser record);
}