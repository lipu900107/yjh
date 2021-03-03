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

public class SxUtils {
	
	/**
	 * SXYD69900043  营销包产品编码 营销包产品编码 
	 * SXYD96034471  营销包编码 承诺低消38 元，赠送50元话费（优惠5个月） 
	 * SXYD99412339 500M全国流量日包 
	 */
	
	private static final Logger log = LoggerFactory.getLogger(SxUtils.class);

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
	 * @return
	 */
	public static ProductOrdersLog reqCode(String mobile) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N5003";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			
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
		productOrdersLog.setProductProvice("SX");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 验证码校验接口
	 * @return
	 *//*
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
		productOrdersLog.setProductProvice("SX");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	*/
	
	/**
	 * 产品变更校验
	 * @param mobile
	 * @param orderNo
	 * @param productCode
	 * @return
	 */
	public static ProductOrdersLog validProduct(String mobile, String orderNo, String productCode, String smsCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N5002";
		long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("prodcode", productCode);
			jsonObject.put("modifytag", "0");	// 0: 开通; 1: 取消 
			
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
		
		productOrdersLog.setProductProvice("SX");
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
		String interfaceCode = "N5001";
		long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("channelorderid", orderNo);
			jsonObject.put("smscode", smsCode);
			jsonObject.put("prodcode", productCode);
			jsonObject.put("modifytag", "0");	// 0: 开通; 1: 取消 
			
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
		
		productOrdersLog.setProductProvice("SX");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 营销规则校验接口
	 * 
	 * @param mobile
	 * @param productCode	营销包产品编码
	 * @param marketingCode 营销包编码
	 * @return
	 */
	public static ProductOrdersLog validMarketing(String mobile, String productCode, String marketingCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N5005";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("packageid", marketingCode);
			jsonObject.put("prodcode", productCode);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");
			
			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[validMarketing][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("SX");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}
	
	/**
	 * 营销执行订购接口
	 * 
	 * @param mobile
	 * @param productCode
	 * @param marketingCode
	 * @param ordersNo
	 * @param smsCode
	 * @return
	 */
	public static ProductOrdersLog handleMarketing(String mobile, String productCode, String marketingCode, 
			String ordersNo, String smsCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N5006";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("smscode", smsCode);
			jsonObject.put("channelorderid", ordersNo);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("packageid", marketingCode);
			jsonObject.put("prodcode", productCode);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");
			
			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[validMarketing][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("SX");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}
	
	public static void main(String[] args) {
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog = reqCode("15101008680");
		System.out.println("result:" + productOrdersLog.getResponseResult());
	}
}
