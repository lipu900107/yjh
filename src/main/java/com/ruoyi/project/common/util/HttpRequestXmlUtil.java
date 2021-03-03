package com.ruoyi.project.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sun.misc.BASE64Decoder;

public class HttpRequestXmlUtil {
	
	private static  String  publicKey_1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2FMmfMMs3iV1tpCW5LWy7WnershYTbWyKKVtjVhRk0NXgg509XUtKQaVVxNRunI5hryK6j9fV5c5kjrGJupfI3/8HIvQGCO/zpCJPX9uVDi+uQnrqKhgLFnKRKlHWIhMBrI2C/qaE7p1T5FOgRLnWDi7RIKP/+GgPbpeG1AAi3QIDAQAB";
	
	public static void main(String[] args) throws Exception {
		/*Map<String, Object> map = new HashMap<String, Object>();
		String requestSeq = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String requestTime = sdf.format(new Date());
		map.put("RequestSEQ", "300011997188" + requestTime);
		map.put("RequestTime", requestTime);
		map.put("APID", "736692");
		map.put("APPID", "300011997188");
		map.put("ServCode", "DZBQ-AA02");
		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put("CookieID", "1584566464053476668");
		map.put("ServArgs", argsMap);
		
		Map<String, Object> parartMap = new HashMap<String, Object>();
		parartMap.put("OpenDataRequest", map);
		
		String ap_id = "736692";
		String app_id = "300011997188";
		String app_key = "4215D28F54725F2AC92F74BBAC7C0610";
		String client_code = "by-ys1-yjh";
		String password = "";
		String seq = requestTime;
		String accessName = "accessName=\"" + ap_id + "#" + app_id + "\",";
		String clientCode = "clientCode=\"" + client_code + "\",";
		String content = ap_id + "#" + app_id + "#" + app_key + "#" + seq;
		String account = "account=\"\"";
		String code = "";
		try {
			code = "code\"" + AesUtils.aesEncrypt(content, password) + "\",";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String authorization = accessName + clientCode + code + account;
		
		HttpRequestXmlUtil httpRequestXmlUtil = new HttpRequestXmlUtil();
		String xmlInfo = httpRequestXmlUtil.multilayerMapToXml(parartMap, false);
		System.out.println(xmlInfo);
		String urlStr = "https://open.mmarket.com/cap/dataservice/appDataServiceAuthen.proxy";
		String result = testPost(urlStr, xmlInfo, authorization);
		System.out.println(result);*/
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		String requestSeq = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String requestTime = sdf.format(new Date());
		map.put("apId", "736692");
		map.put("apKey", "AD3691E089EA3367E0533FAE990AC30C");
		map.put("clientCode", "by-ys1-yjh");
		Map<String, Object> parartMap = new HashMap<String, Object>();
		parartMap.put("applyKey", map);
		
		HttpRequestXmlUtil httpRequestXmlUtil = new HttpRequestXmlUtil();
		String xmlInfo = httpRequestXmlUtil.multilayerMapToXml(parartMap, false);
		System.out.println(xmlInfo);
		String urlStr = "https://open.mmarket.com/cap/dataservice/ApplyKey2.proxy";
		String result = testPost(urlStr, xmlInfo, "");
		result = decryptByPublicKey(result, publicKey_1);
		System.out.println("返回结果：" + result);
		
	}
	
	public static String decryptByPublicKey(String param, String publicKey)
            throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bates = decoder.decodeBuffer(param);
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicKey);
        //  byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = bates.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(bates, offSet, 128);
            } else {
                cache = cipher.doFinal(bates, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String pubDecode = new String(decryptedData, "UTF-8");
        return pubDecode;
    }
	
	public static String testPost(String urlStr,String xmlInfo,String authorization) {
		String result = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            //con.setRequestProperty("Authorization", authorization);

            OutputStreamWriter out = new OutputStreamWriter(con
                    .getOutputStream());    
            //String xmlInfo = getXmlInfo();
            System.out.println("urlStr=" + urlStr);
            System.out.println("xmlInfo=" + xmlInfo);
            out.write(new String(xmlInfo.getBytes("utf-8")));
            out.flush();
            out.close();
            
            if (con.getResponseCode() == 200) {
                System.out.println("连接成功");
                InputStream in = con.getInputStream();
                byte[] data1 = readInputStream(in);
                result = new String(data1);

            } else {
                System.out.println("连接失败");
            }
            /*BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream()));
            
            
            for (line = br.readLine(); line != null; line = br.readLine()) {
                System.out.println(line);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[10240];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();

        return outStream.toByteArray();
    }
	
	/**
     * (多层)map转换为xml格式字符串
     *
     * @param map 需要转换为xml的map
     * @param isCDATA 是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    public static String multilayerMapToXml(Map<String, Object> map, boolean isCDATA){
        String parentName = "xml";
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = recursionMapToXml(doc.getRootElement(), parentName, map, isCDATA);
        return formatXML(xml);
    }

    /**
     * multilayerMapToXml核心方法，递归调用
     *
     * @param element 节点元素
     * @param parentName 根元素属性名
     * @param map 需要转换为xml的map
     * @param isCDATA 是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    @SuppressWarnings("unchecked")
    private static String recursionMapToXml(Element element, String parentName, Map<String, Object> map, boolean isCDATA) {
        Element xmlElement = element.addElement(parentName);
        map.keySet().forEach(key -> {
            Object obj = map.get(key);
            if (obj instanceof Map) {
                recursionMapToXml(xmlElement, key, (Map<String, Object>)obj, isCDATA);
            } else {
                String value = obj == null ? "" : obj.toString();
                if (isCDATA) {
                    xmlElement.addElement(key).addCDATA(value);
                } else {
                    xmlElement.addElement(key).addText(value);
                }
            }
        });
        return xmlElement.asXML();
    }
    
    /**
     * 格式化xml,显示为容易看的XML格式
     *
     * @param xml 需要格式化的xml字符串
     * @return
     */
    public static String formatXML(String xml) {
        String requestXML = null;
        try {
            // 拿取解析器
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            if (null != document) {
                StringWriter stringWriter = new StringWriter();
                // 格式化,每一级前的空格
                OutputFormat format = new OutputFormat("    ", true);
                // xml声明与内容是否添加空行
                format.setNewLineAfterDeclaration(false);
                // 是否设置xml声明头部
                format.setSuppressDeclaration(false);
                // 是否分行
                format.setNewlines(true);
                XMLWriter writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                writer.close();
                requestXML = stringWriter.getBuffer().toString();
            }
            return requestXML;
        } catch (Exception e) {
            return null;
        }
    }
}
