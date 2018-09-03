package cn.itrip.service.order;

import cn.itrip.beans.vo.order.ValidateRoomStoreVO;
import cn.itrip.beans.vo.store.StoreVO;

import java.util.List;
import java.util.Map;

public interface HotelTempStoreService {
    /***
     * 查库存
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
    List<StoreVO>  validateRoomStore(ValidateRoomStoreVO validateRoomStoreVO)throws Exception;
    /***
     *  startTime 开始时间
     *  endTime 结束时间
     *  roomId 房间ID
     *  count 预订数目
     * @return
     * @throws Exception
     */
     boolean updateRoomStore(Map<String, Object> param) throws Exception;



     void flushStore(ValidateRoomStoreVO validateRoomStoreVO)throws Exception;
}
