package cn.itrip.service.order;

import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.pojo.OrderLinkUser;
import cn.itrip.beans.pojo.UserLinkUser;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import cn.itrip.common.BigDecimalUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelorder.HotelOrderMapper;
import cn.itrip.dao.hotelroom.HotelRoomMapper;
import cn.itrip.dao.hoteltempstore.HotelTempStoreMapper;
import cn.itrip.dao.orderlinkuser.OrderLinkUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * 酒店订单Service实现类
 */
@Service
public class HotelOrderServiceImpl implements HotelOrderService {
    @Resource
    private HotelOrderMapper hotelOrderMapper;
    @Resource
    private HotelRoomMapper hotelRoomMapper;
    @Resource
    private OrderLinkUserMapper orderLinkUserMapper;
    @Resource
    private HotelTempStoreMapper tempStoreMapper;


    @Override
    public Page<HotelOrderVO> getPageByMap(SearchOrderVO searchOrderVO) throws Exception {

        Integer total = hotelOrderMapper.CountOrderByMap(searchOrderVO);
        Page page = new Page(searchOrderVO.getPageNo(), searchOrderVO.getPageSize(), total);
        List<HotelOrderVO> hotelOrderList = hotelOrderMapper.selectByMap(searchOrderVO);
        page.setRows(hotelOrderList);
        return page;
    }

    @Override
    public HotelOrder getItripHotelOrderById(Long id) throws Exception {
        return hotelOrderMapper.selectByPrimaryKey(id);
    }

    @Override
    public PersonalOrderRoomVo getHotelOrderRoomById(Long id) throws Exception {
        if (id != null) {
            return hotelOrderMapper.getItripHotelOrderRoomInfoById(id);
        }
        return null;
    }

    /**
     * 刷新订单状态
     */
    @Override
    public boolean flushOrderStatus(Integer type) throws Exception {
        Integer flag;
        if (type == 1) {
            flag = hotelOrderMapper.flushCancelOrderStatus();
        } else {
            flag = hotelOrderMapper.flushSuccessOrderStatus();
        }
        return flag > 0;
    }


    /**
     * 计算订单金额
     */
    @Override
    public BigDecimal getOrderPayAmount(Integer count, Long roomId) throws Exception {
        BigDecimal payAmount = null;
        BigDecimal roomPrice = hotelRoomMapper.selectByPrimaryKey(roomId).getRoomPrice();
        payAmount = BigDecimalUtil.OperationASMD(count, roomPrice,
                BigDecimalUtil.BigDecimalOprations.multiply,
                2, ROUND_DOWN);
        return payAmount;
    }

    /**
     * 生成订单:
     * 需判断订单是否有ID；
     * 若有 需要先删除之前旅客信息再更新旅客信息
     */
    @Override
    public Map<String, String> addHotelOrder(HotelOrder hotelOrder, List<UserLinkUser> linkUserList) throws Exception {

        //定义变量map，里面存放订单的id和orderNo返回给前端
        Map<String, String> map = new HashMap<>();
        if (null != hotelOrder) {
            int flag = 0;
            if (EmptyUtils.isNotEmpty(hotelOrder.getId())) {
                //删除联系人
                orderLinkUserMapper.deleteOrderLinkUserByOrderId(hotelOrder.getId());
                hotelOrder.setModifyDate(new Date());
                flag = hotelOrderMapper.updateHotelOrder(hotelOrder);
            } else {
                hotelOrder.setCreationDate(new Date());
                flag = hotelOrderMapper.insertSelective(hotelOrder);
            }
            if (flag > 0) {
                Long orderId = hotelOrder.getId();
                //添加订单之后还需要往订单与常用联系人关联表中添加记录
                if (orderId > 0) {
                    for (UserLinkUser userLinkUser : linkUserList) {
                        OrderLinkUser orderLinkUser = new OrderLinkUser();
                        orderLinkUser.setOrderId(orderId);
                        orderLinkUser.setLinkUserId(userLinkUser.getId());
                        orderLinkUser.setLinkUserName(userLinkUser.getLinkUserName());
                        orderLinkUser.setCreationDate(new Date());
                        orderLinkUser.setCreatedBy(hotelOrder.getCreatedBy());
                        orderLinkUserMapper.insertSelective(orderLinkUser);
                    }
                }
                map.put("id", hotelOrder.getId().toString());
                map.put("orderNo", hotelOrder.getOrderNo());
                return map;
            }
        }
        return map;
    }
/**
 * 更改order支付类型，订单状态，并锁定库存
 *
 * */
    @Override
    public Integer modifyHotelOrder(HotelOrder hotelOrder) throws Exception {
        HotelOrder modifyHotelOrder = hotelOrderMapper.selectByPrimaryKey(hotelOrder.getId());
        //更新临时表的库存
        Map<String, Object> roomStoreMap = new HashMap<>();
        roomStoreMap.put("startTime", modifyHotelOrder.getCheckInDate());
        roomStoreMap.put("endTime", modifyHotelOrder.getCheckOutDate());
        roomStoreMap.put("count", modifyHotelOrder.getCount());
        roomStoreMap.put("roomId", modifyHotelOrder.getRoomId());
        tempStoreMapper.updateRoomStore(roomStoreMap);
        return hotelOrderMapper.updateByPrimaryKeySelective(hotelOrder);
    }

    @Override
    public List<HotelOrder> getHotelOrderListByMap(Map<String, Object> orderParam) throws Exception {
        return hotelOrderMapper.getHotelOrderListByMap(orderParam);
    }

    @Override
    public boolean modifyOrderStatus(Long id,Long modifyBy)throws Exception {

        return hotelOrderMapper.updateHotelOrderStatus(id,modifyBy)>0;
    }


}
