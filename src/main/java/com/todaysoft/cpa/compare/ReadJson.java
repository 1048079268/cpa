package com.todaysoft.cpa.compare;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/10 11:44
 */
public class ReadJson {
    public static JSONObject read(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(buffer.toString());
        return jsonObject;
    }
}
