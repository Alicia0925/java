package cn.auth.dao;

import cn.auth.entity.HotelFeature;

public interface HotelFeatureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HotelFeature record);

    int insertSelective(HotelFeature record);

    HotelFeature selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelFeature record);

    int updateByPrimaryKey(HotelFeature record);
}