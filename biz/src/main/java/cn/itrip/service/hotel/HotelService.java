package cn.itrip.service.hotel;


import cn.itrip.beans.pojo.Hotel;
import cn.itrip.beans.pojo.HotelWithBLOBs;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.SearchDetailsHotelVO;

import java.util.List;

public interface HotelService {

    //根据酒店id获取酒店信息
    HotelWithBLOBs getHotelWithBLOBsById(Long hotelId) throws Exception;

    //根据酒店id查询酒店特色、商圈、酒店名称
    HotelVideoDescVO getVideoDescByHotelId(Long hotelId) throws Exception;

    //根据酒店的id查询酒店的特色和介绍
    List<SearchDetailsHotelVO> getHotelDetails(Long id)throws Exception;



}
