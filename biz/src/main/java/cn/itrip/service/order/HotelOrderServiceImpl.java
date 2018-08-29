package cn.itrip.service.order;

import cn.itrip.beans.pojo.HotelOrder;
import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.beans.vo.order.PersonalOrderRoomVo;
import cn.itrip.beans.vo.order.SearchOrderVO;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelorder.HotelOrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 酒店订单Service实现类
 *
 * */
@Service
public class HotelOrderServiceImpl implements HotelOrderService {
    @Resource
    private HotelOrderMapper hotelOrderMapper;

    @Override
    public Page<HotelOrderVO> getPageByMap(SearchOrderVO searchOrderVO ) throws Exception {

        Integer total = hotelOrderMapper.CountOrderByMap(searchOrderVO );
        Page page = new Page(searchOrderVO.getPageNo(), searchOrderVO.getPageSize(), total);
        List< HotelOrderVO> itripHotelOrderList = hotelOrderMapper.selectByMap(searchOrderVO);
        page.setRows(itripHotelOrderList);
        return page;
    }

    @Override
    public HotelOrder getItripHotelOrderById(Long id) throws Exception {
        return hotelOrderMapper.selectByPrimaryKey(id);
    }
    @Override
    public PersonalOrderRoomVo getHotelOrderRoomById(Long id) throws Exception {
        if(id!=null){
            return hotelOrderMapper.getItripHotelOrderRoomInfoById(id);
        }
        return null;
    }



}
