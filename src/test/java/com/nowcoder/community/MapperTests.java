package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(146);
        System.out.println(user);

        user = userMapper.selectByName("lihonghe");
        System.out.println(user);

        user = userMapper.selectByEmail("yifan@gmail.com");
        System.out.println(user);
    }

    @Test
    public void testUpdateUser(){
        userMapper.updateStatus(150, 789);
        userMapper.updateHeader(150, "http://new.hearderurl.com");
        userMapper.updatePassword(150, "newpassword");

        User user;
        user = userMapper.selectByEmail("yifan@gmail.com");
        System.out.println(user);
    }

    @Test
    public void testSelectPost(){
        List<DiscussPost> list = new ArrayList<>();
        list = discussPostMapper.selectDiscussPosts(132, 0, 20);
        for (DiscussPost discussPost: list){
            System.out.println(discussPost);
        }
    }

    @Test
    public void testSelectDiscussPostRows(){
        System.out.println(discussPostMapper.selectDiscussPostRows(132));
    }

    @Test
    public void testSelectMessageTest() {


        System.out.println(messageMapper.selectLetterUnreadCount(112, "111_112"));


    }
}
