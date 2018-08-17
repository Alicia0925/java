package dao.labeldic;

import cn.itrip.beans.pojo.LabelDic;

public interface LabelDicMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(LabelDic record);

    int insertSelective(LabelDic record);

    LabelDic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LabelDic record);

    int updateByPrimaryKey(LabelDic record);
}