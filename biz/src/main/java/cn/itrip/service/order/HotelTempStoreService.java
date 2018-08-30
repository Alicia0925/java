package cn.itrip.service.order;

import cn.itrip.beans.vo.store.StoreVO;

import java.util.List;
import java.util.Map;

public interface HotelTempStoreService {
    /***
     *  startTime 开始时间
     *  endTime 结束时间
     *  roomId 房间ID
     *  hotelId 酒店ID
     * @return
     * @throws Exception
     */
     List<StoreVO> queryRoomStore(Map<String,Object> param)throws Exception;
    /***
     *  startTime 开始时间
     *  endTime 结束时间
     *  roomId 房间ID
     *  hotelId 酒店ID
     *  count 预订数目
     * @return
     * @throws Exception
     */
     boolean validateRoomStore(Map<String,Object> param)throws Exception;
    /***
     *  startTime 开始时间
     *  endTime 结束时间
     *  roomId 房间ID
     *  count 预订数目
     * @return
     * @throws Exception
     */
     boolean updateRoomStore(Map<String, Object> param) throws Exception;
}
