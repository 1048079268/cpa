package com.todaysoft.cpa.utils;


import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

/**
 * @desc: 主键生成器
 * @author: 鱼唇的人类
 */
public class PkGenerator {
    /**
     * @desc: 根据实体类名+UUid生成,然后使用md5加密
     * @author: 鱼唇的人类
     * @param clazz
     * @return
     */
    public static String generator(Class<?> clazz) {
        UUID u = UUID.randomUUID();
        Random random = new Random();
        Integer rand = random.nextInt(1000);
        String str =clazz.getSimpleName()+u.toString()+rand.toString()+System.currentTimeMillis();
        String generator = null;
        try {
            generator = DigestUtils.md5DigestAsHex(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return generator;
    }
}
