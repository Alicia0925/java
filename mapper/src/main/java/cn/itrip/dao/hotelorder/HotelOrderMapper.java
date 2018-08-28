package cn.itrip.dao.hotelorder;

import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.vo.order.HotelOrderVO;

import java.util.List;
import java.util.Map;

public interface HotelOrderMapper {
    /**
     * 获取个人订单列表
     */

    List<HotelOrderVO> selectByMap(Map<String, Object> params) throws Exception;

    Integer CountOrderByMap(Map<String, Object> params) throws Exception;


    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id)throws Exception;

    int insert(HotelOrder record)throws Exception;

    int insertSelective(HotelOrder record)throws Exception;

    HotelOrder selectByPrimaryKey(Long id)throws Exception;

    int updateByPrimaryKeySelective(HotelOrder record)throws Exception;

    int updateByPrimaryKeyWithBLOBs(HotelOrder record)throws Exception;

    int updateByPrimaryKey(HotelOrder record)throws Exception;
}