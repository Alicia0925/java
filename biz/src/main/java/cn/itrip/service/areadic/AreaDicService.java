package cn.itrip.service.areadic;

import cn.itrip.beans.vo.AreaDicVO;


import java.util.List;

public interface AreaDicService {
    //查询国内外的热门城市
    List<AreaDicVO> getAreaDicList(Integer isChina) throws Exception;

    //根据城市id查询商圈
    List<AreaDicVO> getAreaDicListByCityId(Long cityId) throws Exception;
}
