package cn.itrip.dao.hoteltempstore;

import cn.itrip.beans.pojo.HotelTempStore;
import cn.itrip.beans.vo.store.StoreVO;

import java.util.List;
import java.util.Map;

public interface HotelTempStoreMapper {

    /**
     * 查询当日库存信息
     */
    List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception;

    /**
     * 刷新库存
     */
    void flushStore(Map<String, Object> param) throws Exception;

    /**
     * 库存满足则锁定库存
     */
    Integer updateRoomStore(Map<String, Object> param) throws Exception;


    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelTempStore record);

    int insertSelective(HotelTempStore record);

    HotelTempStore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelTempStore record);

    int updateByPrimaryKey(HotelTempStore record);
}