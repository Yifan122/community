package com.nowcoder.community.dao;

import org.apache.ibatis.annotations.Mapper;
import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // SQL 需要动态条件 （在<if> 中使用），并且方法只有一个参数的时候
    // 一定需要@Param
    int selectDiscussPostRows(@Param("userId") int userId);
//    int selectDiscussPostRows(int userId);
}
