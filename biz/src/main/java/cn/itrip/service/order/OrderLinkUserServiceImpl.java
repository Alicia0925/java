package cn.itrip.service.order;

import cn.itrip.beans.vo.order.OrderLinkUserVo;
import cn.itrip.dao.orderlinkuser.OrderLinkUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class OrderLinkUserServiceImpl implements OrderLinkUserService{

    @Resource
    private OrderLinkUserMapper orderLinkUserMapper;


    @Override
    public List<OrderLinkUserVo> getOrderLinkUserListByOrderId(Long id) throws Exception {
        return orderLinkUserMapper.selectVoByOrderId(id);
    }
    /**
     *查询所有未支付状态下的旅客id
     * */
    @Override
    public List<Long> getOrderLinkUserIdByOrderId(Long userId, Integer orderStatus) throws Exception {
        return orderLinkUserMapper.selectOrderLinkUserIdByConditions(userId,orderStatus);
    }


}
