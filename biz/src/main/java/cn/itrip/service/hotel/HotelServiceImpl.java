package cn.itrip.service.hotel;

import cn.itrip.beans.pojo.Hotel;
import cn.itrip.dao.hotel.HotelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HotelServiceImpl implements HotelService {
    @Resource
    private HotelMapper hotelMapper;
    @Override
    public Hotel getHotelById(Long hotelId) throws Exception {
        return hotelMapper.selectByPrimaryKey(hotelId);
    }
}
