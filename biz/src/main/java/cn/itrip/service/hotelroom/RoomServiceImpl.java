package cn.itrip.service.hotelroom;

import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.dao.hotelroom.HotelRoomMapper;

import javax.annotation.Resource;

public class RoomServiceImpl implements RoomService {
    @Resource
    private HotelRoomMapper hotelRoomMapper;

    @Override
    public HotelRoom getHotelRoomById(Long roomId) {
        return hotelRoomMapper.selectByPrimaryKey(roomId);
    }
}
