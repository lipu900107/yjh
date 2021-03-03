package com.ruoyi.project.api.dq.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.api.dq.util.BjPhUtils;
import com.ruoyi.project.api.dq.util.BjPhVo;
import com.ruoyi.project.common.util.RedisUtil;
import com.ruoyi.project.common.util.ToolUtils;
import com.ruoyi.project.system.productFlowpacketOrders.domain.ProductFlowpacketOrders;
import com.ruoyi.project.system.productFlowpacketOrders.service.IProductFlowpacketOrdersService;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;
import com.ruoyi.project.system.productOrdersLog.service.IProductOrdersLogService;

/**
 * 北京电渠普惠接口
 * 
 */
@Controller
@CrossOrigin
@RequestMapping("/api/dq/bjph")
public class BjPhController extends BaseController {

	private static final Logger log = LogManager.getLogger(BjPhController.class);

	@Autowired
	private IProductFlowpacketOrdersService productFlowpacketOrdersService;

	@Autowired
	private IProductOrdersLogService productOrdersLogService;

	private String getUserName(String excludeUserName, String mobile, String originIp, String originUrl) {

		// 随机
		List<String> userNameList = new ArrayList<String>();
		/*userNameList.add("WWFQ426");
		userNameList.add("WWFQ423");*/
		userNameList.add("WWTZ120360");
		userNameList.add("WWTZ120359");
		userNameList.add("WWTZ120358");

		Random random = new Random();
		int n = random.nextInt(userNameList.size());
		String userName = userNameList.get(n);

		if(StringUtils.isBlank(userName) || "null".equals(userName)) {
			userName = "WWFQ426";
		}

		// 账户按照最大量处理，计算使用哪个工号
		Map<String, String> map = BjPhUtils.getUserNameMap("");
		int maxNum = 0;
		int num = 0;
		String numStr = "", str = "", maxNumStr = "";
		
		int mapNum = 0;
		int mapSize = map.size();

		for (Entry<String, String> entry: map.entrySet()) {
			str = entry.getKey();
			maxNumStr = entry.getValue();

			if(StringUtils.isBlank(str) || StringUtils.isBlank(maxNumStr)) continue;
			if(StringUtils.isNotBlank(excludeUserName) && excludeUserName.equals(str)) continue;

			maxNum = Integer.valueOf(maxNumStr);

			numStr = RedisUtil.get("num:" + str);
			log.info("mobile: {}, numStr: {}, str: {}", mobile, numStr, str);
			if(StringUtils.isNotBlank(numStr) && !"null".equals(numStr)) {
				num = Integer.valueOf(numStr);
			}
			if(num >= maxNum) {
				num = 0;
				mapSize--;
				continue;
			}
			
			/*log.info("mobile: {}, mapSize: {}, userName: {}, mapNum: {}", mobile, mapSize, userName, mapNum);
			if(num > 0) {// 每五个切换账号，余数为0，证明整除
				if((maxNum-num)%5 == 0){
					if(mapNum == mapSize) {
					} else {
						mapNum++;
						continue;
					}
				}
			}*/

			userName = str;

			//if(maxNum-num <= 3) {
				// 如果锁住工号，就暂停切换下一个
				String lockUserName = RedisUtil.get("lock:" + userName);
				log.info("mobile: {}, maxNum: {}, num: {}, lockUserName: {}", mobile, maxNum, num, lockUserName);
				if(StringUtils.isNotBlank(lockUserName) && !"null".equals(lockUserName)) {
					num = 0;
					continue;
				}

				// 锁住工号
				RedisUtil.set("lock:" + userName, "lock", 60);
			//}

			break;
		}


		try {
			ProductOrdersLog productOrdersLog = new ProductOrdersLog();
			JSONObject jsonObject = new JSONObject();
			String result = "";

			String key = "username:" + userName;
			String sessionId = RedisUtil.get(key);
			if(StringUtils.isBlank(sessionId) || "null".equals(sessionId)) {
				/**
				 * 
				 */
				productOrdersLog = BjPhUtils.loginUserName(userName);
				productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);

				result = productOrdersLog.getResponseResult();
				if(StringUtils.isBlank(result)) {
					return getUserName(userName, mobile, originIp, originUrl);
				}

				jsonObject = JSONObject.parseObject(result);
				sessionId = jsonObject.getString("SESSION_ID");
				if(StringUtils.isBlank(sessionId)) {
					return getUserName(userName, mobile, originIp, originUrl);
				}
				RedisUtil.set(key, sessionId, 60*30);		// 1个小时
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return userName;
	}

	private void delLoginRedis(String repResult, String userName) {
		String key = "username:" + userName;
		if(StringUtils.isNotBlank(repResult)) {
			JSONObject jsonObject = JSONObject.parseObject(repResult);
			String resultCode = jsonObject.getString("X_RESULTCODE");
			if("-100".equals(resultCode)) {
				RedisUtil.delete(key);
				BjPhUtils.getUserNameMap(userName);
			}
		}
	}

	@RequestMapping(value = "/sendCode",method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendCode(String mobile, String originUrl, HttpServletRequest request)
	{
		log.info("mobile: {}, origin: {}", mobile, originUrl);

		String originIp = ToolUtils.getRemoteAddr(request);
		log.info("mobile: {}, originIp: {}", mobile, originIp);
		if (StringUtils.contains(originIp, ",")) {
			String[] split = originIp.split(",");
			originIp = split[0];
		}

		if(StringUtils.isBlank(mobile)) {
			return AjaxResult.error(211, "手机号不能为空，请重新输入");
		}

		String userName = getUserName("", mobile, originIp, originUrl);
		if(StringUtils.isBlank(userName)) {
			return AjaxResult.error(211, "接口异常，请稍后再试");
		}

		try {
			ProductOrdersLog productOrdersLog = new ProductOrdersLog();
			JSONObject jsonObject = new JSONObject();
			String result = "";

			String key = "username:" + userName;
			String sessionId = RedisUtil.get(key);
			log.info("mobile: {}, userName: {}, sessionId: {}", mobile, userName, sessionId);
			if(StringUtils.isBlank(sessionId) || "null".equals(sessionId)) {
				return AjaxResult.error(211, "接口异常，请稍后再试");
			}

			/**
			 * 
			 */
			productOrdersLog = BjPhUtils.customerValidate(mobile, userName, sessionId);
			productOrdersLog.setMobile(mobile);
			productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);

			result = productOrdersLog.getResponseResult();
			if(StringUtils.isBlank(result)) {
				return AjaxResult.error(211, "接口异常，请稍后再试");
			}
			delLoginRedis(result, userName);

			jsonObject = JSONObject.parseObject(result);
			String errorCode = jsonObject.getString("errorCode");
			if(!"success".equals(errorCode)) {
				return AjaxResult.error(211, "抱歉，您暂时不符合办理条件！");
			}


			/**
			 * 
			 */
			productOrdersLog = BjPhUtils.queryWarePlanPrivilege(mobile, userName, sessionId);
			productOrdersLog.setMobile(mobile);
			productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);

			result = productOrdersLog.getResponseResult();
			if(StringUtils.isBlank(result)) {
				return AjaxResult.error(211, "接口异常，请稍后再试");
			}

			jsonObject = JSONObject.parseObject(result);
			result = jsonObject.getString("resultCode");
			errorCode = jsonObject.getString("errorCode");
			if(!"00000".equals(result) && !"success".equals(errorCode)) {
				return AjaxResult.error(211, jsonObject.getString("resultMsg"));
			}

			String warePlans = jsonObject.getString("warePlans");
			JSONArray jsonArray = JSONArray.parseArray(warePlans);
			if(jsonArray == null || jsonArray.size() == 0) {
				return AjaxResult.error(211, "抱歉，您的套餐不符合办理条件");
			}
			jsonObject = jsonArray.getJSONObject(0);

			String warePlanId = jsonObject.getString("ware_plan_id");
			String planFee = jsonObject.getString("plan_fee");
			String planId = jsonObject.getString("plan_id");
			String planName = jsonObject.getString("plan_name");
			String planFlow = jsonObject.getString("plan_flow");
			String arpuAvg = jsonObject.getString("arpu_avg");

			if(StringUtils.isBlank(warePlanId) || StringUtils.isBlank(planFee)
					|| StringUtils.isBlank(planName) || StringUtils.isBlank(planId)
					|| StringUtils.isBlank(planFlow)) {
				return AjaxResult.error(211, "接口异常，请稍后再试");
			}

			/**
			 * 
			 */
			String typeId = "30000102";
			productOrdersLog = BjPhUtils.reqCode(mobile, "全网优惠享特权+" + planName, 
					userName, sessionId, typeId, warePlanId, planFee);
			productOrdersLog.setMobile(mobile);
			productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);

			result = productOrdersLog.getResponseResult();
			if(StringUtils.isBlank(result)) {
				return AjaxResult.error(211, "接口异常，请稍后再试");
			}

			jsonObject = JSONObject.parseObject(result);
			result = jsonObject.getString("X_RESULTCODE");
			errorCode = jsonObject.getString("errorCode");
			String submitId = "";
			if("0".equals(result) && "success".equals(errorCode)) {
				submitId = jsonObject.getString("id");
				if(StringUtils.isBlank(submitId)) {
					return AjaxResult.error(211, "接口异常，请稍后再试");
				}

				BjPhVo vo = new BjPhVo();
				vo.setPlanId(planId);
				vo.setPlanName(planName);
				vo.setPlanFee(planFee);
				vo.setWarePlanId(warePlanId);
				vo.setSubmitId(submitId);
				vo.setUserName(userName);

				key = "vo:mobile:" + mobile;
				RedisUtil.set(key, JSONObject.toJSONString(vo).toString(), 60*60); 

				BigDecimal bignum1 = new BigDecimal(planFee.substring(0, planFee.lastIndexOf("元")));  
				BigDecimal bignumX = new BigDecimal(arpuAvg.substring(0, arpuAvg.lastIndexOf("元"))); 
				BigDecimal bignumA = new BigDecimal(planFlow);

				//减法  
				BigDecimal bignumY = bignum1.subtract(bignumX);  
				log.info("mobile: {}, x: {}, y: {}, a: {}", mobile, bignumX, bignumY, bignumA);

				JSONObject respJson = new JSONObject();
				respJson.put("X", bignumX.toString());
				respJson.put("Y", bignumY.toString());
				respJson.put("A", bignumA.toString());
				return AjaxResult.success(200, "成功", respJson);
			} else {
				String errdesc = jsonObject.getString("errorMsg");
				return AjaxResult.error(211, errdesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error(201, "接口异常，请稍后再试");
		}
	}

	@PostMapping("/saveOrders")
	@ResponseBody
	public AjaxResult saveOrders(String mobile, String originUrl, String validCode, HttpServletRequest request)
	{
		log.info("mobile: {}, originUrl: {}", mobile, originUrl);

		String originIp = ToolUtils.getRemoteAddr(request);
		log.info("originIp: {}", originIp);
		if (StringUtils.contains(originIp, ",")) {
			String[] split = originIp.split(",");
			originIp = split[0];
		}

		if(StringUtils.isBlank(mobile)) {
			return AjaxResult.error(211, "手机号不能为空，请重新输入");
		}

		if(StringUtils.isBlank(validCode)) {
			return AjaxResult.error(211, "验证码不能为空，请重新输入");
		}

		/**
		 * 需要从缓存获取
		 */
		String key = "vo:mobile:" + mobile;
		String voJson = RedisUtil.get(key);
		if(StringUtils.isBlank(voJson)) {
			return AjaxResult.error(211, "检验错误，请重新获取验证码");
		}
		BjPhVo vo = JSONObject.parseObject(voJson, BjPhVo.class);

		String userName = vo.getUserName();
		String submitId = vo.getSubmitId();
		String planFee = vo.getPlanFee();
		String planName = vo.getPlanName();
		String warePlanId = vo.getWarePlanId();

		key = "username:" + userName;
		String sessionId = RedisUtil.get(key);

		try {
			// 验证输入验证码
			ProductOrdersLog productOrdersLog = BjPhUtils.validateCode(mobile, validCode, submitId, userName, sessionId);
			productOrdersLog.setMobile(mobile);
			productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);

			String result = productOrdersLog.getResponseResult();
			if(StringUtils.isBlank(result)) {
				return AjaxResult.error(211, "验证码验证错误，请重新获取");
			}

			JSONObject jsonObject = JSONObject.parseObject(result);
			result = jsonObject.getString("X_RESULTCODE");
			String errorCode = jsonObject.getString("errorCode");
			String token = jsonObject.getString("token");
			
			boolean flag = false;
			if("0".equals(result) && "success".equals(errorCode)) {
				flag = true;
			}


			String uuid = ToolUtils.getUUID();
			String ordersNo = uuid + DateUtils.dateTimeNow("yyyyMMddHHmmssSSS");
			String reCode = "";
			String resultInfo = "";

			if(flag) {	// 业务办理
				productOrdersLog = BjPhUtils.handleBusiness(mobile, validCode, token, submitId, warePlanId, 
						userName, sessionId, planFee);
				productOrdersLog.setMobile(mobile);
				productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);

				result = productOrdersLog.getResponseResult();
				if(StringUtils.isBlank(result)) {
					return AjaxResult.error(211, "业务办理异常，请稍后再试");
				}
				
				// 多重试一次测试
				jsonObject = JSONObject.parseObject(result);
				reCode = jsonObject.getString("X_RESULTCODE");
				resultInfo = jsonObject.getString("X_RESULTINFO");
				if("-888".equals(reCode)) {
					Thread.sleep(5000);
					log.info("系统重试：mobile: {}, userName: {}", mobile, userName);
					productOrdersLog = BjPhUtils.handleBusiness(mobile, validCode, token, submitId, warePlanId, 
							userName, sessionId, planFee);
					productOrdersLog.setMobile(mobile);
					productOrdersLogService.insertProductOrdersLog(productOrdersLog, originIp, originUrl);
				}
				
				
				errorCode = jsonObject.getString("errorCode");
				String resultCode = jsonObject.getString("resultCode");
				if("success".equals(errorCode) && "00000".equals(resultCode)) {
					flag = true;
				} else {
					flag = false;
				}
			}

			ProductFlowpacketOrders productFlowpacketOrders = new ProductFlowpacketOrders();
			productFlowpacketOrders.setUuid(uuid);
			productFlowpacketOrders.setOrdersNo(ordersNo);
			productFlowpacketOrders.setOrderMobile(mobile);
			productFlowpacketOrders.setValidateCode(validCode);
			productFlowpacketOrders.setOriginIp(originIp);
			productFlowpacketOrders.setOriginUrl(originUrl);
			productFlowpacketOrders.setCreateTime(new Date());
			productFlowpacketOrders.setProductProvice("BJPH");
			productFlowpacketOrders.setCallbackInfo(productOrdersLog.getResponseResult());
			productFlowpacketOrders.setProductId(warePlanId);
			productFlowpacketOrders.setProductName(planName);

			if(StringUtils.isNotBlank(originUrl) && originUrl.indexOf("_") != -1) {
				String[] originUrls = originUrl.split("_"); 
				productFlowpacketOrders.setExtensionChannelType(originUrls[1]);
			}
			productFlowpacketOrders.setExtensionChannel(userName);

			if(flag) {
				// 办理成功
				productFlowpacketOrders.setOrdersStatus("1");
				productFlowpacketOrdersService.insertProductFlowpacketOrders(productFlowpacketOrders);

				// 记录每个账号的量级
				String keyNum = "num:" + userName;
				String numStr = RedisUtil.get(keyNum);
				int num = 0;
				if(StringUtils.isNotBlank(numStr) && !"null".equals(numStr)) {
					num = Integer.valueOf(numStr);
				}
				num = num+1;
				log.info("mobile: {}, num: {}, userName: {}", mobile, num, userName);
				RedisUtil.set(keyNum, num+"", 0);

				// 释放工号
				RedisUtil.delete("lock:" + userName);

				return AjaxResult.success(200, "恭喜您，" + planName + "办理成功");
			} else {
				// 办理失败
				productFlowpacketOrders.setOrdersStatus("2");
				productFlowpacketOrdersService.insertProductFlowpacketOrders(productFlowpacketOrders);

				String errorMsg = jsonObject.getString("errorMsg");
				if("-888".equals(reCode)) {
					return AjaxResult.error(211, resultInfo);
				}
				
				return AjaxResult.error(211, errorMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error(201, "接口异常，请稍后再试");
		}
	}

}
