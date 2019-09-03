package com.nowcoder.community;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommentTests {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void selectTest() {
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 228, 0, 20);
        for (Comment comment : comments) {
            System.out.println(comment);
        }

        System.out.println("size");
        System.out.println(comments.size());

        System.out.println(commentMapper.selectCountByEntity(1, 228));
    }
}
