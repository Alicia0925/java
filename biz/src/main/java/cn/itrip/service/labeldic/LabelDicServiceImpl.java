package cn.itrip.service.labeldic;

import cn.itrip.beans.pojo.LabelDic;
import cn.itrip.dao.labeldic.LabelDicMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LabelDicServiceImpl implements LabelDicService {

    @Resource
    private LabelDicMapper labelDicMapper;

    //查询父类ID是酒店特色的标签字典列表
    @Override
    public List<LabelDic> getLabelDicS() throws Exception {
        return labelDicMapper.selectLabelDicS();
    }
}
