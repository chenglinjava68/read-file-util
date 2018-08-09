package com.netease.readfileutil.core.impl;

import com.netease.readfileutil.core.CoreHttpAPIService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Order//高优先级，否则可能出现问题
public class CoreHttpAPIServiceImpl implements CoreHttpAPIService {

    private CloseableHttpClient httpClient;

    private RequestConfig config;

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setConfig(RequestConfig config) {
        this.config = config;
    }

    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     *
     * @param url
     * @return
     * @throws Exception
     */
    @Override
    public String doGet(String url, String charset) {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 装载配置信息
        httpGet.setConfig(config);
        // 发起请求
        CloseableHttpResponse response = null;
        try {
            response = this.httpClient.execute(httpGet);
            // 判断状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 返回响应体的内容
                return EntityUtils.toString(response.getEntity(), charset);//这里返回的是
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}