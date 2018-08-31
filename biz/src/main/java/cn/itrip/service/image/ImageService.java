package cn.itrip.service.image;

import cn.itrip.beans.pojo.Image;

import java.util.List;

public interface ImageService {

    //根据targetId查询酒店房型图片(type=1)
    List<Image> getImageList(Long targetId)throws Exception;

}
