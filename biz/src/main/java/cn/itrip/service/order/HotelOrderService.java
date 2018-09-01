package cn.itrip.service.order;


import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.pojo.UserLinkUser;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import cn.itrip.common.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 酒店订单Service接口
 */
public interface HotelOrderService {
    /**
     * 用户个人订单列表分页展示
     */

    Page<HotelOrderVO> getPageByMap(SearchOrderVO searchOrderVO) throws Exception;

    /**
     * 获取个人订单详情
     */
    HotelOrder getItripHotelOrderById(Long id) throws Exception;

    /**
     * 获取个人订单详情--房间信息
     */
    PersonalOrderRoomVo getHotelOrderRoomById(Long id) throws Exception;

    /**
     * 刷新订单状态-定时任务
     */
    boolean flushOrderStatus(Integer type)throws Exception;
    /**
     * 订单总金额=入住天数*订单数量*房间价格
     *
     * */

    BigDecimal getOrderPayAmount(Integer count, Long roomId)throws Exception;

    /**
     * 生成订单
     * */

    Map<String,String> addHotelOrder(HotelOrder hotelOrder, List<UserLinkUser> linkUserList)throws Exception;
/**
 * 修改订单
 * */
    void modifyHotelOrder(HotelOrder hotelOrder);

    List<HotelOrder> getHotelOrderListByMap(Map<String,Object> orderParam);
}
