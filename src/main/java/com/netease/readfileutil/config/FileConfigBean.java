package com.netease.readfileutil.config;

import com.netease.readfileutil.commons.ParamConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @date: 2018-07-27
 * @author: liguobin
 * @description:文件读取配置，其他ParamConstants中的参数如果需要自定义配置的在这里添加字段，并在application.properties中添加配置即可
 */
@ConfigurationProperties(prefix = "config")
@Component
public class FileConfigBean {

    private String filePath = ParamConstants.PATH;//文件路径，当前仅当测试时应该使用
    private int transmissionFrequenc = ParamConstants.TRANSMISSION_FREQUENCY;//发送频率默认
    private int bufferSize = ParamConstants.BUFFER_SIZE;//缓冲区大小默认
    private String isString = ParamConstants.IS_STRING;//是否为字符串数据，默认false，使用true,需要增加CoreReadFileUtil的实现类，或者在原有类中增加个if,实现具体读取逻辑，基本一致，只是少个Long到String的类型转换

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
