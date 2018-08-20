package cn.itrip.auth.service.impl;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.pojo.User;
import cn.itrip.dao.user.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User findByUserCode(String userCode) throws Exception {
        return null;
    }

    @Override
    public List<User> findAll() throws Exception {
        return null;
    }

    @Override
    public void itriptxCreateUser(User user) throws Exception {

    }

    @Override
    public void deleteUser(Long userId) throws Exception {

    }

    @Override
    public User login(String userCode, String password) throws Exception {
        return null;
    }
}
