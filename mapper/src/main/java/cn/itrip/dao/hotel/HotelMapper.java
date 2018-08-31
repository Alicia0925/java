package cn.itrip.dao.hotel;

import cn.itrip.beans.pojo.AreaDic;
import cn.itrip.beans.pojo.Hotel;
import cn.itrip.beans.pojo.HotelWithBLOBs;
import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.SearchFacilitiesHotelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotelMapper {

    /**
     * 根据酒店id查询酒店信息
     * @param hotelId 参数是酒店Id
     * @return 返回结果封装到 HotelWithBLOBs 中
     * @throws Exception 若有异常抛出
     */
    HotelWithBLOBs selectHotelWithBLOBsById(@Param(value = "hotelId") Long hotelId) throws Exception;

    /**
     *  根据酒店ID获取商圈信息
     * @param hotelId 参数是酒店Id
     * @return 返回结果封装到VO中
     * @throws Exception 若有异常抛出
     */
    List<AreaDic> selectHotelAreaDicByHotelId(@Param(value = "hotelId") Long hotelId)throws Exception;

    /**
     *  根据酒店ID获取酒店特色信息
     * @param hotelId 参数是酒店Id
     * @return 返回结果封装到VO中
     * @throws Exception 若有异常抛出
     */
    List<LabelDic> selectHotelFeatureByHotelId(@Param(value = "hotelId") Long hotelId)throws Exception;




    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(HotelWithBLOBs record);

    int insertSelective(HotelWithBLOBs record);

    HotelWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HotelWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(HotelWithBLOBs record);

    int updateByPrimaryKey(Hotel record);
}