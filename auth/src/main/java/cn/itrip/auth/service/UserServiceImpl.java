package cn.itrip.auth.service;

import cn.itrip.beans.pojo.User;
import cn.itrip.common.MD5;
import cn.itrip.dao.user.UserMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService  {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MailSender mailSender;
    @Resource
    private SimpleMailMessage simpleMailMessage;
    @Override
    public boolean deleteByPrimaryKey(Long id)throws Exception {
        if (userMapper.deleteByPrimaryKey(id) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean add(User record)throws Exception {
        if (userMapper.insert(record) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addSelective(User record) throws Exception{

        return userMapper.insertSelective(record) > 0;
    }

    @Override
    public User findtByPrimaryKey(Long id) throws Exception {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean modifyByPrimaryKeySelective(User record)throws Exception {

        return userMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public boolean modifyByPrimaryKey(User record)throws Exception {

        return userMapper.updateByPrimaryKeySelective(record) > 0;
    }

    @Override
    public User findByUserCode(String userCode)throws Exception {
        return userMapper.selectByUserCode(userCode);
    }

    /**
     * login
     */
    @Override
    public User login(String userCode, String userPassword) throws Exception{
        User user=userMapper.selectByUserCode(userCode);
        if(null!=null&&user.getUserPassword().equals(userPassword)) {
            if (user.getActivated() != 1)
                throw new Exception("用户未激活");
            return user;
        }
        return null;
    }
    /**
     * 发送验证邮件
     * @param email
     */
    @Override
    public String sendActivationMail(String email){
        String activationCode = MD5.getMd5(new Date().toString(),12);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText("注册邮箱"+email+"激活码"+activationCode);
        mailSender.send(simpleMailMessage);
        return activationCode;
    }


}
