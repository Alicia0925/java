package dao.areadic;

import cn.itrip.beans.pojo.AreaDic;

public interface AreaDicMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(AreaDic record);

    int insertSelective(AreaDic record);

    AreaDic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AreaDic record);

    int updateByPrimaryKey(AreaDic record);
}