package cn.itrip.service.order;

import cn.itrip.beans.vo.order.OrderLinkUserVo;

import java.util.List;

/**
 * 订单旅客Service
 */
public interface OrderLinkUserService {

    /**
     * 根据orderId查旅客信息
     */
    List<OrderLinkUserVo> getOrderLinkUserListByOrderId(Long id) throws Exception;

    /**
     * 查询用户id所有未支付订单id
     * 根据订单ID查询的旅客id
     * */
    List<Long> getOrderLinkUserIdByOrderId(Long userId,Integer orderStatus)throws Exception;



}
