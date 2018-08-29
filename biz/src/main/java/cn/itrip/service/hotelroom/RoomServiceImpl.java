package cn.itrip.service.hotelroom;

import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.dao.hotelroom.HotelRoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class RoomServiceImpl implements RoomService {
    @Resource
    private HotelRoomMapper hotelRoomMapper;

    @Override
    public HotelRoom getHotelRoomById(Long roomId) {
        return hotelRoomMapper.selectByPrimaryKey(roomId);
    }
}
