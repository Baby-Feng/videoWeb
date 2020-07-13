package com.example.hello.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FeedbackMapper {
    //用户反馈结果输入到数据库中

    @Update(value = "update resource set yes = yes+1,look= look+1 WHERE id = #{id}")
    //更新用户反馈满意结果
    void updateYes(@Param("id") int id);
    //更新用户反馈不满意结果
    @Update(value = "update resource set no = no+1,look= look+1 WHERE id = #{id}")
    void updateNo(@Param("id") int id);
}
