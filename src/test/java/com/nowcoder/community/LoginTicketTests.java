package com.nowcoder.community;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoginTicketTests {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void selectTest() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("inds");
        System.out.println(loginTicket);
    }

    @Test
    public void updateStatusTest() {
        loginTicketMapper.updateStatus("inds", 999);
    }

}
