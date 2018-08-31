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


    @Override
    public boolean addImgs(List<Image> images)throws Exception {
        if(null!=images && images.size()>0){

            for(Image img:images){
                imageMapper.insert(img);
            }
            return true;
        }

        return false;
    }

    @Override
    public List<Image> getImgListByConditions(Long targetId, String type) throws Exception {
        return imageMapper.selectByConditions(type,targetId);
    }
}
