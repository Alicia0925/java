package cn.itrip.service.areadic;

import cn.itrip.beans.vo.AreaDicVO;


import java.util.List;

public interface AreaDicService {
    //查询国内外的热门城市
    List<AreaDicVO> selectAreaDicList(Integer isChina) throws Exception;
}
