package cn.itrip.service.labeldic;

import cn.itrip.beans.pojo.LabelDic;

import java.util.List;

public interface LabelDicService {

    //查询父类ID是酒店特色的标签字典列表
    List<LabelDic> selectLabelDicS() throws Exception;

}
