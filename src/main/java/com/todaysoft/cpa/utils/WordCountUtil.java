package com.todaysoft.cpa.utils;

import java.util.Arrays;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/1 9:31
 */
public class WordCountUtil {
    public static long count(String string){
        if (string==null||string.length()==0){
            return 0L;
        }
        // [\s\d\p{Punct}]+
        return Arrays.stream(string.split("[\\s\\d.,?!|;]+"))
                .map(word -> word.replaceAll("[^a-zA-Z]", ""))
                .count();
    }
}
