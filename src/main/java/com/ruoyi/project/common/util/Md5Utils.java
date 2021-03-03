package com.ruoyi.project.common.util;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Md5Utils {
	
    private static Logger logger = LoggerFactory.getLogger(Md5Utils.class);

    public Md5Utils() {
    }

    public static String strToMd5(String str) {
        return strToMd5(str, "UTF-8");
    }

    public static String jsonToMd5(String key, String jsonContent) {
        char[] chars = jsonContent.toCharArray();
        Arrays.sort(chars);
        String encryptContent = strToMd5(key + new String(chars), "UTF-8");
        return encryptContent;
    }

    public static String jsonToMd5(String jsonContent) {
        char[] chars = jsonContent.toCharArray();
        Arrays.sort(chars);
        String encryptContent = strToMd5(new String(chars), "UTF-8");
        return encryptContent;
    }

    public static String jsonToMd5(String key, JSONObject paramJsonObject) {
        Set<String> keySet = paramJsonObject.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < keyList.size(); ++i) {
            sb.append(paramJsonObject.getString((String)keyList.get(i)));
        }

        sb.append(key);
        String encryptContent = strToMd5(sb.toString(), "UTF-8");
        return encryptContent.toUpperCase();
    }

    public static String strToMd5(String str, String charSet) {
        String md5Str = null;
        if(str != null && str.length() != 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes(charSet));
                byte[] b = md.digest();
                StringBuffer buf = new StringBuffer("");

                for(int offset = 0; offset < b.length; ++offset) {
                    int i = b[offset];
                    if(i < 0) {
                        i += 256;
                    }

                    if(i < 16) {
                        buf.append("0");
                    }

                    buf.append(Integer.toHexString(i));
                }

                md5Str = buf.toString();
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException var8) {
                logger.error("MD5加密发生异常。加密串：" + str);
                var8.printStackTrace();
            }
        }

        return md5Str;
    }

    public static void main(String[] args) {
        System.out.println(strToMd5("ZNLJ_RJCY"));
    }
}
