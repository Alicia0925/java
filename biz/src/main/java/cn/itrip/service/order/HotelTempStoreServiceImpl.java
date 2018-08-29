package cn.itrip.service.order;

import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.dao.hoteltempstore.HotelTempStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class HotelTempStoreServiceImpl implements HotelTempStoreService {
    @Resource
    private HotelTempStoreMapper hotelTempStoreMapper;

    @Override
    public List<StoreVO> queryRoomStore(Map param) {
        return null;
    }
}
