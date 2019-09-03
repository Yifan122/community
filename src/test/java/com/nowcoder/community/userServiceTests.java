package com.nowcoder.community;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class userServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

//    @Test
//    public void userRegisterTest(){
//        User user = new User("haha", "abc123", "838183@qq.com");
//        userService.register(user);
//        userMapper.updateStatus(159, 1);
//    }
}
