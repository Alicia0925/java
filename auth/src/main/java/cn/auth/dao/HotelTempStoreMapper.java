package cn.auth.dao;

import cn.auth.entity.HotelTempStore;

public interface HotelTempStoreMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HotelTempStore record);

    int insertSelective(HotelTempStore record);

    HotelTempStore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelTempStore record);

    int updateByPrimaryKey(HotelTempStore record);
}