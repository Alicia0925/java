package cn.auth.dao;

import cn.auth.entity.HotelExtendProperty;

public interface HotelExtendPropertyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HotelExtendProperty record);

    int insertSelective(HotelExtendProperty record);

    HotelExtendProperty selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelExtendProperty record);

    int updateByPrimaryKey(HotelExtendProperty record);
}