package com.example.hello.dao;

import com.example.hello.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchMapper {


    @Select("SELECT * FROM resource where title like #{keyword}")
    List<Resource> getCourseByAll(@Param("keyword") String keyword);

    @Select("SELECT * FROM resource where title like '%开发%'and title like '%黑马程序员%'")
    List<Resource> test();

    @Select("SELECT *,(${keyweightCondition} + (0.6*collection+0.4*play)/(0.4*play)) AS keyweight FROM resource WHERE CONCAT_WS(' ', title,label) REGEXP '${whereCondition}' ORDER BY keyweight DESC LIMIT ${limit}")
    List<Resource>  getResourceListByAll(@Param("keyweightCondition") String keyweightCondition,
                                         @Param("whereCondition") String whereCondition,
                                         @Param("limit") int limit);

    @Select("SELECT * FROM ch_keyword where keyword like '${first}%'")
    List<String> getChKeywordListFromMysql(@Param("first") String first);

    @Select("SELECT * FROM en_keyword where keyword like '${first}%'")
    List<String> getEnKeywordListFromMysql(@Param("first") String first);

    @Select("SELECT * FROM keyword where keyword like '${first}%'")
    List<String> getKeywordListFromMysql(@Param("first") String first);

    @Select("select * from resource where CONCAT(title,label) like ${className} ORDER BY ((collection*0.6)+(play*0.4))/(play*0.4) DESC LIMIT #{limit}")
    List<Resource> gerResourceByClassName(@Param("className") String className,@Param("limit") int limit);

    @Select("select *,((collection*0.6)+(play * 0.4))/(play*0.4) as keyWeight from resource ORDER BY keyWeight DESC LIMIT #{limit}")
    List<Resource> getSatisfiedResourceList(@Param("limit") int limit);

}
