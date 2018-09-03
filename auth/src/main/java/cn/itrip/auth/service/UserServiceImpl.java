package cn.itrip.auth.service;

import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.common.*;
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
    @Resource
    private RedisAPI redisAPI;

    @Override
    public boolean add(User record)throws Exception {
        if (userMapper.insert(record) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public User findByUserCode(String userCode)throws Exception {
        return userMapper.selectByUserCode(userCode);
    }

    /**
     * 发送验证邮件
     * @param email
     */
    @Override
    public String sendActivationMail(String email)throws Exception {
        String activationCode = MD5.getMd5(new Date().toString(),12);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText("注册邮箱"+email+"激活码"+activationCode);
        mailSender.send(simpleMailMessage);
        return activationCode;
    }


    public void addNewUser(User user,int isEmail) throws Exception {
        user.setUserCode(user.getUserCode());
        user.setUserPassword(MD5.getMd5(user.getUserPassword(),32));
        user.setUserName(user.getUserName());
        user.setUserType(0);
        user.setCreationDate(new Date());
        add(user);
        if (isEmail==0){
            redisAPI.set("activationCode",sendActivationMail(user.getUserCode()));
        }else{
            String activationCode = MD5.getMd5(new Date().toString(),4);
            redisAPI.set("activationCode",120,activationCode);
            SendMessage sendMessage = new SendMessage();
            sendMessage.sendMessage(user.getUserCode(),activationCode);
        }
    }

    @Override
    public Integer updateByPrimaryKey(User user) throws Exception {

        return userMapper.updateByPrimaryKey(user);
    }

    public Dto ckAcCode(String code,
                        String userCode){
        if (code.equals(redisAPI.get("activationCode"))){
            try {
                User user = findByUserCode(userCode);
                user.setFlatID(user.getId());
                user.setActivated(1);
                updateByPrimaryKey(user);
                redisAPI.delete("activationCode");
                return DtoUtil.returnSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("失败",ErrorCode.AUTH_ACTIVATE_FAILED);
            }
        }
        return DtoUtil.returnFail("失败",ErrorCode.AUTH_ACTIVATE_FAILED);
    }
}
