package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("838145518@qq.com", "TEST", "Weelcome");
        System.out.println("Successfully");
    }

    @Test
    public void htmlEmail(){
        Context context = new Context();
        context.setVariable("username", "fandi");

        String content = templateEngine.process("/mail/demo", context);
        mailClient.sendMail("838145518@qq.com", "TEST", content);
        System.out.println(content);
    }


}
