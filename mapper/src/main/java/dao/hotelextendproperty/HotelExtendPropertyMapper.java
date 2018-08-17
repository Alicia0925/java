package dao.hotelextendproperty;

import cn.itrip.beans.pojo.HotelExtendProperty;

public interface HotelExtendPropertyMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelExtendProperty record);

    int insertSelective(HotelExtendProperty record);

    HotelExtendProperty selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelExtendProperty record);

    int updateByPrimaryKey(HotelExtendProperty record);
}