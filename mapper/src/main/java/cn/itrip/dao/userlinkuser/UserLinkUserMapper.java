package cn.itrip.dao.userlinkuser;

import cn.itrip.beans.pojo.UserLinkUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserLinkUserMapper {

    //以下是自动生成CURD
    int deleteByPrimaryKey(Long id);

    int insert(UserLinkUser record);
    /**新增常用旅客*/
    int insertSelective(UserLinkUser record);

    UserLinkUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLinkUser record);

    int updateByPrimaryKey(UserLinkUser record);


    /**
     *根据登录用户ID查所有常用旅客
     * 旅客姓名模糊查询
     * */

    List<UserLinkUser> selectByUserId(@Param("userId") Long userId,
                                      @Param("linkUserName")String linkUserName);
    /**总记录数*/
    Integer countByUserId(@Param("userId") Long userId,
                          @Param("linkUserName")String linkUserName);

    /**分页*/
    List<UserLinkUser> selectByUserIdPage(Map<String,Object> param,
                                          @Param("offset") Integer offset,
                                          @Param("pageSize") Integer pageSize);
}