package com.example.hello.dao;

import com.example.hello.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MainMapper {
    //添加历史记录
    @Update("update person set history = #{history} where username = #{username}")
    void addHistory(@Param("history") String history, @Param("username") String username);

    //添加收藏记录
    @Update("update person set collection = #{collection} where username = #{username}")
    void addCollection(@Param("collection") String collection,@Param("username") String username);

    //减少收藏记录
    @Update("update person set collection = #{collection} where username = #{username}")
    void reduceCollection(@Param("collection") String collection,@Param("username") String username);

    @Select("select * from resource where tid = #{tid}  LIMIT 1")
    Resource getResourceByTid(@Param("tid") String tid);

    //资源播放数自增1
    @Update("update resource set play = play + 1 where tid = #{tid}")
    void updateResourcePlay(@Param("tid") String tid);

    //资源收藏数自增1
    @Update("update resource set collection = collection + 1 where tid = #{tid}")
    void updateResourceCollection(@Param("tid") String tid);

    //资源收藏数减少1
    @Update("update resource set collection = collection - 1 where tid = #{tid}")
    void reduceResourceCollection(@Param("tid") String tid);

}
