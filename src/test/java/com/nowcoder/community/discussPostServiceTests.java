package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class discussPostServiceTests {

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void insertDiscussService() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle("<script>alert('xss')</scirpt>");
        discussPost.setContent("我要赌博，我要开票，哈哈哈哈");
        discussPost.setCommentCount(2);
        discussPost.setScore(0);
        discussPost.setUserId(12);
        discussPost.setType(1);

        discussPostService.addDiscussPost(discussPost);
    }

    @Test
    public void selectDiscussPostTest() {
        System.out.println(discussPostService.findDiscussPostById(238));
    }

}
