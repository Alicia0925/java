package cn.auth.dao;

import cn.auth.entity.HotelOrder;

public interface HotelOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HotelOrder record);

    int insertSelective(HotelOrder record);

    HotelOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelOrder record);

    int updateByPrimaryKeyWithBLOBs(HotelOrder record);

    int updateByPrimaryKey(HotelOrder record);
}