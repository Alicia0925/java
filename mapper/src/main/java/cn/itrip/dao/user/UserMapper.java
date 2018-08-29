package cn.itrip.dao.user;

import cn.itrip.beans.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id)throws Exception ;

    int insert(User record)throws Exception ;

    int insertSelective(User record)throws Exception ;

    User selectByPrimaryKey(Long id)throws Exception ;

    int updateByPrimaryKeySelective(User record)throws Exception ;

    int updateByPrimaryKey(User record)throws Exception ;
    /**
     * 查byUserCode
     * */
    User selectByUserCode(@Param("userCode") String userCode)throws Exception ;
}