package com.ruoyi.project.api.dq.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.project.common.util.AesUtils;
import com.ruoyi.project.common.util.Common;
import com.ruoyi.project.common.util.HttpClientUtils;
import com.ruoyi.project.common.util.Md5Utils;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;

public class HbUtils {
	
	private static final Logger log = LoggerFactory.getLogger(HbUtils.class);

	public static String generateParam(JSONObject jsonObject) {
		try {
			if(jsonObject == null)return null;
			
			String param = jsonObject.toJSONString();
			log.warn("[generateParam][param]:" + param);
			
			param = AesUtils.aesEncrypt(param, Common.secretkey);
			log.warn("[generateParam][AesUtils][param]:" + param);
			
			return param;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String generateSign(String partId, String data, String time) {
		try {
			JSONObject json = new JSONObject();
			json.put("partid", partId);
			json.put("data", data);
			json.put("time", time);
			log.warn("[generateSign][json]:" + json.toJSONString());
			
			String sign = Md5Utils.jsonToMd5(json.toJSONString());
			log.warn("[generateSign][sign]:" + sign);
			return sign;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取验证码接口 
	 * @param productCode	HBrb10072;HByb10776;HByb10500
	 * @return
	 */
	public static ProductOrdersLog reqCode(String mobile, String productCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N2101";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("productcode", productCode);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");

			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[reqCode][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("HB");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	

	/**
	 * 验证码校验接口
	 * @return
	 */
	public static ProductOrdersLog validCode(String mobile, String productCode, String smsCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N2102";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("smscode", smsCode);
			jsonObject.put("productcode", productCode);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");
			
			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[validCode][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("HB");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 业务办理
	 * @param mobile
	 * @param orderNo
	 * @param productCode
	 * @return
	 */
	public static ProductOrdersLog handle(String mobile, String orderNo, String productCode, String smsCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N2103";
		long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("channelorderid", orderNo);
			jsonObject.put("smscode", smsCode);
			jsonObject.put("productcode", productCode);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");
			
			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;

			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[handle][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		productOrdersLog.setProductProvice("HB");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	public static void main(String[] args) {
		/**
		 * HBrb10072	￥5 1G日包 5元1G日包 
		 * HByb10776	￥30 4G月包  30 元 4G 月包_送 30 元话费
		 * HByb10500	￥50 5G月包 50元5G月包
		 */
		System.out.println("=========	河北	 ========");
		System.out.println(reqCode("17886510703", "HBrb10072"));
		System.out.println(validCode("17886510703", "HBrb10072", "123456"));
		System.out.println(handle("17886510703", "33333333333", "HBrb10072", "123456"));
	}
}
