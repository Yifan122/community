package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int userId) {
        return userMapper.selectById(userId);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        if(user == null) {
            logger.error(logger.getName() + " User is null.");
            throw new IllegalArgumentException("参数不能为空");
        }

        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if(userMapper.selectByName(user.getUsername()) != null){
            map.put("usernameMsg", "账号已被占用");
            return map;
        }

        if(userMapper.selectByEmail(user.getEmail()) != null){
            map.put("emailMsg", "邮箱已被占用");
            return map;
        }

        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        Context context = new Context();
        context.setVariable("email", user.getEmail());

        System.out.println(user.getEmail());

        // set activation url http://localhost:8081/community/activation/101/activationCode
        String activationUrl = domain+contextPath+"/activation/" + user.getId() + "/" +user.getActivationCode();
        context.setVariable("url", activationUrl);
        String content = templateEngine.process("/mail/activation", context);

        // TO DO: send Mail
        mailClient.sendMail("838145518@qq.com", "Activation Email", content);
        System.out.println(content);
        System.out.println(activationUrl);
        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);

        if(user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 验证是否为空
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "Username cannot be empty");
            return map;
        }

        User user = userMapper.selectByName(username);

        // 验证username是否存在
        if (user == null) {
            map.put("usernameMsg", "Username doesn't exist");
            return map;
        }

        // 验证是否激活
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "Please activate your account");
            return map;
        }

        // 验证密码是否为空
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "password cannot be empty");
            return map;
        }

        password = CommunityUtil.md5(password + user.getSalt());
        if (!password.equals(user.getPassword())) {
            map.put("passwordMsg", "password is not right");
            return map;
        }

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * expiredSeconds));

        loginTicketMapper.insertLoinTicket(loginTicket);

        // return ticket
        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }
}
