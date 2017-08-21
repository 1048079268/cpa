package com.todaysoft.cpa.utils;


import org.springframework.util.DigestUtils;

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
        String str =clazz.getSimpleName()+u.toString();
        String generator = DigestUtils.md5DigestAsHex(str.getBytes());
        return generator;
    }
}
