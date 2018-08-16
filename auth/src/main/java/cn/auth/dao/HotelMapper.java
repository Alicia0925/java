package cn.auth.dao;

import cn.auth.entity.Hotel;
import cn.auth.entity.HotelWithBLOBs;

public interface HotelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HotelWithBLOBs record);

    int insertSelective(HotelWithBLOBs record);

    HotelWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(HotelWithBLOBs record);

    int updateByPrimaryKey(Hotel record);
}