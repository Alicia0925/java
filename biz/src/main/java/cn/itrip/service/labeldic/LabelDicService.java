package cn.itrip.service.labeldic;

import cn.itrip.beans.pojo.LabelDic;

import java.util.List;

public interface LabelDicService {

    //根据父类ID查询标签字典列表
    List<LabelDic> getLabelDicS(Long parentId) throws Exception;

}
