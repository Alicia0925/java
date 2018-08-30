package cn.itrip.service.order;

import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.dao.hoteltempstore.HotelTempStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class HotelTempStoreServiceImpl implements HotelTempStoreService {
    @Resource
    private HotelTempStoreMapper hotelTempStoreMapper;

    /***
     * 库存list
     *  startTime 开始时间
     *  endTime   结束时间
     *  roomId    房间ID
     *  hotelId   酒店ID
     * @return
     * @throws Exception
     */
    @Override
    public List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception {
        return hotelTempStoreMapper.queryRoomStore(param);
    }

    /***
     *
     * 是否有库存
     *  startTime 开始时间
     *  endTime   结束时间
     *  roomId    房间ID
     *  hotelId   酒店ID
     *  count     数目
     * @return
     * @throws Exception
     */
    @Override
    public boolean validateRoomStore(Map<String, Object> param) throws Exception {
        Integer count = (Integer) param.get("count");
        //刷新库存
        hotelTempStoreMapper.flushStore(param);
        //查询库存
        List<StoreVO> storeVOList = hotelTempStoreMapper.queryRoomStore(param);
        if(EmptyUtils.isEmpty(storeVOList)){
            return false;
        }
        for (StoreVO vo : storeVOList) {
            if (vo.getStore() < count) {
                return false;
            }
        }
        return true;
    }

    /***
     * 更新锁定库存
     *  startTime 开始时间
     *  endTime   结束时间
     *  roomId    房间ID
     *  count     数目
     * @return
     * @throws Exception
     */
    public boolean updateRoomStore(Map<String, Object> param) throws Exception {
        Integer flag = hotelTempStoreMapper.updateRoomStore(param);
        return flag == 0 ? false : true;
    }
}
