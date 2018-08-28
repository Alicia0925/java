package cn.itrip.service.userlinkuser;

import cn.itrip.beans.vo.order.HotelOrderVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.hotelorder.HotelOrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class HotelOrderServiceImpl implements HotelOrderService {
    @Resource
    private HotelOrderMapper hotelOrderMapper;

    @Override
    public Page<HotelOrderVO> getPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = hotelOrderMapper.CountOrderByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.CURRENT_PAGE : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("offset", page.getOffset());
        param.put("pageSize", page.getPageSize());
        List< HotelOrderVO> itripHotelOrderList = hotelOrderMapper.selectByMap(param);
        page.setRows(itripHotelOrderList);
        return page;
    }



}
