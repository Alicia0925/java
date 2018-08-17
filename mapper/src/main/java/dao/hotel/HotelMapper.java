package dao.hotel;

import cn.itrip.beans.pojo.Hotel;
import cn.itrip.beans.pojo.HotelWithBLOBs;

public interface HotelMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelWithBLOBs record);

    int insertSelective(HotelWithBLOBs record);

    HotelWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(HotelWithBLOBs record);

    int updateByPrimaryKey(Hotel record);
}