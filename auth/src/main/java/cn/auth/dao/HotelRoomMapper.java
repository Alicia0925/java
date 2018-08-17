package cn.auth.dao;

import cn.auth.entity.HotelRoom;

public interface HotelRoomMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelRoom record);

    int insertSelective(HotelRoom record);

    HotelRoom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelRoom record);

    int updateByPrimaryKey(HotelRoom record);
}