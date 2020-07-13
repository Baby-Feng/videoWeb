package com.example.hello.serviceImpl;

import com.example.hello.dao.PersonMapper;
import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;
import com.example.hello.entity.User;
import com.example.hello.service.PersonService;
import jnr.ffi.annotations.In;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private static final int userLabelSum = 10;//用户最多保存10个个性标签
    private static final int historyWeight = 1;//历史记录所占权重
    private static final int collectionWeight = 10;//收藏记录所占权重

    private static final int userDefinedLabelWeight = 20;//用户标签权重
    @Autowired
    private PersonMapper personMapper;

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        return this.personMapper.getUserByUsernameAndPassword(username, password);
    }

    @Override
    public boolean register(String username, String password, String email, String phone,String interest) {
        System.out.println("准备注册");
        User user = this.personMapper.personInfo(username);
        if(user !=null) {
            System.out.println("该账号已被使用！");
            return false;

        }
        else{
            System.out.println("该账号没有被使用！");
            System.out.println(username+","+password+","+phone+","+email+","+interest);
            this.personMapper.register(username, password, phone,email,interest);
            System.out.println("注册成功！");
            user = this.personMapper.personInfo(username);
            if(user!=null) return true;
            else return false;
        }

    }
    //用户管理

    //修改用户信息，手机、昵称、邮箱
    @Override
    public boolean updatePersonNamePhoneAndEmail(String name,String phone,String email,String username){
        try {
            this.personMapper.updatePersonNamePhoneAndEmail(name, phone, email, username);
        }catch (Exception e){
            System.out.println("修改昵称、手机、邮箱出错");
            return false;
        }
        return true;
    }
    @Override
    public boolean updatePassword(String password,String username){
        try {
            this.personMapper.updatePassword(password, username);
        }catch (Exception e){
            System.out.println("修改密码出错");
            return false;
        }
        return true;
    }


    @Override
    public User personInfo(String username) {
        return this.personMapper.personInfo(username);
    }

    @Override
    public List<Resource> getHistory(String username) {
        String historyStr = this.personMapper.getHistoryStr(username);
        System.out.println("历史:"+historyStr);
        if(historyStr == null || historyStr.length()==0) return null;
        else return this.getResource(historyStr.split("-"));
    }

    @Override
    public List<Resource> getCollection(String username) {
        String collectionStr = this.personMapper.getCollectionStr(username);
        System.out.println("收藏:"+collectionStr);
        if(collectionStr == null || collectionStr.length()==0) return null;
        else return this.getResource(collectionStr.split("-"));

    }

    @Override
    public List<Resource> getResource(String[] info){
        if(info.length == 0) return null;
        else{
            List<Resource> resourceList = new ArrayList<>();
            for(int i=0;i<info.length;++i){
                String tid = info[i].split(",")[0];
                System.out.println("tid : "+tid);
                resourceList.add(this.personMapper.getOneResource(tid));
            }
            return resourceList;
        }
    }


    public List<Keyword> getUserLabelListByHistoryAndCollection(String username) {
        System.out.println("username : "+username);
        List<Keyword> userLabelList = new ArrayList<>();
        //用户偏好标签
        User user = this.personMapper.personInfo(username);
        //获取用户的详细信息
        String historyRecord = user.getHistory();
        boolean haveHistory = false;
        if(historyRecord != null && historyRecord.length() != 0){
            //有历史记录
            haveHistory = true;
            String[] history = historyRecord.split("-");
            int historySum = history.length;
            String historyStr = "";
            for (int i = 0; i < historySum; ++i) {
                System.out.println(history[i]);
                String tid = history[i].split(",")[0];
                System.out.println("历史视频id : " + tid);
                if (i > 0) historyStr += ",";
                historyStr += "'" + tid + "'";
            }
            List<String> HistoryLabelList = this.personMapper.getVideoLabel(historyStr);
            for (int i = 0; i < HistoryLabelList.size(); ++i) {
                //统计历史记录中的关键词
                updateVideoListToUserLabel(userLabelList,HistoryLabelList.get(i).split("-"),historyWeight);
            }
        }
        String collectionRecorder = user.getCollection();
        boolean haveCollection = false;
        if(collectionRecorder !=null && collectionRecorder.length() != 0){
            //有收藏记录
            haveCollection = true;
            String[] collection = collectionRecorder.split("-");
            int collectionSum = collection.length;
            String collectionTid = "";
            for (int i = 0; i < collectionSum; ++i) {
                String tid = collection[i].split(",")[0];
                System.out.println("收藏视频id : " + tid);
                if (i > 0) collectionTid += ",";
                collectionTid += "'" + tid + "'";
            }
            List<String> collectionLabelList = this.personMapper.getVideoLabel(collectionTid);
            for(int i=0;i<collectionLabelList.size();++i){
                //统计收藏记录中的关键词
                updateVideoListToUserLabel(userLabelList,collectionLabelList.get(i).split("-"),collectionWeight);
            }
        }
        //合并用户标签集

        List<Keyword> userKeyword = getUserLabelList(user.getLabel());
        boolean haveUserLabel =false;
        if(userKeyword!=null) {
            //有用户自定义的标签
            haveUserLabel = true;
            userLabelList.addAll(userKeyword);
        }
        //若用户没有任何使用信息，则无法获取推荐内容
        if(!haveHistory && !haveCollection && !haveUserLabel) return null;

        //合并重复标签的权重,并计算
        margeSameUserLabel(userLabelList);
        //保留有限个用户偏好标签
        sortUserLabelByWeight(userLabelList);
        //将用户标签添加到个人信息中
        //updateUserLabel(userLabelList,username);
        return userLabelList;
    }


    public void updateVideoListToUserLabel(List<Keyword> labelList, String[] video, int weight) {
        int videoSize = video.length;
        for (int i = 0; i < videoSize; ++i) {
            String keyword = video[i];
            if (labelList.size() == 0) {
                labelList.add(new Keyword(keyword, weight));
            } else {
                //有数据
                boolean status = false;
                for (int j = 0; i < labelList.size(); ++i) {
                    int sum = labelList.get(j).getSum();
                    if (labelList.get(j).getLabel().equals(keyword)) {
                        labelList.get(j).setSum(sum + weight);
                        status = true;
                        break;
                    }
                }
                if (!status) labelList.add(new Keyword(keyword, weight));
            }
        }

    }

    public void margeSameUserLabel(List<Keyword> labelList){
        //合并相同的关键词
        int temp = 0;
        while (temp < labelList.size()){
            String nowLabel = labelList.get(temp).getLabel();
            int nowSum = labelList.get(temp).getSum();
            int inner = temp+1;
            while (inner < labelList.size()){
                String innerLabel = labelList.get(inner).getLabel();
                int innerSum = labelList.get(inner).getSum();
                if(nowLabel.equals(innerLabel)){
                    //字符串匹配，则删除内层
                    labelList.get(temp).setSum(nowSum+innerSum);
                    labelList.remove(inner);
                }else inner += 1;
            }
            temp += 1;
        }
    }

    public void sortUserLabelByWeight(List<Keyword> labelList){
        int labelListSize =  labelList.size();
        if(labelListSize > userLabelSum){
            //数量超出限制，则根据权重进行筛选
            for (int i = 0;i<userLabelSum;++i){
                //筛选10次
                int nowMaxWeight = labelList.get(i).getSum();//本次循环的初值
                int nowMaxIndex = i;
                for(int j = i+1;j<labelListSize;++j){
                    int nowWeight = labelList.get(j).getSum();
                    if(nowWeight > nowMaxWeight) {
                        nowMaxIndex = j;//更新索引
                        nowMaxWeight = nowWeight;
                    }
                    //交换位置
                }
                Collections.swap(labelList,i,nowMaxIndex);
            }
            int temp = userLabelSum;
            while (temp < labelList.size()){
                labelList.remove(temp);
            }
        }

    }

    @Override
    public void updateUserLabel(String labelStr,String username){
        System.out.println(labelStr+","+username);
        this.personMapper.updateUserLabel(labelStr,username);
    }

    @Override
    public List<Keyword> getUserLabel(String username){
        return getUserLabelList(this.personMapper.getUserLabel(username));
    }

    public List<Keyword> getUserLabelList(String userLabelStr){
        if(userLabelStr !=null && userLabelStr.length() != 0){
            String[] userLabelList = userLabelStr.split("-");
            int userLabelSum = userLabelList.length;
            List<Keyword> userLabel = new ArrayList<>();
            for (int i = 0; i < userLabelSum; ++i) {
                String label = userLabelList[i];
                int sum = userDefinedLabelWeight;//默认用户自定义标签权重
                userLabel.add(new Keyword(label,sum));
            }
            return userLabel;
        }
        else{
            System.out.println("无用户定义标签！");
            return null;
        }
    }

}
