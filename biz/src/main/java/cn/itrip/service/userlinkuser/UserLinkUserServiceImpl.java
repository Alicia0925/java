
package cn.itrip.service.userlinkuser;

import cn.itrip.beans.pojo.UserLinkUser;
import cn.itrip.dao.userlinkuser.UserLinkUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserLinkUserServiceImpl implements UserLinkUserService {
    @Resource
    private UserLinkUserMapper userLinkUserMapper;

    @Override
    public List<UserLinkUser> getUserLinkUser(Long userId, String linkUserName) throws Exception {

        return userLinkUserMapper.selectByUserId(userId, linkUserName);
    }

    @Override
    public Integer countUserLinkUser(Long userId, String linkUserName) throws Exception {
        return userLinkUserMapper.countByUserId(userId, linkUserName);
    }

    @Override
    public List<UserLinkUser> getUserLinkUserPage(Map<String, Object> param, Integer currentPage, Integer pageSize) throws Exception {
        return userLinkUserMapper.selectByUserIdPage(param, (currentPage - 1) * pageSize, pageSize);
    }

    @Override
    public UserLinkUser getUserLinkUserById(Long id) throws Exception {
        return userLinkUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean addUserLinkUser(UserLinkUser userLinkUser) throws Exception {
        return userLinkUserMapper.insertSelective(userLinkUser) > 0;
    }

    @Override
    public boolean modifyUserLinkUserById(UserLinkUser userLinkUser) throws Exception {
        return userLinkUserMapper.updateByPrimaryKeySelective(userLinkUser) > 0;
    }

    @Override
    public boolean deleteUserLinkUserById(UserLinkUser userLinkUser) throws Exception {
        return userLinkUserMapper.deleteByPrimaryKey(userLinkUser.getId()) > 0;
    }

    /**
     * 批量删除
     */
    @Override
    public boolean deleteUserLinkUserByIds(List<Long> ids) throws Exception {
        return userLinkUserMapper.deleteByIds(ids) > 0;
    }
}
