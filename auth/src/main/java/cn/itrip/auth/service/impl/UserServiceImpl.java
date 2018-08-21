package cn.itrip.auth.service.impl;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.User;
import cn.itrip.beans.vo.ItripUserVo;
import cn.itrip.dao.user.UserMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SimpleMailMessage mailMessage;
    @Resource
    private MailSender mailSender;

    @Override
    public User findByUserCode(String userCode) throws Exception {
        if(userCode!=null){

        return userMapper.selectByUserCode(userCode);
        }
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
    /**
     * 注册
     * @param userVo
     * @return
     */
    @Override
    public Dto doRegister(ItripUserVo userVo) throws Exception {
        //1  接收到用户注册信息
        //调用DAO进行持久操作 userMapper.insertUser(user)
        //1 调用 业务层保存  密码加密 md5加密不可逆 撞库 结果不是有效  对象。方法 （参数）
        User user=new User();
        user.setUserCode(userVo.getUserCode());
        Dto dto=new Dto();
        dto.setErrorCode("10089");
        dto.setMsg("添加成功 ");
        //2.根据结果将数据封装在DTO
        dto.setData(user);
        //3.生成验证码并保存到Redis UUID生成对象.方法 （参数） 有效期，
        String code= UUID.randomUUID().toString();
        //redis操作 StringRedisTemplate
        stringRedisTemplate.opsForValue().set("active:"+user.getUserName(),code,60, TimeUnit.SECONDS);
        // 4 发送邮件到用户邮箱
        redisTemplate.opsForHash().put("user",User.class,user);
        mailMessage.setTo(user.getUserCode());
        mailMessage.setText("你的激活码是："+code);
        mailSender.send(mailMessage);
        System.out.println("取出来的用户激活码："+stringRedisTemplate.opsForValue().get("active:"+user.getUserName()));
        User u2= (User) redisTemplate.opsForHash().get("user",User.class);
        System.out.println("取出来的对象的用户名值："+u2.getUserName());

        //3 返回结果
        return dto;
    }
    /**
     *邮箱激活
     * @param userCode 邮箱账户
     * @param activeCode 激活码
     * @return
     */
    @Override
    public Dto active(String userCode, String activeCode) throws Exception {
        return null;
    }
}
