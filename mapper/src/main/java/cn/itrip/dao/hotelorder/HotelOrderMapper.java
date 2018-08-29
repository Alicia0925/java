package cn.itrip.dao.hotelorder;

import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalHotelOrderVo;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotelOrderMapper {
    /**
     * 获取个人订单列表
     */

    List<HotelOrderVO> selectByMap(SearchOrderVO searchOrderVO) throws Exception;

    Integer CountOrderByMap(SearchOrderVO searchOrderVO) throws Exception;

    /**
     * 个人订单详情-房间信息
     */
    PersonalOrderRoomVo getItripHotelOrderRoomInfoById(@Param("id") Long id) throws Exception;
    /**
     * 个人订单详情
     * */




    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id) throws Exception;

    int insert(HotelOrder record) throws Exception;

    int insertSelective(HotelOrder record) throws Exception;

    HotelOrder selectByPrimaryKey(Long id) throws Exception;

    int updateByPrimaryKeySelective(HotelOrder record) throws Exception;

    int updateByPrimaryKeyWithBLOBs(HotelOrder record) throws Exception;

    int updateByPrimaryKey(HotelOrder record) throws Exception;
}