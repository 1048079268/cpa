package com.todaysoft.cpa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/15 14:59
 */
public class FileUtil {
    public static void copyFile(File fromFile, File toFile) {
        FileInputStream ins = null;
        FileOutputStream out=null;
        try {
            ins = new FileInputStream(fromFile);
            out= new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int n=0;
            while((n=ins.read(b))!=-1){
                out.write(b, 0, n);
            }
            ins.close();
            out.close();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
