package cn.itrip.service.areadic;

import cn.itrip.beans.vo.AreaDicVO;
import cn.itrip.dao.areadic.AreaDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AreaDicServiceImpl implements AreaDicService {
    @Resource
    private AreaDicMapper areaDicMapper;

    //查询国内外的热门城市
    @Override
    public List<AreaDicVO> selectAreaDicList(Integer isChina) throws Exception {
        return areaDicMapper.selectAreaDicList(isChina);
    }
}
