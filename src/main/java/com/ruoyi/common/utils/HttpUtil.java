package com.ruoyi.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * http 工具类
 */
public class HttpUtil {

    public static String post(String requestUrl, String accessToken, String params) throws Exception {
        String generalUrl = requestUrl + "?access_token=" + accessToken;
        URL url = new URL(generalUrl);
        
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(params);
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.out.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        if (requestUrl.contains("nlp")) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        }
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        return result;
    }
    
    public static String doGet(String url, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
    
    public static String doGet(String url) {
        return doGet(url, null);
    }
    
    public static String doPost(String url, Map<String, String> param) {
    	// 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }


    public static String doPostFile(String url, Map<String, String> param, File file) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(300000).setSocketTimeout(30000000).build();
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            PostMethod postMethod = new PostMethod(url);
            
            // 创建参数列表
            if (param != null) {
                FilePart f = new FilePart("file", file);
                f.setContentType("video/mp4");
                JSONObject paramsObj = JSONObject.parseObject(JSONObject.toJSONString(param));
                Part[] parts = {
                        f,
                        new StringPart("videoType", paramsObj.get("videoType") + ""),
                        new StringPart("linkedPhone", paramsObj.get("linkedPhone") + ""),
                        new StringPart("sourceCode", paramsObj.get("sourceCode") + ""),
                        new StringPart("provCode", paramsObj.get("provCode") + ""),
                        new StringPart("channelId", paramsObj.get("channelId") + ""),
                        new StringPart("transactionId", paramsObj.get("transactionId") + "")
                };
                
                // 模拟表单
                MultipartRequestEntity entity = new MultipartRequestEntity(parts, postMethod.getParams());
                postMethod.setRequestEntity(entity);
            }
            
            // 执行http请求
            org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            
            int resultCount = client.executeMethod(postMethod);
            if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
                resultString = new String(postMethod.getResponseBodyAsString().getBytes("ISO-8859-1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    
    public static String doHbPost(String url, Map<String, String> param) {
    	// 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            //httpPost.addHeader("Cookie","SESSIONTKEN=c520f233058a40269524e11172793257_101202332028082; SSOCookie=null; SmsNoPwdLoginCookie=8A6E03D51D34E2276FA12785E33BE7508931B43E6FB73488B15E1F39C0C1232F; JSESSIONID=E661CB6D41B03E0EC23E0CA5F39C500E; JtTokenLoginCookie=null; APPVERSION=3.0.2; BIGipServerpool_HeShengHuoAPP=!gIRKj7EivssmKdFtKjT8BkgYaAdSArbz1hN68LuZ+eM7U5rB2Rm+2rStLfRvhnW3UEt1W1DnVFp1zA==; SmsNoPwdLoginCookie=8A6E03D51D34E22731BAE69ACD108BFCFD75DA89AD46B9F2F2BD7B2D32D81B2F");
            httpPost.addHeader("Host", "he.10086.cn");
            httpPost.addHeader("Origin", "http://he.10086.cn");
            httpPost.addHeader("lc-act-ifs", "act1001_initPage");
            httpPost.addHeader("lc-act-actcode", "1001");
            httpPost.addHeader("lc-act-finger", "3e7a9432e9877ecc34a4ef225f64e0a0");
            httpPost.addHeader("User-Agent","hbmcc_android Mozilla/5.0 (Linux; Android 5.0.2; M821 Build/LRX22G; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044807 Mobile Safari/537.36");
            httpPost.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Accept-Encoding", "gzip, deflate");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            httpPost.addHeader("connection", "Keep-Alive");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Upgrade-Insecure-Requests", "1");
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }
}
