package cn.itrip.dao.productstore;

import cn.itrip.beans.pojo.ProductStore;

public interface ProductStoreMapper {





    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(ProductStore record);

    int insertSelective(ProductStore record);

    ProductStore selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductStore record);

    int updateByPrimaryKey(ProductStore record);
}