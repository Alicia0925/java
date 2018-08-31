package cn.itrip.dao.image;

import cn.itrip.beans.pojo.Image;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);

    List<Image> selectByConditions(@Param("type")String type,
                                   @Param("targetId")Long targetId)throws Exception;
}