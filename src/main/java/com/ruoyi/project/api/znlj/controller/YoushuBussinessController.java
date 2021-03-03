package com.ruoyi.project.api.znlj.controller;

import java.text.SimpleDateFormat;
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
import com.ruoyi.project.common.util.YsBussinessUtil;
import com.ruoyi.project.system.channelAuth.domain.ChannelAuth;
import com.ruoyi.project.system.channelAuth.service.IChannelAuthService;
import com.ruoyi.project.system.channelRecord.domain.ChannelRecord;
import com.ruoyi.project.system.channelRecord.domain.ChannelRecordDto;
import com.ruoyi.project.system.channelRecord.service.IChannelRecordService;
import com.ruoyi.project.system.channelRefererLog.domain.ChannelRefererLog;
import com.ruoyi.project.system.channelRefererLog.service.IChannelRefererLogService;
import com.thoughtworks.xstream.XStream;

@Controller
@RequestMapping("/znlj/ysBussniess")
@CrossOrigin
public class YoushuBussinessController {

	private static final Logger LOGGER = LogManager.getLogger(YoushuBussinessController.class);

	
    @Autowired(required=false)
    private XStream xStream;

	@RequestMapping(value = "/getCenterNo",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult getCenterNo(HttpServletRequest request, HttpServletResponse response) {
		String logId = String.valueOf(System.currentTimeMillis());
		
		String cookieId = request.getHeader("cookieId");

		YsBussinessUtil ysBussinessUtil = new YsBussinessUtil();
		String apId = ConstantConfig.ys_apid;
		String apKey = ConstantConfig.ys_apkey;
		String appId = ConstantConfig.ys_app_id;
		String appKey = ConstantConfig.ys_app_key;
		String clinentCode = ConstantConfig.client_code;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("apId", apId);
		paramMap.put("apKey", apKey);
		paramMap.put("appId", appId);
		paramMap.put("appKey", appKey);
		paramMap.put("clientCode",clinentCode);
		
		String seqRedis = RedisUtil.get("seq." + apId);
		String seq = "0";
		if(StringUtils.isNotBlank(seqRedis)){
			int seqint = Integer.parseInt(seqRedis);
			seqint = seqint + 1;
			seq = seqint + "";
		}
		RedisUtil.set("seq." + apId, seq, 3600 * 24);
		try {
			String keyRedis = RedisUtil.get("key." + apId);
			if(StringUtils.isNotBlank(keyRedis)){
				Map<String, Object> keyMap = ysBussinessUtil.getApplyKey(xStream, paramMap);
				String key = (String)keyMap.get("key");
				//paramMap.put("seq",seq);
				paramMap.put("key", key);
				RedisUtil.set("key." + apId, key, 3600 * 24);
			}
			/**
	         * 手机号码（11位长度的中国移动号码）
	         */
	        String phone = cookieId;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	        String regTime = sdf.format(new Date());
	        String servCode = "DZBQ-AA02";
			String requestXml = YsBussinessUtil.createXml(phone, regTime, servCode, paramMap);
			String responseXml = YsBussinessUtil.doPost(ConstantConfig.opendata_url, requestXml, paramMap);
			LOGGER.warn("获取中间号码返回结果：" + responseXml);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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

}
