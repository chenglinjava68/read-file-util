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
        File file = new File("C:\\Users\\liguobin\\Desktop\\read-file-util\\read-file-util\\data.txt");
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


    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
