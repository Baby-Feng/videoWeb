package com.example.hello.serviceImpl;

import com.example.hello.dao.MainMapper;
import com.example.hello.dao.PersonMapper;
import com.example.hello.dao.SearchMapper;
import com.example.hello.entity.Resource;
import com.example.hello.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MainServiceImpl implements MainService {
    //静态常量区
    private static final int historySum = 20;//最多保存20条历史记录
    private static final int collectionSum = 20;//最多收藏20个视频

    @Autowired
    private MainMapper mainMapper;

    @Autowired
    private PersonMapper personMapper;

    @Override
    public void addHistory(String tid,String username){
        String historyStr = this.personMapper.getHistoryStr(username);
        String nowTime = new SimpleDateFormat("yyyy:MM:dd").format(new Date());
        System.out.println("当前时间 ： "+nowTime);
        System.out.println("history = "+historyStr);
        if(historyStr == null || historyStr.length()==0) this.mainMapper.addHistory(tid+","+nowTime,username);
        else{
            String[] history = historyStr.split("-");
            if(history.length < historySum) this.mainMapper.addHistory(tid+","+nowTime+"-"+historyStr,username);
            else{
                //删除最早的记录
                String historyTempStr = "";
                for(int i = 0;i<historySum-1;++i){
                    if(i > 0) historyTempStr +="-";
                    historyTempStr += history[i];
                }
                this.mainMapper.addHistory(tid+","+nowTime+"-"+historyTempStr,username);
            }
        }
    }
    @Override
    public boolean addCollection(String tid,String username){
        String collectionStr = this.personMapper.getCollectionStr(username);
        String nowTime = new SimpleDateFormat("yyyy:MM:dd").format(new Date());
        System.out.println("当前时间 ： "+nowTime);
        if(collectionStr == null || collectionStr.length()==0) this.mainMapper.addCollection(tid+","+nowTime,username);
        else{
            String[] collection = collectionStr.split("-");
            if(collection.length < collectionSum) this.mainMapper.addCollection(tid+","+nowTime+"-"+collectionStr,username);
            else{
                //删除最后一个
                String collectionTempStr = "";
                //判断是否已有该收藏
                for(int i = 0;i<collectionSum-1;++i){
                    if(collection[i].split(",")[0].equals(tid)) return false;
                    //说明该资源已经收藏，添加失败
                    if(i > 0) collectionTempStr +="-";
                    collectionTempStr += collection[i];
                }
                this.mainMapper.addCollection(tid+","+nowTime+"-"+collectionTempStr,username);
            }
        }
        return true; //添加成功

    }

    @Override
    public void reduceCollection(String tid,String username){
        String collectionStr = this.personMapper.getCollectionStr(username);
        String collectionTempStr = "";
        if(collectionStr.length()!=0){
            //有收藏记录
            String[] collection = collectionStr.split("-");
            for(int i=0;i<collection.length;++i){
                System.out.println("第"+(i+1)+"条收藏记录为:"+collection[i]);
                String cTid = collection[i].split(",")[0];
                if (!tid.equals(cTid)){
                    //匹配不成功，则相加
                    collectionTempStr += collection[i];
                }else continue;
                //匹配成功则不加
            }
        }else{
            //用户没有收藏记录
            System.out.println("用户没有收藏记录");
        }
        //更新用户收藏记录
        this.mainMapper.reduceCollection(collectionTempStr,username);
        //该资源收藏数减少1
        this.mainMapper.reduceResourceCollection(tid);

    }

    @Override
    public void updateResourcePlay(String tid) {
        this.mainMapper.updateResourcePlay(tid);
    }

    //资源收藏数自增
    @Override
    public void updateResourceCollection(String tid){
        this.mainMapper.updateResourceCollection(tid);
    }

    //资源收藏数自减
    @Override
    public void reduceResourceCollection(String tid){
        this.mainMapper.reduceResourceCollection(tid);
    }

    @Override
    public Resource getResourceByTid(String tid) {
        return this.mainMapper.getResourceByTid(tid);
    }

    @Override
    public int isResourceInCollection(String tid,String username){
        String collectionStr = this.personMapper.getCollectionStr(username);
        if(collectionStr == null || collectionStr.length() == 0) return 0;
        //收藏记录为空
        else{
            //收藏记录不为空
            String[] collection = collectionStr.split("-");
            int collectionSum = collection.length;
            for(int i=0;i<collectionSum;++i){
                String cTid = collection[i].split(",")[0];
                if(cTid.equals(tid)) return 1;
            }
            return 0;
        }

    }
}
