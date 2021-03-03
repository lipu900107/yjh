package com.ruoyi.project.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.api.znlj.util.ConstantConfig;

public class TokenValidate {

	/**
	 * 将传入的参数自动变成报文
	 * @Title: 请求排序key不为null
	 * @param apptype,key,token,userInformation
	 * @return requestJson
	 */
	private static JSONObject requestSort(String apptype, String key, String token, String userInformation) {
		JSONObject requestJson = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("msgid", ToolUtils.getUUID());
		header.put("id", ConstantConfig.znlj_appid);
		header.put("version", "1.2");
		header.put("systemtime", DateUtils.dateTimeNow("yyyyMMddHHmmssSSS"));
		header.put("idtype", "1");
		
		header.put("apptype",apptype);
		header.put("key", key);
		//header.put("expandparams", "");
		requestJson.put("header", header);

		JSONObject body = new JSONObject();
		body.put("token", token);
		body.put("userInformation", userInformation);
		requestJson.put("body", body);
		return requestJson;
	}

	/**
	 * 将传入的参数自动变成报文
	 * @Title: 请求排序key为null
	 * @param apptype,token
	 * @return JSONObject
	 */
	private static JSONObject requestSort(String apptype, String token) {
		JSONObject requestJson = new JSONObject();
		JSONObject header = new JSONObject();
		
		header.put("msgid", ToolUtils.getUUID());
		header.put("systemtime", DateUtils.dateTimeNow("yyyyMMddHHmmssSSS"));
		header.put("version", "1.2");
		header.put("id", ConstantConfig.znlj_appid);
		header.put("idtype", "1");
		header.put("apptype", apptype);
		
		JSONObject body = new JSONObject();
		body.put("token", token);
		
		requestJson.put("header", header);
		requestJson.put("body", body);
		return requestJson;
	}
	
	/**
	 * 将传入的参数自动变成报文
	 * @Title: 创建签名
	 * @param apptype,key,token,userInformation
	 * @return String
	 */
	public static String createSign(String apptype, String token, String userInformation){
		JSONObject requestSort = requestSort(apptype, ConstantConfig.znlj_appkey, token, userInformation);
		String reauestJson = createSign(requestSort.toJSONString());
		return reauestJson;
	}

	/**
	 * 将传入的参数自动变成报文
	 * @Title: 创建签名 key 为null
	 * @param apptype,token
	 * @return
	 */
	public static String createSign(String apptype, String token){
		JSONObject requestSort = requestSort(apptype, token);
		String reauestJson = createSign(requestSort.toJSONString());
		return reauestJson;
	}

	/**
	 * 
	 * 将传入的参数自动变成报文
	 * 
	 * @Title: 创建签名
	 * @param JsonString
	 * @return
	 * @author:
	 */
	public static String createSign(String JsonString){
		JSONObject json = JSONObject.parseObject(JsonString);
		String sign ="";

		if (json == null) {
			throw new IllegalArgumentException("请求数据不能为空");
		}
		JSONObject respheader = json.getJSONObject("header");
		JSONObject respbody = json.getJSONObject("body");
		if (respheader == null) {
			throw new IllegalArgumentException("请header不能为空");
		}
		if (respbody == null) {
			throw new IllegalArgumentException("请body不能为空");
		}
		String apptype =(String)respheader.get("apptype");
		String id =(String)respheader.get("id");
		String idtype =(String)respheader.get("idtype");
		String key =(String)respheader.get("key");
		String msgid =(String)respheader.get("msgid");
		String systemtime =(String)respheader.get("systemtime");
		String version =(String)respheader.get("version");
		String token =(String)respbody.get("token");
		if(StringUtils.isBlank(apptype)){
			throw new IllegalArgumentException("apptype不能为空");
		}
		if(StringUtils.isBlank(id)){
			throw new IllegalArgumentException("id不能为空");
		}
		if(StringUtils.isBlank(idtype)){
			throw new IllegalArgumentException("idtype不能为空");
		}
		if(StringUtils.isBlank(msgid)){
			throw new IllegalArgumentException("msgid不能为空");
		}
		if(StringUtils.isBlank(systemtime)){
			throw new IllegalArgumentException("systemtime不能为空");
		}
		if(StringUtils.isBlank(version)){
			throw new IllegalArgumentException("version不能为空");
		}
		if(StringUtils.isBlank(token)){
			throw new IllegalArgumentException("token不能为空");
		}
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("version", version);
		map.put("id", id);
		map.put("idtype", idtype);
		map.put("msgid", msgid);
		map.put("token", token);
		map.put("systemtime", systemtime);
		map.put("apptype", apptype);
		if(StringUtils.isBlank(key)){
			String enStr = mapToString(map);
			sign = md5(enStr);
		}else{
			map.put("key", key);
			String enStr = mapToString(map);
			sign = md5(enStr);
		}
		json.getJSONObject("header").put("sign", sign);
		json.getJSONObject("header").remove("key");
		return json.toJSONString();
	}

	/**
	 * 
	 * 将Map转换为String
	 * 
	 * @Title: map2String
	 * @param map
	 * @return
	 * @author: yanhuajian 2013-7-21下午7:25:08
	 */
	private static String mapToString(Map<String, Object> map) {
		if (null == map || map.isEmpty()) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getValue());
		}

		return sb.toString();
	}

	private static String md5(String text) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes("UTF-8"));
			byte[] b = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 发送post请求
	 * @Title: 发送post请求
	 * @param requestJson
	 * @return String
	 */
	public static String postMethod(String url, String reauestJson)
			throws UnsupportedEncodingException, IOException, HttpException {
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		RequestEntity entity = new StringRequestEntity(reauestJson, null, "utf-8");
		postMethod.setRequestEntity(entity);
		postMethod.addRequestHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
		postMethod.addRequestHeader("Api-Version", "1");
		client.executeMethod(postMethod);
		String resp1 = postMethod.getResponseBodyAsString();
		return resp1;
	}
	
	
	// ====================================================================================
			
	
	/**
	 * 获取accessToken和refreshToken
	 * @param refreshToken
	 * @return JSONObject
	 */
	public static JSONObject requestToken(String refreshToken) {
		JSONObject requestJson = new JSONObject();
		
		requestJson.put("appId", ConstantConfig.znlj_appid);
		requestJson.put("appKey", ConstantConfig.znlj_appkey);
		requestJson.put("reqId", ToolUtils.getUUID());
		requestJson.put("timestamp", DateUtils.dateTimeNow("yyyy-MM-dd HH:mm:ss"));
		if(!StringUtils.isEmpty(refreshToken)) {
			requestJson.put("refreshToken", refreshToken);
		}
		return requestJson;
	}

	/**
	 * 获取accessToken和refreshToken
	 * @param refreshToken
	 * @return JSONObject
	 */
	public static JSONObject requestSendAuthMsg(String accessToken, String mobile, String templateId, String templateParam, String callbackUrl) {
		JSONObject requestJson = new JSONObject();
		
		requestJson.put("appId", ConstantConfig.znlj_appid);
		requestJson.put("appKey", ConstantConfig.znlj_appkey);
		requestJson.put("reqId", ToolUtils.getUUID());
		requestJson.put("timestamp", DateUtils.dateTimeNow("yyyyMMddHHmmss"));
		requestJson.put("accessToken", accessToken);
		requestJson.put("phone", mobile);
		requestJson.put("templateId", templateId);
		requestJson.put("templateParam", templateParam);
		requestJson.put("callBackUrl", callbackUrl);
		return requestJson;
	}
	
	
	public static void main(String[] args) throws HttpException, IOException {
		/*String userInformation = "";
		String createSign = TokenValidate.createSign("5", "H529565dbc98b2ab706ddbb94d0e2e25e0", userInformation);
		System.out.println("createSign:" + createSign);
		String response = postMethod(ConstantConfig.ceshi_tokenValidateUrl, createSign);
		System.out.println("result:" + response);*/
		
		String parameter = TokenValidate.requestToken(null).toJSONString();
		System.out.println("parameter:" + parameter);
		String response = postMethod(ConstantConfig.accessTokenUrl, parameter);
		System.out.println("result:" + response);
		
		/**
		 * sendAuthMsg 
		 * 响应示例：{"appName": "","msgText": "非口令登录，请点击确定","result": "1","resultDes": "成功","serviceName": "dev.30000011816219","taskId": "MS201911221612343a","templateId": "DF2019112115570394","templateName": "非口令登录模板1"
		 * 回调示例：{"phoneSendStat":{"phone":"18702003243","state":0},"result":"1","isPaid ":true,"resultDes":"成功","taskId":"MS2019112220500556","taskStartTime":"2019-11-22 20:50:06"}
		 */
		
		/*String mobile = "18304072984";
		mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		System.out.println("mobile:" + mobile);*/
	}
}
