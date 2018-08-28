package cn.itrip.service.userlinkuser;


import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.common.Page;

import java.util.Map;

public interface HotelOrderService {
    /**用户个人订单列表分页展示*/

    Page<HotelOrderVO> getPageByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception;


}
