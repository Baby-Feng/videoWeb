package com.example.hello.service;

import com.example.hello.entity.Resource;

public interface MainService {

    void addHistory(String tid,String username);
    boolean addCollection(String tid,String username);

    Resource getResourceByTid(String tid);

    void updateResourcePlay(String tid);
    void updateResourceCollection(String tid);
    void reduceResourceCollection(String tid);

    int isResourceInCollection(String tid,String username);

    void reduceCollection(String tid,String username);
}
