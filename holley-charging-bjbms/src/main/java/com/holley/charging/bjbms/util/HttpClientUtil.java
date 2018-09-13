package com.holley.charging.bjbms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class HttpClientUtil {

    private final static Logger logger = Logger.getLogger(HttpClientUtil.class);

    public static String doPost(String url, Map<String, String> params) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // 设置Http Post数据
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        if (params != null) {
            Set<String> keySet = params.keySet();
            NameValuePair[] param = new NameValuePair[keySet.size()];
            int i = 0;
            for (String key : keySet) {
                param[i] = new NameValuePair(key, params.get(key));
                i++;
            }
            method.setRequestBody(param);
        }
        InputStream responseBodyStream = null;
        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                responseBodyStream = method.getResponseBodyAsStream();
                streamReader = new InputStreamReader(responseBodyStream, "utf-8");
                reader = new BufferedReader(streamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！", e);
        } finally {
            try {
                responseBodyStream.close();
                streamReader.close();
                reader.close();
            } catch (IOException e) {
                logger.error("执行HTTP Post请求" + url + "时，发生异常，关闭流异常！", e);
                e.printStackTrace();
            }
            method.releaseConnection();
        }
        return response.toString();
    }

}
