package cn.itrip.service.image;

import cn.itrip.beans.pojo.Image;
import cn.itrip.dao.image.ImageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageMapper imageMapper;


    //根据targetId查询酒店房型图片(type=1)
    @Override
    public List<Image> getImageList(Long targetId) throws Exception {
        return imageMapper.selectImageList(targetId);
    }
}
