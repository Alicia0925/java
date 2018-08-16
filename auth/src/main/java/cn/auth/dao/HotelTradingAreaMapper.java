package cn.auth.dao;

import cn.auth.entity.HotelTradingArea;

public interface HotelTradingAreaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HotelTradingArea record);

    int insertSelective(HotelTradingArea record);

    HotelTradingArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelTradingArea record);

    int updateByPrimaryKey(HotelTradingArea record);
}