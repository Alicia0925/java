package cn.itrip.dao.hotelroom;

import cn.itrip.beans.pojo.HotelRoom;
import cn.itrip.beans.vo.hotel.SearchHotelRoomVO;

import java.util.List;

public interface HotelRoomMapper {

    /**
     * 通过条件查询酒店房间列表
     * @param searchHotelRoomVO 查询条件
     * @return 返回房间集合
     * @throws Exception 有异常抛出
     */
    List<HotelRoom> selectHotelRoomListByQuery(SearchHotelRoomVO searchHotelRoomVO)throws Exception;



    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelRoom record);

    int insertSelective(HotelRoom record);

    HotelRoom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelRoom record);

    int updateByPrimaryKey(HotelRoom record);
}