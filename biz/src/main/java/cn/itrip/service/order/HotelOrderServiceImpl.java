package cn.itrip.service.order;

import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.pojo.UserLinkUser;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelorder.HotelOrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 酒店订单Service实现类
 */
@Service
public class HotelOrderServiceImpl implements HotelOrderService {
    @Resource
    private HotelOrderMapper hotelOrderMapper;

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
    public boolean flushOrderStatus(int i)throws Exception {
        return false;
    }

    /**
     * 计算订单金额
     */
    @Override
    public BigDecimal getOrderPayAmount(int i, Long roomId)throws Exception {



        return null;
    }

    /**
     * 生成订单
     */
    @Override
    public Map<String, String> addHotelOrder(HotelOrder hotelOrder, List<UserLinkUser> linkUserList)throws Exception {
        return null;
    }


}
