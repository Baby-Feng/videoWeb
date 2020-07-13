package com.example.hello.service;

import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;

import java.util.List;


public interface SearchService {
    List<Resource> getResourceList(String search);
    List<Resource> getResourceListByAll(List<String> keywordList,int limit);

    List<Resource> getResourceListByKeywordWeightAndKeywordSum(List<Keyword> keywordList,int limit);

    List<Resource> gerResourceByClassName(String className);

    List<Resource> getSatisfiedResourceList();

    String toASCI(String searchStr);
    String back(String searchStr);
}
