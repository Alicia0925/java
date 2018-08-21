package cn.itrip.auth.service.impl;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.pojo.User;
import cn.itrip.common.MD5;
import cn.itrip.dao.user.UserMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MailSender mailSender;
    @Resource
    private SimpleMailMessage simpleMailMessage;
//    private ActivationMailMessage activationMailMessage;
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

    /**
     * 发送验证邮件
     * @param email
     */
    @Override
    public String sendActivationMail(String email) {
        String activationCode = MD5.getMd5(new Date().toString(),12);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText("注册邮箱"+email+"激活码"+activationCode);
        return activationCode;
    }
}
