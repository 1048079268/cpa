package com.todaysoft.cpa.test;

import com.alibaba.fastjson.*;
import com.todaysoft.cpa.compare.AcquireJsonStructure;
import com.todaysoft.cpa.compare.CompareJsonStructure;
import com.todaysoft.cpa.compare.JsonDataType;
import com.todaysoft.cpa.compare.ReadJson;
import com.todaysoft.cpa.utils.ExceptionInfo;
import com.todaysoft.cpa.utils.StructureChangeException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/11 10:54
 */
public class ErrorJsonAnalyze {
    private static String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjc0MSwic2VydmljZUlkIjo0LCJpYXQiOjE0OTMxMzEwNjl9.Q0Y8ty9eZ-s2MvMk4VmQW68ZWoxemFbKCOShxPzGMd4";
    private static String url="https://cpa.myknowtions.com/v2/gene/3582";
    public static void main(String[] args) throws IOException {
//        Connection.Response response=Jsoup.connect(url)
//                .userAgent("'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6'") // 设置 User-Agent
//                .header("Authorization", token)
//                .header("Accept", "application/test")
//                .ignoreContentType(true)
//                .timeout(120000)
//                .maxBodySize(0)
//                .execute();
        long start = System.currentTimeMillis();
        JSONObject temp= ReadJson.read("jsonTemp/gene.json");
        Map<String, JsonDataType> tempKeyMap = AcquireJsonStructure.getJsonKeyMap(null, temp);
        tempKeyMap.entrySet().stream().forEach(stringDataTypeEntry -> {
            System.out.println(stringDataTypeEntry.getKey()+":"+stringDataTypeEntry.getValue());
        });
        System.out.println("----------------");
        JSONObject jsonObject=ReadJson.read("jsonTemp/geneTest.json");
        Map<String, JsonDataType> jsonKeyMap = AcquireJsonStructure.getJsonKeyMap(null, jsonObject);
        jsonKeyMap.entrySet().stream().forEach(stringDataTypeEntry -> {
            System.out.println(stringDataTypeEntry.getKey()+":"+stringDataTypeEntry.getValue());
        });
        System.out.println("--------------------");
        try {
            CompareJsonStructure.compare(tempKeyMap,jsonKeyMap);
        } catch (StructureChangeException e) {
            System.out.println(e.getMessage());
            System.out.println( "【content】JSON结构变化"+ ExceptionInfo.getErrorInfo(e));;
        }
        System.out.println(System.currentTimeMillis()-start);
    }
}
