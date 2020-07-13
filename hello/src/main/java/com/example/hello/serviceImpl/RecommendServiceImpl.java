package com.example.hello.serviceImpl;

import com.example.hello.dao.RecommendMapper;
import com.example.hello.dao.SearchMapper;
import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;
import com.example.hello.service.PersonService;
import com.example.hello.service.RecommendService;
import com.example.hello.service.SearchService;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    public static final int recommendationNum = 10;//一次性最多显示推荐内容的个数
    public static final String historyJsonFile ="C://project/fengyao/json/history.txt";
    //假设系统定义标签分两类
    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private SearchService searchService;
    @Autowired
    private PersonService personService;

    public List<Resource> getResourceByUserLabel(String username){
        List<Keyword> keywordList = this.personService.getUserLabelListByHistoryAndCollection(username);
        List<Resource> resourceList = this.searchService.getResourceListByKeywordWeightAndKeywordSum(keywordList,recommendationNum);
        return resourceList;
    }

    @Override
    public List<Resource> getSatisfiedResourceList(){
        return this.searchService.getSatisfiedResourceList();
    }


    @Override
    public List<Resource> getRecommendation(String tidList,int username) {
        //Input:
        System.out.println("2");
        String [] history = this.getHistoryLabelDataFromMySql(username);
        int historyNum = history.length;
        System.out.println("历史记录总数"+historyNum);
        //Output:
        List<Resource> resourceList = new ArrayList<>();

        //则随机产生10个随机数
        double averageProperty = 1.0/historyNum;//小区间的长度
        System.out.println("区间长度:"+averageProperty);
        List<String> tidSet = new ArrayList<>();
        if(tidList != null){
            System.out.println("ok"+tidList);
            String[] tidTemp = tidList.split("-");
            for(int i = 0;i<tidTemp.length;++i){
                tidSet.add(tidTemp[i]);
            }
        }
        System.out.println(tidSet.size());
        int temp = 0;
        while(temp < recommendationNum){
            int indexTemp = (int)Math.floor(Math.random()/averageProperty);//向下取整,并保存索引号
            //查询
            String [] labels = history[indexTemp].split("-");
            if (labels.length >= 2){
                //标签个数大于两个,说明有用户自定义标签，可以进行推荐任务
                String label = "'%"+labels[1]+"%'";
                //第一个用户自定义标签
                Resource resource = this.recommendMapper.getRecommendation(label,getCondition(tidSet));
                String tId = resource.getTid();
                if(!this.in_TidList(tidSet,tId)) {
                    //没有重复记录，添加
                    System.out.println("添加记录"+tId);
                    resourceList.add(resource);
                    tidSet.add(tId);
                    ++temp;
                }

            }
            //只有一级标签，不能进行推荐

        }
        return resourceList;
    }
    public String getCondition(List<String> tIdSet){
        if(tIdSet.size() == 0)
            return "";
        String condition = " and tid not in (";
        for(int i = 0;i<tIdSet.size();++i){
            if(i != 0)
                condition +=",";//分隔符
            condition += "'"+tIdSet.get(i)+"'";
        }
        condition += ")";
        System.out.println(condition);
        return condition;
    }

    public boolean in_TidList(List<String> tidSet,String tId){
        System.out.println("查找串"+tId);
        for(int i = 0;i<tidSet.size();++i){
            System.out.println("匹配"+tidSet.get(i));
            if (tidSet.get(i).equals(tId)){
                System.out.println("找到");
                return true;
            }
        }
        return false;
    }

    public List<String> getHistoryData() throws Exception{
        FileInputStream in = new FileInputStream(historyJsonFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));// 读取文件
        String tempString=null;
        List<String> historyLabelList = new ArrayList<>();
        while((tempString=reader.readLine())!=null){
            System.out.println(tempString);
            historyLabelList.add(tempString);
        }
        reader.close();
        return historyLabelList;
    }

    public String [] getHistoryLabelDataFromMySql(int username){
        String [] videoList = this.recommendMapper.getHistoryByUsername(username).split("-");
        String videoId = "";
        for(int i=0;i<videoList.length;++i){
            String tid = videoList[i];
            if(i == 0){
                videoId = "'"+tid+"'";
            }else{
                videoId += ",'"+tid+"'";
            }
        }
        String [] labelList = this.recommendMapper.getLabelByVideoId(videoId);
        return labelList;
    }


}
