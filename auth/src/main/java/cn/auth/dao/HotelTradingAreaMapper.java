package cn.auth.dao;

import cn.auth.entity.HotelTradingArea;

public interface HotelTradingAreaMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelTradingArea record);

    int insertSelective(HotelTradingArea record);

    HotelTradingArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelTradingArea record);

    int updateByPrimaryKey(HotelTradingArea record);
}