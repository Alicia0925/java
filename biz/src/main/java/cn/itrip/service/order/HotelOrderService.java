package cn.itrip.service.order;


import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import cn.itrip.common.Page;
/**
 * 酒店订单Service接口
 * */
public interface HotelOrderService {
    /**
     * 用户个人订单列表分页展示
     */

    Page<HotelOrderVO> getPageByMap(SearchOrderVO searchOrderVO) throws Exception;

    /**
     * 获取个人订单详情
     */
    HotelOrder getItripHotelOrderById(Long id)throws Exception;
    /**
     * 获取个人订单详情--房间信息
     */
    PersonalOrderRoomVo getHotelOrderRoomById(Long id) throws Exception;


}
