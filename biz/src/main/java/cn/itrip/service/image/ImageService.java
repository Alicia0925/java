package cn.itrip.service.image;

import cn.itrip.beans.pojo.Image;

import java.util.List;

public interface ImageService {
    /**
     * 新增
     * */

    boolean addImgs(List<Image> images)throws Exception;


    /**
     * 根据targetId 和type 获取图片列表
     * */

    List<Image> getImgListByConditions(Long targetId, String type)throws Exception;

}
