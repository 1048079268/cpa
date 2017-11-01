package com.todaysoft.cpa.utils;

import java.util.Arrays;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/1 9:31
 */
public class WordCountUtil {
    public static long count(String string){
        return Arrays.stream(string.split("[\\s\\d\\p{Punct}]+"))
                .map(word -> word.replaceAll("[^a-zA-Z]", ""))
                .count();
    }
}
