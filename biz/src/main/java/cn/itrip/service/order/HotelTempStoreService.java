package cn.itrip.service.order;

import cn.itrip.beans.vo.store.StoreVO;

import java.util.List;
import java.util.Map;

public interface HotelTempStoreService {
    /**
     * 查询库存
     * */
    List<StoreVO> queryRoomStore(Map param);
}
