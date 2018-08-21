package cn.itrip.auth.service.impl;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.pojo.User;
import cn.itrip.dao.user.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        if (userMapper.deleteByPrimaryKey(id)>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(User record) {
        if (userMapper.insert(record)>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean insertSelective(User record) {
        if (userMapper.insertSelective(record)>0){
            return true;
        }
        return false;
    }

    @Override
    public User selectByPrimaryKey(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(User record) {
        if (userMapper.updateByPrimaryKeySelective(record)>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(User record) {
        if (userMapper.updateByPrimaryKeySelective(record)>0){
            return true;
        }
        return false;
    }

    @Override
    public User selectByUserCode(String userCode) {
        return userMapper.selectByUserCode(userCode);
    }
}
