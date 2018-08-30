package cn.itrip.dao.hotelorder;

import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     */

    HotelOrder selectByPrimaryKey(Long id) throws Exception;

    /**
     * 查询个人所有未支付状态的订单ID
     */

    List<Long> selectOrderIdByUserId(@Param("userId") Long userId,
                                     @Param("orderStatus") Integer orderStatus) throws Exception;

    /***
     * 刷新取消订单状态(用于定时程序)
     * @return
     * @throws Exception
     */
    Integer flushCancelOrderStatus() throws Exception;

    /***
     * 刷新已入住订单状态(用于定时程序)
     * @return
     * @throws Exception
     */
    Integer flushSuccessOrderStatus() throws Exception;

    /**
     * 更改订单状态为已点评
     */
    Integer updateHotelOrderStatus(@Param(value = "id")Long id,
                                   @Param(value = "modifiedBy")Long modifiedBy) throws Exception;


    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id) throws Exception;

    int insert(HotelOrder record) throws Exception;

    int insertSelective(HotelOrder record) throws Exception;


    int updateByPrimaryKeySelective(HotelOrder record) throws Exception;

    int updateByPrimaryKeyWithBLOBs(HotelOrder record) throws Exception;

    int updateByPrimaryKey(HotelOrder record) throws Exception;

    List<HotelOrder> getHotelOrderListByMap(Map<String,Object> param);

    void updateHotelOrder(HotelOrder hotelOrder);
}