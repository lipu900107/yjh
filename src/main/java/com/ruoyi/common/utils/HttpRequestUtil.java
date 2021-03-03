package com.ruoyi.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtil {
    /** 
     *  
     * @param requestUrl  请求地址 
     * @param requestMethod   请求方式 
     * @param str 提交的数据 
     * @return 
     */  
    public static JSONObject httpRequestJSONObject(String requestUrl, String requestMethod, String outputStr){  
        JSONObject jsonObject = null;  
        try {  
            //创建SSLContext对象  
            TrustManager[] tm = {new MyX509TrustManager()};  
            SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            //从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
              
            URL url = new URL(requestUrl);  
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();  
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1 MicroMessenger/4.5.255");
            //这个是在网上看到的，要加上这个，避免其他错误
            conn.setRequestProperty("Referer", "https://mp.weixin.qq.com");
            conn.setSSLSocketFactory(ssf);  
              
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            //设置请求方式  
            conn.setRequestMethod(requestMethod);  
              
           // if (GET_METHOD.equalsIgnoreCase(requestMethod)){  
                 conn.connect();   
           // }  
              
            if(null != outputStr){  
                OutputStream outputStream = conn.getOutputStream();  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
              
            //从输入流读取返回内容  
            InputStream inputStream = conn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
              
            String str = null;  
            StringBuffer buffer = new StringBuffer();  
            while((str = bufferedReader.readLine())!= null){  
                buffer.append(str);  
            }  
              
            //释放资源  
            bufferedReader.close();  
            inputStreamReader.close();  
            inputStream.close();  
            inputStream=null;  
            conn.disconnect();  
            jsonObject = JSONObject.parseObject(buffer.toString());          
        } catch (Exception e) {  
            e.printStackTrace();  
            //Logger.info("请求异常: "+e.getMessage());   
        }          
        return jsonObject;  
    }  
}
