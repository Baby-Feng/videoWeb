package com.example.hello.serviceImpl;

import com.example.hello.dao.SearchMapper;
import com.example.hello.entity.Keyword;
import com.example.hello.entity.Resource;
import com.example.hello.service.SearchService;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SearchServiceImpl implements SearchService {
    private static final int maxPage = 10;//允许显示的最大页数
    private static final int resourceSumInPage = 20;//每页显示的记录数

    private static final int maxSatisfiedResource = 10;//优质推荐的资源数

    @Autowired
    private SearchMapper searchMapper;

    @Override
    public List<Resource> gerResourceByClassName(String className){
        int limit = maxPage * resourceSumInPage;//一次获取的最大记录数
        return this.searchMapper.gerResourceByClassName("'%"+className+"%'",limit);
    }

    @Override
    public List<Resource> getResourceList(String search){
        //先直接搜索
        System.out.println("search :"+search);
        int limit = maxPage * resourceSumInPage;//一次获取的最大记录数

        List<Resource> tempList = this.searchMapper.gerResourceByClassName("'%"+search+"%'",limit);
        if(tempList!=null && tempList.size()!=0){
            return tempList;
        }else{
            System.out.println("对搜索关键词进行分词");
            List<String> keywordList = getKeywordListFromSearchStr(search);
            //获得搜索关键词
            System.out.println("获取关键词个数为："+keywordList.size());
            for(int i = 0;i<keywordList.size();++i){
                System.out.println(keywordList.get(i));
            }
            try {
                List<Resource> resourceList = getResourceListByAll(keywordList,limit);
                return resourceList;
            }catch (Exception e){
                System.out.println("分词查询异常！");
                return null;
            }
            //获取资源
        }

    }

    //根据关键字综合查询
    @Override
    public List<Resource> getResourceListByAll(List<String> keywordList,int limit){
        String keyweightCondition = "";
        String whereCondition = "";
        for(int i=0;i<keywordList.size();++i){
            if(i > 0){
                keyweightCondition +="+";
                //添加语句连接符号
                whereCondition += "|";
            }
            String keyword = keywordList.get(i);
            keyweightCondition += "(IF( CONCAT_WS(' ', title,label) LIKE '%"+keyword+"%', 1, 0))";
            whereCondition += keyword;
        }
        System.out.println("关键词条件"+keyweightCondition);
        return this.searchMapper.getResourceListByAll(keyweightCondition,whereCondition,limit);
    }

    //根据评分（收藏数、播放数）全局查询
    //优质推荐
    @Override
    public List<Resource> getSatisfiedResourceList(){
        return this.searchMapper.getSatisfiedResourceList(maxSatisfiedResource);
    }

    //根据分割后关键词集、关键词权重综合查询
    //个性推荐、优质推荐
    @Override
    public List<Resource> getResourceListByKeywordWeightAndKeywordSum(List<Keyword> keywordList,int limit){
        String keyweightCondition = "";
        String whereCondition = "";
        for(int i=0;i<keywordList.size();++i){
            if(i > 0){
                keyweightCondition +="+";
                //添加语句连接符号
                whereCondition += "|";
            }
            String keyword = keywordList.get(i).getLabel();
            int keywordWeight = keywordList.get(i).getSum();//关键词权重
            keyweightCondition += "(IF( CONCAT_WS(' ', title,label) LIKE '%"+keyword+"%', "+keywordWeight+", 0))";
            whereCondition += keyword;
        }
        System.out.println("关键词条件"+keyweightCondition);
        List<Resource> resourceList = this.searchMapper.getResourceListByAll(keyweightCondition,whereCondition,limit);
        return resourceList;
    }

    public List<String> getKeywordListFromSearchStr(String search){
        List<String> keywordList = new ArrayList<>();
        List<String> preDivisionStrList = this.getPreDivisionStrList(search);
        int preDivisionStrListSize = preDivisionStrList.size();
        System.out.println("预分割字符串个数："+preDivisionStrListSize);
        for(int i=0;i<preDivisionStrListSize;++i){
            String preDivisionStr = preDivisionStrList.get(i);
            char first = preDivisionStr.charAt(0);
            System.out.println("预分割字符串为："+preDivisionStr);
            if(isChinese(first) || isEnglish(first)){
                //中英文关键词获取
                List<String> keywordListTemp = getKeywordListByForwardMatching(preDivisionStr);
                if(keywordListTemp.size()!=0){
                    keywordList.addAll(keywordListTemp);
                }
            }
//            else if(isEnglish(first)) {
//                List<String> keywordListTemp = getKeywordListByForwardMatching(preDivisionStr);
//                if(keywordListTemp.size()!=0){
//                    keywordList.addAll(keywordListTemp);
//                }
//            }
            else{
                //遇到数字和特殊字符，什么也不做
                //直接保存到关键词列表中
                keywordList.add(preDivisionStr);
            }
        }
        return keywordList;
    }


    public List<String> getKeywordListByForwardMatching(String preDivisionStr){
        //字符串获取
        List<String> keywordList = new ArrayList<>();
        int preTemp = 0;
        int temp = 0;
        int preDivisionStrSize = preDivisionStr.length();
        List<String> nowKeywordList = new ArrayList<>();
        while (temp < preDivisionStrSize){
            char nowChar = preDivisionStr.charAt(temp);
            System.out.println("当前字符 : "+nowChar);
            if(nowKeywordList.size() == 0){
                //当前候选关键词列表为空
                nowKeywordList = this.searchMapper.getKeywordListFromMysql(String.valueOf(nowChar));
//                此处是指定分割的字符串类型
//                if(strType == 1) nowKeywordList = this.searchMapper.getChKeywordListFromMysql(String.valueOf(nowChar));
//                else if(strType == 2) nowKeywordList = this.searchMapper.getEnKeywordListFromMysql(String.valueOf(nowChar));
                if(nowKeywordList.size() != 0) preTemp = temp;//查询到关键词
                else preTemp = temp+1;//未查询到关键词，说明该字符不能作为关键词的起始字符
                System.out.println("查询到的关键词个数 : "+nowKeywordList.size());
                ++temp;
            }else{
                //说明已保存关键词
                int innerTemp = 0;
                boolean status = false;
                while (innerTemp < nowKeywordList.size()){
                    String keyword = nowKeywordList.get(innerTemp);
                    if (keyword.length()-1 < temp-preTemp || keyword.charAt(temp - preTemp)!= nowChar){
                        nowKeywordList.remove(innerTemp);
                        //删除列表中不满足条件的元素
                    }else {
                        //存在至少一个满足条件的元素
                        status = true;
                        ++innerTemp;
                    }
                }
                if(status){
                    //存在至少一个满足条件的元素
                    ++temp;
                }else {
                    //没有一个满足条件的元素,说明nowChar不能并入前面的关键词
                    //此时nowKeywordList的长度为0
                    System.out.println("匹配失败，当前字符 : "+nowChar);
                    keywordList.add(preDivisionStr.substring(preTemp,temp));
                    //temp不移动
                    System.out.println("收录关键词个数 : "+nowKeywordList.size());
                    preTemp = temp;
                }

            }
        }
        if(preTemp < preDivisionStrSize)
            keywordList.add(preDivisionStr.substring(preTemp,preDivisionStrSize));
        return keywordList;
    }

    public boolean isChinese(char temp){
        if (!Pattern.compile("[\u4e00-\u9fa5]").matcher(
                String.valueOf(temp)).find())
            return false;
        else return true;
    }

    public boolean isEnglish(char temp){
        if((temp >= 'a' && temp <= 'z') || (temp >= 'A' && temp <= 'Z'))
            return true;
        else return false;
    }

    public boolean isNumber(char temp){
        Pattern pattern = Pattern.compile("[0-9]");
        return pattern.matcher(String.valueOf(temp)).find();
    }

    public boolean isSpecialChar(char temp){
        //特殊字符
        Pattern pattern = Pattern.compile("[@#$%&*_+-]");
        return pattern.matcher(String.valueOf(temp)).find();
    }

    @Override
    public String toASCI(String searchStr){
        System.out.println("开始转义:"+searchStr);
        char[] search = searchStr.toCharArray();
        String newSearch = "";
        for(int i =0;i<search.length;++i){
            char nowChar = search[i];
            System.out.println(nowChar);
            if(!isChinese(nowChar) && !isEnglish(nowChar) && !isNumber(nowChar)){
                //在特殊字符前后添加转义字符
                newSearch +="!"+stringToAscii(String.valueOf(nowChar))+"!";
            }else{
                newSearch += search[i];
            }
        }
        System.out.println("转义字符:"+newSearch);
        return newSearch;
    }

    @Override
    public String back(String searchStr){
        char[] search = searchStr.toCharArray();
        String newSearch = "";
        String nowTran = "";
        boolean status = false;
        for(int i=0;i<search.length;++i){
            if(search[i]!='!' && !status){
                System.out.println("添加"+search[i]);
                newSearch += search[i];
            }else if(search[i]!='!' && status){
                //记录当前字符串...
                nowTran += search[i];
            }else if(search[i]=='!' && !status){
                //准备开始记录特殊字符码
                status = true;

            }else if(search[i]=='!' && status){
                //结束特殊字符码
                System.out.println("添加"+nowTran);
                newSearch += asciiToString(nowTran);
                nowTran = "";
//                int as = Integer.valueOf(nowTran);
//                char ch = (char)as;
//                newSearch += String.valueOf(ch);
                status = false;
            }
        }
        System.out.println("解码结果:"+newSearch);
        return newSearch;
    }

    public String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

    public String asciiToString(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }


    public List<String> getPreDivisionStrList(String search){
        //对搜索字符串进行预分割
        //数字和特殊字符单独拿出来
        //英文和汉字可以组合
        List<String> preDivisionStrList = new ArrayList<>();
        int preTemp = 0;
        int preCharType = 0;
        for(int i=0;i<search.length();++i){
            char nowChar = search.charAt(i);
            if(isChinese(nowChar)){
                if(preCharType != 1){
                    //前一个字符不是中文
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 1;

            }else if(isEnglish(nowChar)){
                //是英文
                if(preCharType != 2){
                    //前一个字符不是英文
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 2;
            }else if(isNumber(nowChar)){
                //是数字
                if(preCharType != 3){
                    //前一个字符不是数字
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 3;
            }else if(isSpecialChar(nowChar)){
                //是特殊字符
                if(preCharType != 4){
                    if(preTemp < i) preDivisionStrList.add(search.substring(preTemp, i));
                    preTemp = i;
                }
                preCharType = 4;
            }else {
                //其它字符一律作为分割符号
                if(preTemp < i)
                    //说明已保存至少一个字符
                    preDivisionStrList.add(search.substring(preTemp,i));
                preTemp = i+1;
                //前驱指针指向后一个字符
            }
        }
        if(preTemp < search.length())
            preDivisionStrList.add(search.substring(preTemp));
        return preDivisionStrList;
    }
}
