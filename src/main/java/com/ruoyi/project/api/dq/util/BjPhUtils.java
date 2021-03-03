package com.ruoyi.project.api.dq.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.api.dq.bj.util.MobileSecurity;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;

public class BjPhUtils {

	private static final Logger log = LogManager.getLogger(BjPhUtils.class);

	private static String channelIpuUrl = "http://ysmapp.bj.10086.cn/app/channel-ipu/mobiledata";
	private static String channelLoginUrl = "http://ysmapp.bj.10086.cn/app/channel-app/mobiledata";

	/**
	 *
	 * @param mobile
	 * @param userName
	 * @param sessionId
	 * @return
	 */
	private static Map<String, String> generateParam(String mobile, String userName, String sessionId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("submitPhoneNO", mobile);
		map.put("SESSION_ID", sessionId);
		map.put("USERNAME", userName);
		return map;
	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static ProductOrdersLog loginUserName(String userName) {

		String logId = String.valueOf(System.currentTimeMillis());

		Map<String, String> map = new HashMap<String, String>();
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog.setRequestInterfaces(channelLoginUrl);

		try {
			String loginType = "DM";
			String imei = "865206027289856";
			String type = "HUAWEI G7-TL00";
			String model = "Android|" + type + "|" + imei;

			map.put("CHANNEL_CODE", userName);
			map.put("USERNAME", userName);
			map.put("MODEL", model);
			map.put("LOGIN_TYPE", loginType);

			String param = JSONObject.toJSONString(map).toString();
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());

			String url = "LoginBean.doLogin";
			String result = MobileSecurity.post(url, param, channelLoginUrl, null);
			log.info("{}|doLogin|param={}|result={}: {}", logId, param, result);
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		productOrdersLog.setProductProvice("BJPH");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}

	/**
	 *
	 * @param mobile
	 * @param userName
	 * @param sessionId
	 * @return
	 */
	public static ProductOrdersLog customerValidate(String mobile, String userName, String sessionId) {

		String logId = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = generateParam(mobile, userName, sessionId);
		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog.setRequestInterfaces(channelIpuUrl);

		try {
			String param = JSONObject.toJSONString(map).toString();
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());

			String url = "dataBusi.dataBusiCore.customerValidate";
			String result = MobileSecurity.post(url, param, channelIpuUrl, null);
			log.info("{}|customerValidate|param={}|result={}: {}", logId, param, result);
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		productOrdersLog.setProductProvice("BJPH");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}

	/**
	 *
	 * @param mobile
	 * @param userName
	 * @param sessionId
	 */
	public static ProductOrdersLog queryWarePlanPrivilege(String mobile, String userName, String sessionId) {

		String logId = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = generateParam(mobile, userName, sessionId);

		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog.setRequestInterfaces(channelIpuUrl);

		try {
			map.put("phoneNO", mobile);
			String param = JSONObject.toJSONString(map).toString();
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());

			String url = "promiseGetFlow.queryWarePlanPrivilege";
			String result = MobileSecurity.post(url, param, channelIpuUrl, null);
			log.info("{}|queryWarePlanPrivilege|param={}|result={}: {}", logId, param, result);
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		productOrdersLog.setProductProvice("BJPH");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}

	/**
	 *
	 * @param mobile
	 * @param packageName
	 * @param userName
	 * @param sessionId
	 * @param typeId
	 * @param planId
	 * @param fee
	 * @return
	 */
	public static ProductOrdersLog reqCode(String mobile, String packageName, String userName, 
			String sessionId, String wareTypeId, String warePlanId, String planFee) {

		String logId = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = generateParam(mobile, userName, sessionId);

		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog.setRequestInterfaces(channelIpuUrl);

		try {
			map.put("submitPhoneNO", mobile);
			map.put("selectedInfo", packageName);
			map.put("wareTypeId", wareTypeId);
			map.put("warePlanId", warePlanId);
			map.put("planFee", planFee);
			map.put("SESSION_ID", sessionId);
			map.put("USERNAME", userName);

			String param = JSONObject.toJSONString(map).toString();
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());

			String url = "promiseGetFlow.sendMsgPrivilege";
			String result = MobileSecurity.post(url, param, channelIpuUrl, null);
			log.info("{}|sendMsgPrivilege|param={}|result={}: {}", logId, param, result);
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		productOrdersLog.setProductProvice("BJPH");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}


	/**
	 *
	 * @param mobile
	 * @param code
	 * @param userName
	 * @param sessionId
	 * @return
	 */
	public static ProductOrdersLog validateCode(String mobile, String code, 
			String codeId, String userName, String sessionId) {

		String logId = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = generateParam(mobile, userName, sessionId);

		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog.setRequestInterfaces(channelIpuUrl);

		try {
			map.put("submitPhoneNO", mobile);
			map.put("submitCheckCode", code);
			map.put("submitId", codeId);
			map.put("SESSION_ID", sessionId);
			map.put("USERNAME", userName);

			String param = JSONObject.toJSONString(map).toString();
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());

			String url = "dataBusi.dataBusiCore.checkSmsCode";
			String result = MobileSecurity.post(url, param, channelIpuUrl, null);
			log.info("{}|checkSmsCode|param={}|result={}: {}", logId, param, result);
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		productOrdersLog.setProductProvice("BJPH");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;
	}

	/**
	 *
	 * @param mobile
	 * @param code
	 * @param userName
	 * @param sessionId
	 * @param planFee
	 * @return
	 */
	public static ProductOrdersLog handleBusiness(String mobile, String validCode, String token, 
			String submitId, String planId, String userName, String sessionId, String planFee) {

		String logId = String.valueOf(System.currentTimeMillis());
		Map<String, String> map = generateParam(mobile, userName, sessionId);

		ProductOrdersLog productOrdersLog = new ProductOrdersLog();	
		productOrdersLog.setRequestInterfaces(channelIpuUrl);

		try {
			map.put("submitPhoneNO", mobile);
			map.put("submitCheckCode", validCode);
			map.put("submitId", submitId);
			map.put("submitWarePlanId", planId);
			map.put("planFee", planFee);
			map.put("SESSION_ID", sessionId);
			map.put("USERNAME", userName);
			map.put("token", token);

			String param = JSONObject.toJSONString(map).toString();
			productOrdersLog.setRequestParam(param);
			productOrdersLog.setStartTime(new Date());

			String url = "promiseGetFlow.commitDataForQWPHDLL";
			String result = MobileSecurity.post(url, param, channelIpuUrl, null);
			log.info("{}|commitDataForQWPHDLL|param={}|result={}: {}", logId, param, result);
			productOrdersLog.setResponseResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		productOrdersLog.setProductProvice("BJPH");
		productOrdersLog.setEndTime(new Date());
		return productOrdersLog;

	}
	
	public static Map<String, String> getUserNameMap(String invalidUserName) {
		 Map<String, String> map = new LinkedHashMap<String, String>();
		 
		 map.put("WWCE120528","5");
		 map.put("WWNQ000049","5");
		 map.put("WWNQ000289","5");
		 
		 /*map.put("WWCE120258","30");
		 map.put("WWCE120219","6");
		 map.put("WWCE120240","6");
		 map.put("WWCE120785","6");
		 map.put("WWCE120255","6");
		 map.put("WWCE120236","6");
		 map.put("WWCY116273","10");
		 map.put("WWCY120028","10");
		 map.put("WWCY116278","10");
		 map.put("WWCY120324","10");
		 map.put("WWCY120365","5");
		 map.put("WWCY120374","10");
		 map.put("WWCS116242","5");
		 map.put("WWCP120495","5");
		 map.put("WWCE120557","5");
		 map.put("WWCE120559","5");
		 map.put("WWCE120561","15");
		 map.put("WWDX120382","21");
		 map.put("WWDX120381","21");
		 map.put("WWDX120455","21");
		 map.put("WWDX120456","21");
		 map.put("WWCS120182","5");
		 map.put("WWCY120731","10");
		 map.put("WWCY120732","10");
		 map.put("WWCY120733","10");
		 map.put("WWCE120845","13");
		 map.put("WWCE120843","12");
		 map.put("WWCE120784","6");
		 map.put("WWCE115795","6");
		 map.put("WWCE120142","10");
		 map.put("WWCE114849","10");
		 map.put("WWCE115682","10");
		 map.put("WWCE120753","6");
		 map.put("WWCE120032","6");
		 map.put("WWCE120025","6");
		 map.put("WWCE120260","6");
		 map.put("WWNQ000036","6");
		 map.put("WWSC121025","5");
		 map.put("WWSC121023","5");
		 map.put("WWSC121022","5");
		 map.put("WWSC121021","5");
		 map.put("WWSC121020","5");
		 map.put("WWCE120723","8");
		 map.put("WWCE120722","3");
		 map.put("WWCE120724","8");
		 map.put("WWZX000349","12");
		 map.put("WWCP114490","50");
		 map.put("WWCP114927","20");
		 map.put("WWCP000036","50");
		 map.put("WWCP000071","25");
		 map.put("WWCP114716","50");
		 map.put("WWCE120677","12");
		 map.put("WWCE120025","5");
		 map.put("WWZX102170","12");
		 map.put("WWCP120523","25");
		 map.put("WWCP120555","20");
		 map.put("WWCP000106","25");
		 map.put("WWCP101282","50");
		 map.put("WWCP114139","15");
		 map.put("WWCP107466","40");
		 map.put("WWCP101975","45");
		 map.put("WWCP114698","60");
		 map.put("WWCP120488","17");
		 map.put("WWCE120725","5");
		 map.put("WWCE120720","10");*/
		 
		 if(StringUtils.isNotBlank(invalidUserName)) {
			 map.remove(invalidUserName);
		 }
		 
		 return map;
	}

	private static String getUserName(String excludeUserName) {
		// 账户按照最大量处理
		String userNames = "WWFQ426,WWFQ423";
		String userName = "WWTZ120360";

		// 计算使用哪个工号
		String[] userNameArr = userNames.split(",");
		int maxNum = 0;
		int num = 2;
		for (String str : userNameArr) {
			if(StringUtils.isNotBlank(excludeUserName) && excludeUserName.equals(str)) {
				continue;
			}
			if("WWFQ426".equals(str)) {
    			maxNum = 10;
			} else if("WWFQ423".equals(str)) {
    			maxNum = 10;
    		}

			if(num >= maxNum) {
				num = 0;
				continue;
			}

			userName = str;
			break;
		}
		
		return userName;
	}

	public static void main(String[] args) {

		Map<String, String> map = new HashMap<String, String>();
		String userName = "WWCE120528";
		String mobile = "15810867545";
		String sessionId = "191603871743378";
		
		String param = "";
		String url = "";
		String result = "";

		/*
		String loginType = "DM";
		String imei = "865206027289856";
		String type = "HUAWEI G7-TL00";
		String model = "Android|" + type + "|" + imei;
		
		map.put("CHANNEL_CODE", userName);
		map.put("USERNAME", userName);
		map.put("MODEL", model);
		map.put("LOGIN_TYPE", loginType);

		param = JSONObject.toJSONString(map).toString();
		url = "LoginBean.doLogin";
		result = MobileSecurity.post(url, param, channelLoginUrl, null);
		System.out.println("result:" + result);
		
		JSONObject jsonObject = JSONObject.parseObject(result);
		sessionId = jsonObject.getString("SESSION_ID");
		System.out.println("userName:" + userName);
		
		System.out.println("sessionId:" + sessionId);
		*/
		
		/*
		map = generateParam(mobile, userName, sessionId);
		param = JSONObject.toJSONString(map).toString();
		url = "dataBusi.dataBusiCore.customerValidate";
		result = MobileSecurity.post(url, param, channelIpuUrl, null);
		System.out.println("customerValidate.result:" + result);

		map.put("phoneNO", mobile);
		param = JSONObject.toJSONString(map).toString();
		url = "promiseGetFlow.queryWarePlanPrivilege";
		result = MobileSecurity.post(url, param, channelIpuUrl, null);
		System.out.println("queryWarePlanPrivilege.result:" + result);
		
		JSONObject jsonObject = JSONObject.parseObject(result);
		result = jsonObject.getString("resultCode");
		
		String warePlans = jsonObject.getString("warePlans");
		JSONArray jsonArray = JSONArray.parseArray(warePlans);
		jsonObject = jsonArray.getJSONObject(0);

		String warePlanId = jsonObject.getString("ware_plan_id");
		String planFee = jsonObject.getString("plan_fee");
		String planName = jsonObject.getString("plan_name");
		
		String packageName = "全网优惠享特权+" + planName;
		String typeId = "30000102";
		
		map.put("submitPhoneNO", mobile);
		map.put("selectedInfo", packageName);
		map.put("wareTypeId", typeId);
		map.put("warePlanId", warePlanId);
		map.put("planFee", planFee);
		map.put("SESSION_ID", sessionId);
		map.put("USERNAME", userName);

		param = JSONObject.toJSONString(map).toString();
		
		url = "promiseGetFlow.sendMsgPrivilege";
		result = MobileSecurity.post(url, param, channelIpuUrl, null);
		System.out.println("sendMsgPrivilege.result:" + result);
		*/
		
		/*
		JSONObject jsonObject = JSONObject.parseObject(result);
		String warePlans = jsonObject.getString("warePlans");
		JSONArray jsonArray = JSONArray.parseArray(warePlans);
		jsonObject = jsonArray.getJSONObject(0);

		String planFee = jsonObject.getString("plan_fee");
		String arpuAvg = jsonObject.getString("arpu_avg");

		BigDecimal bignum1 = new BigDecimal(planFee.substring(0, planFee.lastIndexOf("元")));  
		//减法  
		BigDecimal bignum2 = new BigDecimal(arpuAvg.substring(0, arpuAvg.lastIndexOf("元")));  
		bignum1 = bignum1.subtract(bignum2);  
		System.out.println("Y：" + bignum1.toString());
		*/
		
		
		
		String submitId = "20844351";
		String checkCode = "955161";
		
		map.put("submitPhoneNO", mobile);
		map.put("submitCheckCode", checkCode);
		map.put("submitId", submitId);
		map.put("SESSION_ID", sessionId);
		map.put("USERNAME", userName);

		param = JSONObject.toJSONString(map).toString();
		url = "dataBusi.dataBusiCore.checkSmsCode";
		result = MobileSecurity.post(url, param, channelIpuUrl, null);
		System.out.println("checkSmsCode.result:" + result);
		
	}
}
