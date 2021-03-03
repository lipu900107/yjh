package com.ruoyi.project.api.dq.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.project.common.util.AesUtils;
import com.ruoyi.project.common.util.Common;
import com.ruoyi.project.common.util.HttpClientUtils;
import com.ruoyi.project.common.util.Md5Utils;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;

public class JlUtils {
	
	private static final Logger log = LoggerFactory.getLogger(JlUtils.class);
	
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
	 * 流量业务办理资格校验接口
	 * @param mobile
	 * @param productCode
	 * @param operateType
	 * @param cityCode
	 * @return
	 */
	public static ProductOrdersLog validHandle(String mobile, String productCode, String operateType, String cityCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3001";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject json = new JSONObject();
			json.put("productcode", productCode);
			json.put("operatetype", operateType);
			
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(json);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("citycode", cityCode);
			jsonObject.put("productinfo", jsonArray);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");
			
			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[validHandle][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}
	
	/**
	 * 短信验证码获取接口
	 * 
	 * @param codeType	01：短信随机码；02：网页数字验证码；03：积分业务随机码
	 * @return
	 */
	public static ProductOrdersLog reqCode(String mobile, String codeType, String cityCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3002";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("codetype", codeType);
			jsonObject.put("citycode", cityCode);
			
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
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 短信验证码校验接口
	 * 
	 * @param codeType	
	 * 01：短信随机码
	 * 02：网页数字验证码
	 * 03：积分业务随机码
	 * @return
	 */
	public static ProductOrdersLog validCode(String mobile, String codeType, String cityCode, String checkCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3003";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("checkcode", checkCode);
			jsonObject.put("codetype", codeType);
			jsonObject.put("citycode", cityCode);
			
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
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 手机号码状态校验接口
	 * @param mobile
	 * @param cityCode
	 * @return
	 */
	public static ProductOrdersLog validMobileStatus(String mobile, String cityCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3004";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("citycode", cityCode);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");

			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			/**
			 * userstatus 用户状态
			 * 00：正常
			 * 01：单向停机
			 * 02：停机 
			 * 03：预销户 
			 * 04：销户
			 * username	用户姓名（只返回姓名最后一个字）
			 */
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[validMobileStatus][result]:" + result);
			
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 流量包业务办理接口
	 * @param mobile
	 * @param orderNo
	 * @param productCode
	 * @param operateType
	 * @param cityCode
	 * @return
	 */
	public static ProductOrdersLog handleFlowpacket(String mobile, String orderNo, String productCode, String operateType, String cityCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3005";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject json = new JSONObject();
			json.put("productcode", productCode);
			json.put("operatetype", operateType);	// 可选值 A/D/U/N
			
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(json);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("channelorderid", orderNo);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("citycode", cityCode);
			jsonObject.put("productinfo", jsonArray);
			
			String data = generateParam(jsonObject);
			String sign = generateSign(partId, data, time+"");

			String param = "partid=" + partId + "&data=" + data + "&sign=" + sign + "&time=" + time;
			
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());
			
			String result = HttpClientUtils.sendPost(url, param, null);
			log.warn("[handleFlowpacket][result]:" + result);
			
			productOrdersLog.setEndTime(new Date());
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		
		return productOrdersLog;
	}
	
	/**
	 * 营销规则校验接口
	 * 
	 * @param mobile
	 * @param productCode
	 * @return
	 */
	public static ProductOrdersLog validMarketing(String mobile, String productCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3006";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("actid", "190214076182405883");
			jsonObject.put("gradeid", productCode);
			
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
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}
	
	/**
	 * 营销执行订购接口
	 * 
	 * @param mobile
	 * @param productCode
	 * @param ordersNo
	 * @param cityCode
	 * @return
	 */
	public static ProductOrdersLog handleMarketing(String mobile, String productCode, String ordersNo, String cityCode) {
		String url = Common.url;
		String partId = Common.partId;
		String interfaceCode = "N3007";
		Long time = System.currentTimeMillis();
		
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
    	productOrdersLog.setRequestInterfaces(url);
		
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("interfacecode", interfaceCode);
			jsonObject.put("phoneno", mobile);
			jsonObject.put("channelorderid", ordersNo);
			jsonObject.put("actid", "190214076182405883");
			jsonObject.put("gradeid", productCode);
			jsonObject.put("citycode", cityCode);
			
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
		productOrdersLog.setProductProvice("JL");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}
	
	public static void main(String[] args) {
		// productCode = JL22CAF0057 
		System.out.println("=========	吉林	========");
		/*reqCode("18243175627", "01", "");
		validCode("18243175627", "01", "", "34523");
		validHandle("18243175627", "JL22CAF0057", "A", "");
		handleFlowpacket("18243175627", "44444444444", "JL22CAF0057", "A", "");*/
		validCode("18243175627", "01", "", "34523");
		/*validMarketing("18243175627", "JLM190214076182405891");
		validMarketing("18243175627", "JLM190214076182405951");
		handleMarketing("18243175627", "JLM190214076182405951", "5555555", "2206");*/
	}
}
