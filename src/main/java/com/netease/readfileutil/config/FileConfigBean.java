package com.netease.readfileutil.config;

import com.netease.readfileutil.commons.ParamConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @date: 2018-07-27
 * @author: liguobin
 * @description:
 */
@ConfigurationProperties(prefix = "config")
@Component
public class FileConfigBean {

    private String filePath;//文件路径
    private int transmissionFrequenc = ParamConstants.TRANSMISSION_FREQUENCY;//发送频率
    private int bufferSize = ParamConstants.BUFFER_SIZE;//缓冲区大小
    private String isString = ParamConstants.IS_STRING;//是否为字符串数据

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getTransmissionFrequenc() {
        return transmissionFrequenc;
    }

    public void setTransmissionFrequenc(int transmissionFrequenc) {
        this.transmissionFrequenc = transmissionFrequenc;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getIsString() {
        return isString;
    }

    public void setIsString(String isString) {
        this.isString = isString;
    }
}
