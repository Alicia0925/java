package cn.itrip.dao.hotelorder;

import cn.itrip.beans.pojo.HotelOrder;

public interface HotelOrderMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelOrder record);

    int insertSelective(HotelOrder record);

    HotelOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelOrder record);

    int updateByPrimaryKeyWithBLOBs(HotelOrder record);

    int updateByPrimaryKey(HotelOrder record);
}