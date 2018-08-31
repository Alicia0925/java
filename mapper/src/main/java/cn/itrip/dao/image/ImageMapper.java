package cn.itrip.dao.image;

import cn.itrip.beans.pojo.Image;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper {

    /**
     * 根据targetId查询酒店房型图片(type=1)
     * @param targetId 查询条件
     * @return 返回图片集合
     * @throws Exception 有异常抛出
     */
    List<Image> selectImageList(@Param(value = "targetId" ) Long targetId)throws Exception;



    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);
}