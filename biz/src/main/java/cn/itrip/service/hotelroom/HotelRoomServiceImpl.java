package cn.itrip.service.hotelroom;

import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.beans.vo.hotel.SearchHotelRoomVO;
import cn.itrip.dao.hotelroom.HotelRoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {
    @Resource
    private HotelRoomMapper hotelRoomMapper;

    //通过条件查询酒店房间列表
    @Override
    public List<HotelRoom> getHotelRoomListByQuery(SearchHotelRoomVO searchHotelRoomVO) throws Exception {
        return hotelRoomMapper.selectHotelRoomListByQuery(searchHotelRoomVO);
    }
/**
 * 根据id查房间
 * */
    @Override
    public HotelRoom getHotelRoomById(Long roomId) {
        return hotelRoomMapper.selectByPrimaryKey(roomId);
    }

}
