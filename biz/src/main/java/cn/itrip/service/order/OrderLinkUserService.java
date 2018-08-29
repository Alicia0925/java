package cn.itrip.service.order;

import cn.itrip.beans.vo.order.OrderLinkUserVo;

import java.util.List;
import java.util.Map;

/**
 *
 * 订单旅客Service
 *
 * */
public interface OrderLinkUserService {


    List<OrderLinkUserVo> getOrderLinkUserListByMap(Map<String,Object> param);
}
