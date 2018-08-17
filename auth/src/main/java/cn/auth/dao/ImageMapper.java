package cn.auth.dao;

import cn.auth.entity.Image;

public interface ImageMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);
}