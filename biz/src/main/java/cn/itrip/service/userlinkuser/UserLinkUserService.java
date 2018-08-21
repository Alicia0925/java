package cn.itrip.service.userlinkuser;

import cn.itrip.beans.pojo.UserLinkUser;

import java.util.List;
import java.util.Map;

public interface UserLinkUserService {

    /**
     * 获取常用旅客list by用户ID
     * 模糊搜索by旅客姓名
     **/

    List<UserLinkUser> getUserLinkUser(Long userId,String linkUserName)throws Exception;

    /**
     * 总记录
     * */
    Integer countUserLinkUser(Long userId,String linkUserName)throws Exception;

    /**分页
     * */
    List<UserLinkUser> getUserLinkUserPage(Map<String,Object> param,Integer currentPage,Integer pageSize)throws Exception;

    /**
     * OID
     * */
    UserLinkUser getUserLinkUserById(Long id)throws Exception;

    /**
     * 添加
     *
     **/
    boolean addUserLinkUser(UserLinkUser userLinkUser)throws Exception;

    /**修改*/

    boolean modifyUserLinkUserById(UserLinkUser userLinkUser)throws Exception;

    /**删*/


    boolean deleteUserLinkUserById(UserLinkUser userLinkUser)throws Exception;
}
