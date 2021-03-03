package com.ruoyi.project.api.znlj.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.api.znlj.util.ConstantConfig;
import com.ruoyi.project.common.util.AESUtil;
import com.ruoyi.project.common.util.AesUtils;
import com.ruoyi.project.common.util.RSAUtils;
import com.ruoyi.project.common.util.RedisUtil;
import com.ruoyi.project.common.util.ReqValidate;
import com.ruoyi.project.common.util.TokenValidate;
import com.ruoyi.project.system.channelAuth.domain.ChannelAuth;
import com.ruoyi.project.system.channelAuth.service.IChannelAuthService;
import com.ruoyi.project.system.channelRecord.domain.ChannelRecord;
import com.ruoyi.project.system.channelRecord.domain.ChannelRecordDto;
import com.ruoyi.project.system.channelRecord.service.IChannelRecordService;
import com.ruoyi.project.system.channelRefererLog.domain.ChannelRefererLog;
import com.ruoyi.project.system.channelRefererLog.service.IChannelRefererLogService;

@Controller
@RequestMapping("/znlj/bussniess")
@CrossOrigin
public class BussinessController {

	private static final Logger LOGGER = LogManager.getLogger(BussinessController.class);

	@Autowired
	private IChannelAuthService channelAuthService;

	@Autowired
	private IChannelRecordService channelRecordService;

	@Autowired
	private IChannelRefererLogService channelRefererLogService;

	@RequestMapping(value = "/getSign",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getSign(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());

		// 从数据库里面验证appId是否存在，同时获取IP白名单
		String appId = request.getHeader("appid");
		if(StringUtils.isEmpty(appId)) {
			return AjaxResult.success(211, "渠道Id为空！");
		}

		// 校验
		if(!appId.equals(ConstantConfig.znlj_appid)) {
			ChannelAuth channelAuth = new ChannelAuth();
			channelAuth.setChannelAppid(appId);
			channelAuth.setAuthStatus("0");
			channelAuth = channelAuthService.selectChannelAuth(channelAuth);
			if(channelAuth == null) {
				return AjaxResult.success(211, "渠道不存在！");
			}

			// 判断测试量级
			Integer testNum = channelAuth.getTestNum();
			String key = "num:" + appId;
			int num = StringUtils.isBlank(RedisUtil.get(key))?0:Integer.valueOf(RedisUtil.get(key)).intValue();
			if(testNum == null)num = 10000000;
			if(testNum > 0 && testNum < num) {
				return AjaxResult.success(211, "达到测试上限，请联系商务！");
			}

			/*String accessOrigins = channelAuth.getWhiteIps();
			if(StringUtils.isEmpty(accessOrigins)) {
				return AjaxResult.success(211, "渠道没有报备IP！");
			}
			String origin = IpUtils.getIpAddr(request);
			LOGGER.info("{}|referer={}|origin={}", logId, null, origin);
			if(!ReqValidate.validateUrl(origin, accessOrigins, logId)){
				return AjaxResult.success(211, "无效IP！");
			}*/
		}

		// 判断该渠道是否重复下单
		String originIp = IpUtils.getIpAddr(request);
		LOGGER.info("getSign|{}|appId={}|originIp={}", logId, appId, originIp);
		ChannelRecord channelRecord = new ChannelRecord();
		channelRecord.setChannelAppid(appId);
		channelRecord.setOriginIp(originIp);
		List<ChannelRecord> list = channelRecordService.selectChannelRecordList(channelRecord);
		if(!list.isEmpty() && list != null && list.size() > 0) {
			LOGGER.info("getSign|{}|appId={}|originIp={}|size{}", logId, appId, originIp,list.size());
			return AjaxResult.success(211, "渠道手机号已存在！");
		}

		JSONObject respJson = new JSONObject();
		try {
			String sign = request.getParameter("sign");
			if (StringUtils.isEmpty(sign)){
				return AjaxResult.success(211, "签名错误");
			}

			sign = RSAUtils.sign(sign.getBytes("UTF-8"), ConstantConfig.znlj_rsa_privatekey);
			LOGGER.info("{}|sign={}", logId, sign);

			respJson.put("sign", sign);
			return AjaxResult.success(200, "发送成功", respJson);
		} catch (Exception e) {
			LOGGER.error("{}|Error=", logId, e);
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/getUserInfo",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getUserInfo(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		JSONObject reqJson = new JSONObject();
		JSONObject respJson = new JSONObject();
		try {

			String appId = request.getHeader("appid");
			String aOld = request.getHeader("aOld");
			
			if(StringUtils.isEmpty(appId)) {
				return AjaxResult.success(211, "渠道Id为空！");
			}
			String originIp = IpUtils.getIpAddr(request);

			// 校验
			ChannelAuth channelAuth = null;
			if(!appId.equals(ConstantConfig.znlj_appid)) {
				channelAuth = new ChannelAuth();
				channelAuth.setChannelAppid(appId);
				channelAuth = channelAuthService.selectChannelAuth(channelAuth);
				if(channelAuth == null) {
					return AjaxResult.success(211, "渠道不存在！");
				}

				/*String accessOrigins = channelAuth.getWhiteIps();
				if(StringUtils.isEmpty(accessOrigins)) {
					return AjaxResult.success(211, "渠道没有报备IP！");
				}
				String originIp = IpUtils.getIpAddr(request);
				LOGGER.info("{}|referer={}|origin={}", logId, null, origin);
				if(!ReqValidate.validateUrl(origin, accessOrigins, logId)){
					return AjaxResult.success(211, "无效IP！");
				}*/
			}

			/*String sign = "";
			if(!appId.equals(ConstantConfig.znlj_appid)) {
				sign = request.getHeader("sign");
				if(StringUtils.isEmpty(sign)) {
					return AjaxResult.success(211, "签名为空！");
				}
			}*/

			// 从数据库里面验证appId是否存在，同时获取私钥
			// String pubKey = channelAuth != null?channelAuth.getPublicKey():ConstantConfig.znlj_rsa_publickey;

			String apptype = request.getParameter("apptype");
			String token = request.getParameter("token");
			String userInfomation = request.getParameter("userInfomation");
			LOGGER.info("{}|appId={}|apptype={}|token={}|userInformation={}|Old={}", logId, appId, apptype, token, userInfomation,aOld);

			if(!StringUtils.isEmpty(apptype)) {
				apptype = "5";
			} 
			if(StringUtils.isEmpty(token)) {
				return AjaxResult.success(211, "Token不能为空");
			}
			if(StringUtils.isEmpty(userInfomation)){
				return AjaxResult.success(211, "userInformation不能为空");
			}

			// 把apptype, token, userInfomation进行公钥加密，对比Sign是否一致
			/*if(!appId.equals(ConstantConfig.znlj_appid)) {
				JSONObject bodyJson = new JSONObject();
				bodyJson.put("apptype", apptype);
				bodyJson.put("token", token);
				bodyJson.put("userInformation", userInfomation);

				String valSign = RSAUtils.encryptedData(bodyJson.toJSONString(), pubKey);
				LOGGER.info("{}|valSign={}|sign={}", logId, valSign, sign);
				if(!sign.equals(valSign)) {
					return AjaxResult.success(211, "签名错误");
				}
			}*/

			//生成新的请求报文
			String createSign = TokenValidate.createSign(apptype, token, userInfomation);
			String resResult = TokenValidate.postMethod(ConstantConfig.tokenValidateUrl, createSign);
			// {"header": {"inresponseto": "1540465642944","appId": "000043","resultcode": "103000","systemtime": "20181121171719453","version": "1.2","resultdesc": "成功"},"body": {"usessionid": "","msisdnmask": "136****3286","passid": "1453767527","exresparams":"uid=sid493f88aa049c47e5d864fe20ee3d7ef3",msisdn": "Ag0s+dnXycKAAV44sTqj4w=="}}
			LOGGER.info("{}|appId={}|createSign={}|resResult={}|originIp={}|aOld={}", logId, appId, createSign, resResult, originIp, aOld);

			reqJson = JSONObject.parseObject(resResult);
			JSONObject headerJson = JSONObject.parseObject(reqJson.getString("header"));
			String resultcode = headerJson.getString("resultcode");
			if(!"103000".equals(resultcode)) {
				String resultDesc = headerJson.getString("resultdesc");
				return AjaxResult.success(211, resultDesc);
			}
			JSONObject bodyJson = JSONObject.parseObject(reqJson.getString("body"));
			String msisdn = bodyJson.getString("msisdn");
			String msisdnmask = bodyJson.getString("msisdnmask");

			// 获取的信息存储到数据库里面
			ChannelRecord channelRecord = new ChannelRecord();
			channelRecord.setMobile(msisdn);
			channelRecord.setMobileMask(msisdnmask);
			channelRecord.setChannelAppid(appId);
			channelRecord.setCreateTime(new Date());
			channelRecord.setResponseBody(resResult);
			channelRecord.setOriginIp(originIp);
			channelRecord.setCallbackBody(aOld);
			//if("ZNLJ_WY".equals(appId)) {
			channelRecord.setOriginUrl(RedisUtil.get(originIp));
			//}
			channelRecordService.insertChannelRecord(channelRecord);

			String key = "IsLogin:" + originIp;
			RedisUtil.set(key, "1", 60*60*24*3);	// 有效期3天

			key = "num:" + appId;
			RedisUtil.incr(key, 1L);	// 使用量级
			LOGGER.info("{}|appId={}|aOld={}", logId, appId, aOld);
			if(StringUtils.isNotBlank(aOld)) {	// 实时推送数据
				String aesPwd = channelAuth != null && !StringUtils.isEmpty(channelAuth.getPrivateKey())?channelAuth.getPrivateKey():ConstantConfig.AES_PWD; 
				String aesMobile = AesUtils.encryptor(AESUtil.DeCodeMobileAES(msisdn, ConstantConfig.znlj_appkey), aesPwd);
				LOGGER.info("{}|appId={}|aOld={}|aesPwd={}|aesMobile={}", logId, appId, aOld,aesPwd,aesMobile);
				// 相当于把aOld值和aesMobile传过去就可以了
				String url = "https://m.tuiabq.com/user/getMobile";
		        url = "https://m.7hotest.com/user/getMobile";
		        String param = "orderId=" + aOld + "&mobile=" + aesMobile + "&timestamp=" + System.currentTimeMillis();
		        
		        // {"success":true,"errorCode":null,"desc":null,"data":false}
		        String callback = HttpUtils.sendGet(url, param);
		        LOGGER.info("{}|appId={}|mobile={}|callback={}|originIp={}|aOld={}", logId, appId, aesMobile, callback, originIp, aOld);
			}

			respJson.put("mobile", msisdnmask);
			return AjaxResult.success(200, "成功", respJson);
		} catch (Exception e) {
			LOGGER.error("{}|Error=", logId, e);
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/sendAuthMsg",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult sendAuthMsg(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		JSONObject reqJson = new JSONObject();
		try {

			String appId = request.getHeader("appid");
			if(StringUtils.isEmpty(appId)) {
				return AjaxResult.success(211, "渠道Id为空！");
			}

			// 校验
			ChannelAuth channelAuth = null;
			if(!appId.equals(ConstantConfig.znlj_appid)) {
				channelAuth = new ChannelAuth();
				channelAuth.setChannelAppid(appId);
				channelAuth = channelAuthService.selectChannelAuth(channelAuth);
				if(channelAuth == null) {
					return AjaxResult.success(211, "渠道不存在！");
				}

				String accessOrigins = channelAuth.getWhiteIps();
				if(StringUtils.isEmpty(accessOrigins)) {
					return AjaxResult.success(211, "渠道没有报备IP！");
				}
				String origin = IpUtils.getIpAddr(request);
				LOGGER.info("{}|referer={}|origin={}", logId, null, origin);
				if(!ReqValidate.validateUrl(origin, accessOrigins, logId)){
					return AjaxResult.success(211, "无效IP！");
				}
			}

			String sign = "";
			if(!appId.equals(ConstantConfig.znlj_appid)) {
				sign = request.getHeader("sign");
				if(StringUtils.isEmpty(sign)) {
					return AjaxResult.success(211, "签名为空！");
				}
			}

			// 从数据库里面验证appId是否存在,同时获取公钥私钥等配置信息
			String pubKey = channelAuth != null?channelAuth.getPublicKey():ConstantConfig.znlj_rsa_publickey;
			String priKey = channelAuth != null?channelAuth.getPrivateKey():ConstantConfig.znlj_rsa_privatekey;

			String mobile = request.getParameter("mobile");
			LOGGER.info("{}|mobile={}|sign={}", logId, mobile, sign);
			if(StringUtils.isEmpty(mobile)) {
				return AjaxResult.success(211, "手机号不能为空");
			}

			// 把mobile进行公钥加密，对比Sign是否一致
			if(!appId.equals(ConstantConfig.znlj_appid)) {
				JSONObject bodyJson = new JSONObject();
				bodyJson.put("mobile", mobile);

				String valSign = RSAUtils.encryptedData(bodyJson.toJSONString(), pubKey);
				LOGGER.info("{}|valSign={}|sign={}", logId, valSign, sign);
				if(!sign.equals(valSign)) {
					return AjaxResult.success(211, "签名错误");
				}
			}

			String mobileDecrypt = RSAUtils.decryptData(mobile, priKey);
			if(!ReqValidate.isMobile(mobileDecrypt)) {
				return AjaxResult.success(211, "手机号格式错误");
			}

			String parameter = TokenValidate.requestToken(null).toJSONString();
			LOGGER.info("{}|parameter={}", logId, parameter);
			String resResult = TokenValidate.postMethod(ConstantConfig.accessTokenUrl, parameter);
			LOGGER.info("{}|parameter={}|resResult={}", logId, parameter, resResult);

			reqJson = JSONObject.parseObject(resResult);

			String accessToken = reqJson.getString("accessToken");
			String refreshToken = reqJson.getString("refreshToken");
			if(StringUtils.isEmpty(accessToken)) {
				return AjaxResult.success(211, "获取Token为空");
			}
			if(StringUtils.isEmpty(refreshToken)) {
				return AjaxResult.success(211, "获取Token为空");
			}

			parameter = TokenValidate.requestSendAuthMsg(accessToken, mobileDecrypt, ConstantConfig.templateId, null, ConstantConfig.callbackUrl).toJSONString();
			LOGGER.info("{}|parameter={}", logId, parameter);
			resResult = TokenValidate.postMethod(ConstantConfig.sendAuthMsgUrl, parameter);
			LOGGER.info("{}|parameter={}|resResult={}", logId, parameter, resResult);


			// 获取的信息存储到数据库里面
			ChannelRecord channelRecord = new ChannelRecord();
			channelRecord.setMobile(AESUtil.aesEncrypt(mobile, ConstantConfig.znlj_appkey));

			String msisdnmask = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
			channelRecord.setMobileMask(msisdnmask);

			channelRecord.setChannelAppid(appId);
			channelRecord.setCreateTime(new Date());
			channelRecord.setResponseBody(resResult);
			channelRecordService.insertChannelRecord(channelRecord);

			return AjaxResult.success(200, "成功");
		} catch (Exception e) {
			LOGGER.error("{}|Error=", logId, e);
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		String phoneSendStat = request.getParameter("phoneSendStat");
		String result = request.getParameter("result");
		String isPaid = request.getParameter("isPaid");
		String resultDes = request.getParameter("resultDes");
		String taskId = request.getParameter("taskId");
		String taskStartTime = request.getParameter("taskStartTime");

		JSONObject respJson = new JSONObject();
		respJson.put("phoneSendStat", phoneSendStat);
		respJson.put("result", result);
		respJson.put("isPaid", isPaid);
		respJson.put("resultDes", resultDes);
		respJson.put("taskId", taskId);
		respJson.put("taskStartTime", taskStartTime);

		JSONObject mobileInfo = JSONObject.parseObject(phoneSendStat);
		String mobile = mobileInfo.getString("phone");

		// 根据手机号把callback内容存到数据库里面，同时转化掩码存放到缓存中，时效30分钟，可通过taskId进行匹配
		mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

		ChannelRecord channelRecord = new ChannelRecord();
		channelRecord.setMobileMask(mobile);
		channelRecord = channelRecordService.selectChannelRecord(channelRecord);
		if(channelRecord == null) {
			LOGGER.info("{}此手机号不存在", mobile);
			return;
		}

		channelRecord.setCallbackBody(respJson.toJSONString());
		channelRecordService.updateChannelRecord(channelRecord);
	}

	@RequestMapping(value = "/getAuthMobile",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getAuthMobile(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		try {

			String appId = request.getHeader("appid");
			if(StringUtils.isEmpty(appId)) {
				return AjaxResult.success(211, "渠道Id为空！");
			}

			// 校验
			if(!appId.equals(ConstantConfig.znlj_appid)) {
				ChannelAuth channelAuth = new ChannelAuth();
				channelAuth.setChannelAppid(appId);
				channelAuth = channelAuthService.selectChannelAuth(channelAuth);
				if(channelAuth == null) {
					return AjaxResult.success(211, "渠道不存在！");
				}

				String accessOrigins = channelAuth.getWhiteIps();
				if(StringUtils.isEmpty(accessOrigins)) {
					return AjaxResult.success(211, "渠道没有报备IP！");
				}
				String origin = IpUtils.getIpAddr(request);
				LOGGER.info("{}|referer={}|origin={}", logId, null, origin);
				if(!ReqValidate.validateUrl(origin, accessOrigins, logId)){
					return AjaxResult.success(211, "无效IP！");
				}
			}

			// 从缓存中获取手机号，通过taskId进行匹配
			String mobile = "";

			JSONObject respJson = new JSONObject();
			respJson.put("mobile", mobile);
			return AjaxResult.success(200, "成功", respJson);
		} catch (Exception e) {
			LOGGER.error("{}|Error=", logId, e);
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/getReqLog",method=RequestMethod.POST)
	@ResponseBody
	public void getReqLog(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());

		// 从数据库里面验证appId是否存在，同时获取IP白名单
		String appId = request.getHeader("appid");
		String logStr = request.getParameter("logStr");
		String originIp = IpUtils.getIpAddr(request);
		LOGGER.info("{}|appId={}|originIp={}|logStr={}", logId, appId, originIp, logStr);
	}

	@RequestMapping(value = "/getReqConfig",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getReqConfig(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		String flag = "fail";

		try {
			String originIp = IpUtils.getIpAddr(request);

			StringBuffer sb = new StringBuffer();
			if(StringUtils.isEmpty(originIp)) return AjaxResult.success(211, "IP为空");;

			String[] ips = originIp.replaceAll(" ", "").split(",");
			for (int i = 0; i < ips.length; i++) {
				String ip = ips[i];
				String locationIsp = IpUtils.getIpIsp(ip);
				if(StringUtils.isEmpty(locationIsp)) continue;
				sb.append(locationIsp).append(",");
			}

			String ispStr = "无", ipIspStr = "";
			if(sb.length() > 0) {
				ipIspStr = sb.toString().substring(0, sb.toString().lastIndexOf(","));
			}

			if(ipIspStr.indexOf(",") == -1 && ipIspStr.indexOf("移动") > 0) {
				ispStr = "移动";
				flag = "success";
			} else if(ipIspStr.indexOf(",") == -1 && ipIspStr.indexOf("联通") > 0) {
				ispStr = "联通";
			} else if(ipIspStr.indexOf(",") == -1 && ipIspStr.indexOf("电信") > 0) {
				ispStr = "电信";
			} else if(ipIspStr.indexOf("局域网") > 0) {
				ispStr = "局域网";
			}

			// 从数据库里面验证appId是否存在，同时获取IP白名单
			String appId = request.getHeader("appid");
			String refererUrl = request.getHeader("refererUrl");

			if(StringUtils.isNotEmpty(refererUrl) && refererUrl.indexOf("yjh.mmnum.com") == -1) {
				ChannelRefererLog channelRefererLog = new ChannelRefererLog();
				channelRefererLog.setChannelAppid(appId);
				channelRefererLog.setOriginIp(originIp);
				channelRefererLog.setIpIsp(ispStr);
				channelRefererLog.setIpIspStr(ipIspStr);
				channelRefererLog.setRefererUrl(refererUrl);
				channelRefererLog.setCreateTime(new Date());

				RedisUtil.set(originIp, refererUrl, 60);

				channelRefererLogService.insertChannelRefererLog(channelRefererLog);
			}

			LOGGER.info("{}|appId={}|ispStr={}|ipIspStr={}|originIp={}", logId, appId, ispStr, ipIspStr, originIp);
			LOGGER.info("{}|appId={}|refererUrl={}", logId, appId, refererUrl);

			JSONObject respJson = new JSONObject();
			respJson.put("flag", flag);
			return AjaxResult.success(200, "成功", respJson);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/getPrize",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getPrize(HttpServletRequest request, HttpServletResponse response) {
		try {
			String originIp = IpUtils.getIpAddr(request);

			String nextDateStr = DateUtils.getNextDate();
			Date startTime = new Date();
			Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, nextDateStr+" 00:00:00");
			int second = DateUtils.getDistanceSecondTime(startTime, endTime);


			/**
			 * 判断是否登陆过
			 */
			String key = "IsLogin:" + originIp;
			String islogin = RedisUtil.get(key);	// 1是已登录；0是未登录
			if(StringUtils.isEmpty(islogin)) {
				islogin = "0";
			}

			int prizeTotalNum = 1;
			key = "Prize:" + originIp;
			if("1".equals(islogin)) {
				String prizeNum = RedisUtil.get(key);
				if(StringUtils.isEmpty(prizeNum)) {
					RedisUtil.set(key, "1", second); 
				} else {
					prizeTotalNum = 0;
				}
			}

			JSONObject respJson = new JSONObject();
			respJson.put("desc", "很遗憾，您未中奖，谢谢参与！");
			respJson.put("num", prizeTotalNum);
			respJson.put("islogin", islogin);
			return AjaxResult.success(200, "成功", respJson);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/getAllInfo",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getAllInfo(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		
		String appId = request.getHeader("appid");
		if(StringUtils.isEmpty(appId)) {
			return AjaxResult.success(211, "渠道Id为空！");
		}

		try {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
				return AjaxResult.success(211, "开始时间和结束时间为空！");
			}
			
			/*String key = "PWD:" + appId;
			String aesPwd = RedisUtil.get(key);
			if(StringUtils.isBlank(aesPwd)) {*/
				ChannelAuth channelAuth = new ChannelAuth();
				channelAuth.setChannelAppid(appId);
				channelAuth = channelAuthService.selectChannelAuth(channelAuth);
				if(channelAuth == null) {
					return AjaxResult.success(211, "渠道不存在！");
				}
				
				//判断IP白名单
				/*String originIp = IpUtils.getIpAddr(request);
				if(StringUtils.isBlank(originIp)) {
					return AjaxResult.success(211, "请求IP为空！");
				}
				if(!channelAuth.getWhiteIps().contains(originIp)) {
					return AjaxResult.success(211, "IP白名单错误！");
				}*/
				
				String aesPwd = channelAuth != null && !StringUtils.isEmpty(channelAuth.getPrivateKey())?channelAuth.getPrivateKey():ConstantConfig.AES_PWD;
				// RedisUtil.set(key, aesPwd);
			/*}*/

			ChannelRecord channelRecord = new ChannelRecord();
			channelRecord.setChannelAppid(appId);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("beginTime", startDate);
			params.put("endTime", endDate);
			channelRecord.setParams(params);

			List<ChannelRecordDto> list = new ArrayList<ChannelRecordDto>();
			List<ChannelRecord> channelRecords = channelRecordService.selectChannelRecordList(channelRecord);
			
			for (ChannelRecord cr : channelRecords) {
				ChannelRecordDto crs = new ChannelRecordDto();
				if(!StringUtils.isEmpty(appId) && (appId.startsWith("ZXXL_TJ0619"))) {
					if(StringUtils.isEmpty(cr.getOriginIp()) && "自动导入".equals(cr.getResponseBody())) {
						crs.setMobile(AesUtils.aesEncrypt(AESUtil.aesDecrypt(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					} else {
						crs.setMobile(AesUtils.aesEncrypt(AESUtil.DeCodeMobileAES(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					}
				} else {
					if(StringUtils.isEmpty(cr.getOriginIp()) && "自动导入".equals(cr.getResponseBody())) {
						crs.setMobile(AesUtils.encryptor(AESUtil.aesDecrypt(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					} else {
						crs.setMobile(AesUtils.encryptor(AESUtil.DeCodeMobileAES(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					}
				}
			}

			JSONObject respJson = new JSONObject();
			respJson.put("info", JSON.parse(JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)));
			return AjaxResult.success(200, "发送成功", respJson);
		} catch (Exception e) {
			LOGGER.error("{}|Error=", logId, e);
			return AjaxResult.success(201, "系统错误");
		}
	}

	@RequestMapping(value = "/getInfo",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getInfo(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		
		String appId = request.getHeader("appid");
		if(StringUtils.isEmpty(appId)) {
			return AjaxResult.success(211, "渠道Id为空！");
		}

		try {
			String beforeDate = DateUtils.getBeforeDate();
			String startDate = beforeDate + " 00:00:00";
			String endDate = beforeDate + " 23:59:59";
			
			/*String key = "PWD:" + appId;
			String aesPwd = RedisUtil.get(key);
			if(StringUtils.isBlank(aesPwd)) {*/
				ChannelAuth channelAuth = new ChannelAuth();
				channelAuth.setChannelAppid(appId);
				channelAuth = channelAuthService.selectChannelAuth(channelAuth);
				if(channelAuth == null) {
					return AjaxResult.success(211, "渠道不存在！");
				}
				
				//判断IP白名单
				/*String originIp = IpUtils.getIpAddr(request);
				if(StringUtils.isBlank(originIp)) {
					return AjaxResult.success(211, "请求IP为空！");
				}
				if(!channelAuth.getWhiteIps().contains(originIp)) {
					return AjaxResult.success(211, "IP白名单错误！");
				}*/
				
				String aesPwd = channelAuth != null && !StringUtils.isEmpty(channelAuth.getPrivateKey())?channelAuth.getPrivateKey():ConstantConfig.AES_PWD;
				// RedisUtil.set(key, aesPwd);
			/*}*/

			ChannelRecord channelRecord = new ChannelRecord();
			channelRecord.setChannelAppid(appId);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("beginTime", startDate);
			params.put("endTime", endDate);
			channelRecord.setParams(params);

			List<ChannelRecordDto> list = new ArrayList<ChannelRecordDto>();
			List<ChannelRecord> channelRecords = channelRecordService.selectChannelRecordList(channelRecord);
			
			for (ChannelRecord cr : channelRecords) {
				ChannelRecordDto crs = new ChannelRecordDto();
				if(!StringUtils.isEmpty(appId) && (appId.startsWith("ZXXL_TJ0619"))) {
					if(StringUtils.isEmpty(cr.getOriginIp()) && "自动导入".equals(cr.getResponseBody())) {
						crs.setMobile(AesUtils.aesEncrypt(AESUtil.aesDecrypt(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					} else {
						crs.setMobile(AesUtils.aesEncrypt(AESUtil.DeCodeMobileAES(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					}
				} else {
					if(StringUtils.isEmpty(cr.getOriginIp()) && "自动导入".equals(cr.getResponseBody())) {
						crs.setMobile(AesUtils.encryptor(AESUtil.aesDecrypt(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					} else {
						crs.setMobile(AesUtils.encryptor(AESUtil.DeCodeMobileAES(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						crs.setChannelAppid(appId);
						crs.setOriginUrl(cr.getOriginUrl());
						crs.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", cr.getCreateTime()));
						crs.setId(cr.getId());
						list.add(crs);
					}
				}
			}

			JSONObject respJson = new JSONObject();
			respJson.put("info", JSON.parse(JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)));
			return AjaxResult.success(200, "发送成功", respJson);
		} catch (Exception e) {
			LOGGER.error("{}|Error=", logId, e);
			return AjaxResult.success(201, "系统错误");
		}
	}
	
	
	public static void main(String[] args) {
		/*String url = "https://m.tuiabq.com/user/getMobile";
        url = "https://m.7hotest.com/user/getMobile";
        String param = "orderId=111&mobile=234567&timestamp=" + System.currentTimeMillis();
        
        // {"success":true,"errorCode":null,"desc":null,"data":false}
        String callback = HttpUtils.sendGet(url, param);
        System.out.println(callback);*/
		
		String aesPwd = ConstantConfig.AES_PWD; 
		try {
			String a = AesUtils.encryptor("15110089495", ConstantConfig.znlj_appkey);
			String s = AesUtils.decrypt(a, ConstantConfig.znlj_appkey);
			System.out.println(a);
			System.out.println(s);
			//String aesMobile = AesUtils.encryptor(a, aesPwd);
			//System.out.println(aesMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
