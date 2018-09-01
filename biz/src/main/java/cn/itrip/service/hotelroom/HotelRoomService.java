package cn.itrip.service.hotelroom;

import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.beans.vo.hotel.SearchHotelRoomVO;

import java.util.List;

public interface HotelRoomService {

    //通过条件查询酒店房间列表
    List<HotelRoom> getHotelRoomListByQuery(SearchHotelRoomVO searchHotelRoomVO)throws Exception;

    HotelRoom getHotelRoomById(Long roomId);
}
