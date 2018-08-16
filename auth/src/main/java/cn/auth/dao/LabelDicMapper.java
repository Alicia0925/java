package cn.auth.dao;

import cn.auth.entity.LabelDic;

public interface LabelDicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LabelDic record);

    int insertSelective(LabelDic record);

    LabelDic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LabelDic record);

    int updateByPrimaryKey(LabelDic record);
}