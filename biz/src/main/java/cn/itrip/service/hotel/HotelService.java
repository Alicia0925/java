package cn.itrip.service.hotel;

import cn.itrip.beans.pojo.Hotel;

public interface HotelService {


    Hotel getHotelById(Long hotelId)throws Exception;
}
