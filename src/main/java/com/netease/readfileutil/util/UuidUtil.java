package com.netease.readfileutil.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @date: 2018-07-26
 * @author: liguobin
 * @description: 自动生成1万个uuid到本地，用来测试
 */
public class UuidUtil {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\read-file-util\\data.txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(file);
        String uuid = "";
        for (int i = 0; i < 1000; i++) {
            uuid = System.nanoTime() + "\r\n";
            out.write(uuid.getBytes());
        }
        out.close();
    }


    /**
     * 配置file路径，一条数据有四条日子[读取日志，生成日志，消费日志，获取日志]，通过这个来判断数据即可
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
