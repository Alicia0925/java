package cn.itrip.dao.hoteltempstore;

import cn.itrip.beans.pojo.HotelTempStore;

public interface HotelTempStoreMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelTempStore record);

    int insertSelective(HotelTempStore record);

    HotelTempStore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelTempStore record);

    int updateByPrimaryKey(HotelTempStore record);
}