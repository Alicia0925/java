package cn.itrip.dao.orderlinkuser;

import cn.itrip.beans.pojo.OrderLinkUser;
import cn.itrip.beans.vo.order.OrderLinkUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface OrderLinkUserMapper {
    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id)throws Exception;

    int insert(OrderLinkUser record)throws Exception;

    int insertSelective(OrderLinkUser record)throws Exception;

    OrderLinkUser selectByPrimaryKey(Long id)throws Exception;

    int updateByPrimaryKeySelective(OrderLinkUser record)throws Exception;

    int updateByPrimaryKey(OrderLinkUser record)throws Exception;

   /**
    *
    * @param orderId
    * @return
    * */
   List<OrderLinkUser> selectByOrderId(Long orderId)throws Exception;

   List<OrderLinkUserVo> selectVoByOrderId(Long orderId) throws Exception;
/**
 *查询所有未支付状态下的旅客id
 * */
   List<Long> selectOrderLinkUserIdByConditions(@Param("userId") Long userId,
                                             @Param("orderStatus") Integer orderStatus)throws Exception;
/**
 * 根据订单编号删除联系人
 * */
    Integer deleteOrderLinkUserByOrderId(Long orderId);
}