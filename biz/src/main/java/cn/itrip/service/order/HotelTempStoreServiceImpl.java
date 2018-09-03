package cn.itrip.service.order;

import cn.itrip.beans.vo.order.ValidateRoomStoreVO;
import cn.itrip.beans.vo.store.StoreVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.dao.hoteltempstore.HotelTempStoreMapper;
import cn.itrip.dao.productstore.ProductStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotelTempStoreServiceImpl implements HotelTempStoreService {
    @Resource
    private HotelTempStoreMapper hotelTempStoreMapper;
    @Resource
    private ProductStoreMapper productStoreMapper;

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
    public List<StoreVO> validateRoomStore(ValidateRoomStoreVO validateRoomStoreVO) throws Exception {
        Integer count = validateRoomStoreVO.getCount();
        //刷新库存
       flushStore(validateRoomStoreVO);
        //查询库存
        Map<String,Object> param=new HashMap<>();
        param.put("roomId", validateRoomStoreVO.getRoomId());
        param.put("startTime",validateRoomStoreVO.getCheckInDate());
        param.put("endTime",validateRoomStoreVO.getCheckOutDate());
        List<StoreVO> storeVOList = hotelTempStoreMapper.queryRoomStore(param);
        if (EmptyUtils.isEmpty(storeVOList)) {
            return null;
        }
        for (StoreVO vo : storeVOList) {
            if (vo.getStore() < count) {
                return null;
            }
        }
        return storeVOList;
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
        return flag == 0;
    }

    /**
     * 预订前刷新实时库存
     * <p>
     * startTime
     * endTime
     * roomId
     * hotelId
     */
    @Override
    public void flushStore(ValidateRoomStoreVO vStoreVO) throws Exception {
        Date startTime = vStoreVO.getCheckInDate();
        Date endTime = vStoreVO.getCheckOutDate();
        Long roomId = vStoreVO.getRoomId();
        Long hotelId = vStoreVO.getHotelId();
        Date tempTime = null;
        Integer store1 = null;
        Integer count1 = null;
        tempTime = startTime;
        Long ms = tempTime.getTime();

        while (ms <= endTime.getTime()) {
            count1 = hotelTempStoreMapper.getCountByRoomIdAndRecordTime(roomId, tempTime);

            if (count1 == 0) {
                store1 = productStoreMapper.selectStoreByProductIdType(roomId, 1);
                Map<String, Object> param = new HashMap<>();
                param.put("hotelId1", hotelId);
                param.put("roomId1", roomId);
                param.put("tempTime", tempTime);
                param.put("store1", store1);
                hotelTempStoreMapper.addTempStore(param);
            }
            ms = tempTime.getTime() + 1000 * 60 * 60*24;

        }


    }
}
