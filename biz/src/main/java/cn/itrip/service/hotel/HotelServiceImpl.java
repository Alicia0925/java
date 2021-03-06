package cn.itrip.service.hotel;

import cn.itrip.beans.pojo.AreaDic;
import cn.itrip.beans.pojo.HotelWithBLOBs;
import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.beans.vo.hotel.HotelVideoDescVO;
import cn.itrip.beans.vo.hotel.SearchDetailsHotelVO;
import cn.itrip.dao.hotel.HotelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Resource
    private HotelMapper hotelMapper;

    //根据酒店id获取酒店信息
    @Override
    public HotelWithBLOBs getHotelWithBLOBsById(Long hotelId) throws Exception {
        return hotelMapper.selectHotelWithBLOBsById(hotelId);
    }

    //根据酒店id查询酒店特色、商圈、酒店名称
    @Override
    public HotelVideoDescVO getVideoDescByHotelId(Long hotelId) throws Exception {

        HotelVideoDescVO hotelVideoDescVO = new HotelVideoDescVO();

        //将查询到的酒店名称封装到hotelVideoDescVO中
        hotelVideoDescVO.setHotelName(hotelMapper.selectHotelWithBLOBsById(hotelId).getHotelName());

        //将查询到的酒店商圈信息封装到hotelVideoDescVO中
        List<String> tradingAreaNames = new ArrayList<>();
        List<AreaDic> areaDicList = hotelMapper.selectHotelAreaDicByHotelId(hotelId);
        for (AreaDic reaDic:areaDicList) {
            tradingAreaNames.add(reaDic.getName());
        }
        hotelVideoDescVO.setTradingAreaNameList(tradingAreaNames);

        //将查询到的酒店特色信息封装到hotelVideoDescVO中
        List<String> hotelFeatureList =  new ArrayList<>();
        List<LabelDic> labelDicList = hotelMapper.selectHotelFeatureByHotelId(hotelId);
        for (LabelDic  labelDic:labelDicList) {
            hotelFeatureList.add(labelDic.getName());
        }
        hotelVideoDescVO.setTradingAreaNameList(tradingAreaNames);

        return hotelVideoDescVO;
    }

    //根据酒店的id查询酒店的特色和介绍
    @Override
    public List<SearchDetailsHotelVO> getHotelDetails(Long id) throws Exception {
        //获取酒店的介绍
        SearchDetailsHotelVO vo = new SearchDetailsHotelVO();
        List<SearchDetailsHotelVO> searchDetailsHotelVOS = new ArrayList<>();
        vo.setName("酒店介绍");
        vo.setDescription(hotelMapper.selectHotelWithBLOBsById(id).getDetails());
        searchDetailsHotelVOS.add(vo);
        //获取酒店的特色
        List<LabelDic> labelDicList = hotelMapper.selectHotelFeatureByHotelId(id);
        for (LabelDic labelDic:labelDicList) {
            SearchDetailsHotelVO vo2 = new SearchDetailsHotelVO();
            vo2.setName(labelDic.getName());
            vo2.setDescription(labelDic.getDescription());
            searchDetailsHotelVOS.add(vo2);
        }
        return searchDetailsHotelVOS;
    }
}
