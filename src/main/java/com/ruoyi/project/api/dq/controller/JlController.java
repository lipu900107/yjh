package com.ruoyi.project.api.dq.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.api.dq.util.JlUtils;
import com.ruoyi.project.common.util.ToolUtils;
import com.ruoyi.project.system.productFlowpacketOrders.domain.ProductFlowpacketOrders;
import com.ruoyi.project.system.productFlowpacketOrders.service.IProductFlowpacketOrdersService;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;
import com.ruoyi.project.system.productOrdersLog.service.IProductOrdersLogService;

/**
 * 吉林电渠接口
 * 
 */
@Controller
@RequestMapping("/api/dq/jl")
public class JlController extends BaseController
{
	private static final Logger log = LoggerFactory.getLogger(JlController.class);
	
	@Autowired
	private IProductFlowpacketOrdersService productFlowpacketOrdersService;
	
	@Autowired
	private IProductOrdersLogService productOrdersLogService;
	
    @PostMapping("/sendCode")
    @ResponseBody
    public AjaxResult sendCode(String mobile, String originUrl, HttpServletRequest request)
    {
    	log.warn("mobile: {}, origin: {}", mobile, originUrl);
    	
    	String originIp = ToolUtils.getRemoteAddr(request);
    	log.warn("originIp: {}", originIp);
    	if (StringUtils.contains(originIp, ",")) {
            String[] split = originIp.split(",");
            originIp = split[0];
        }
    	
    	if(StringUtils.isBlank(mobile)) {
    		return AjaxResult.error(201, "手机号不能为空，请重新输入");
    	}
    	
    	try {
			ProductOrdersLog productOrdersLog = JlUtils.reqCode(mobile, "01", "");
			productOrdersLog.setMobile(mobile);
			productOrdersLog.setOriginIp(originIp);
			productOrdersLog.setOriginUrl(originUrl);
			productOrdersLog.setUuid(ToolUtils.getUUID());
			productOrdersLogService.insertProductOrdersLog(productOrdersLog);
			
			String result = productOrdersLog.getResponseResult();
			if(StringUtils.isBlank(result)) {
				return AjaxResult.error(201, "接口异常，请稍后再试");
			}
			
			JSONObject jsonObject = JSONObject.parseObject(result);
			result = jsonObject.getString("result");
			if("1".equals(result)) {
				return AjaxResult.success(200, "发送成功");
			} else {
				String errdesc = jsonObject.getString("errdesc");
				return AjaxResult.error(201, errdesc);
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
    	log.warn("mobile: {}, originUrl: {}", mobile, originUrl);
    	
    	String originIp = ToolUtils.getRemoteAddr(request);
    	log.warn("originIp: {}", originIp);
    	if (StringUtils.contains(originIp, ",")) {
            String[] split = originIp.split(",");
            originIp = split[0];
        }
    	
    	if(StringUtils.isBlank(mobile)) {
    		return AjaxResult.error(201, "手机号不能为空，请重新输入");
    	}
    	
    	if(StringUtils.isBlank(validCode)) {
    		return AjaxResult.error(201, "验证码不能为空，请重新输入");
    	}

    	try {
    		// 验证输入验证码
			ProductOrdersLog productOrdersLog = JlUtils.validCode(mobile, "01", "", validCode);
			productOrdersLog.setMobile(mobile);
			productOrdersLog.setOriginIp(originIp);
			productOrdersLog.setOriginUrl(originUrl);
			productOrdersLog.setUuid(ToolUtils.getUUID());
			productOrdersLogService.insertProductOrdersLog(productOrdersLog);
			
			String result = productOrdersLog.getResponseResult();
			if(StringUtils.isBlank(result)) {
				return AjaxResult.error(201, "接口异常，请稍后再试");
			}
			
			JSONObject jsonObject = JSONObject.parseObject(result);
			result = jsonObject.getString("result");
			boolean flag = false;
			if("1".equals(result)) {
				flag = true;
			}
			
	    	String productId = request.getParameter("productId");
			String uuid = ToolUtils.getUUID();
	    	String ordersNo = uuid + DateUtils.dateTimeNow("yyyyMMddHHmmssSSS");
			
			if(flag) {	// 校验订单
				productOrdersLog = JlUtils.validHandle(mobile, productId, "A", "");
				productOrdersLog.setMobile(mobile);
				productOrdersLog.setOriginIp(originIp);
				productOrdersLog.setOriginUrl(originUrl);
				productOrdersLog.setUuid(ToolUtils.getUUID());
				productOrdersLogService.insertProductOrdersLog(productOrdersLog);
				
				result = productOrdersLog.getResponseResult();
				if(StringUtils.isBlank(result)) {
					return AjaxResult.error(201, "接口异常，请稍后再试");
				}
				jsonObject = JSONObject.parseObject(result);
				result = jsonObject.getString("result");
				if("1".equals(result)) {
					flag = true;
				} else {
					flag = false;
				}
			}
			
			if(flag) {	// 业务办理
				productOrdersLog = JlUtils.handleFlowpacket(mobile, ordersNo, productId, "A", "");
				productOrdersLog.setMobile(mobile);
				productOrdersLog.setOriginIp(originIp);
				productOrdersLog.setOriginUrl(originUrl);
				productOrdersLog.setUuid(ToolUtils.getUUID());
				productOrdersLogService.insertProductOrdersLog(productOrdersLog);
				
				result = productOrdersLog.getResponseResult();
				if(StringUtils.isBlank(result)) {
					return AjaxResult.error(201, "接口异常，请稍后再试");
				}
				jsonObject = JSONObject.parseObject(result);
				result = jsonObject.getString("result");
				if("1".equals(result)) {
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
	    	productFlowpacketOrders.setProductProvice("JL");
	    	productFlowpacketOrders.setCallbackInfo(productOrdersLog.getResponseResult());
	    	
	    	String productName = request.getParameter("productName");
	    	productFlowpacketOrders.setProductId(productId);
	    	productFlowpacketOrders.setProductName(productName);
	    	
	    	if(StringUtils.contains(originUrl, "_")) {
	    		String[] originUrls = originUrl.split("_"); 
	    		productFlowpacketOrders.setExtensionChannel(originUrls[0]);
	    		productFlowpacketOrders.setExtensionChannelType(originUrls[1]);
	    	}
	    	
	    	if(flag) {
				// 办理成功
		    	productFlowpacketOrders.setOrdersStatus("1");
		    	productFlowpacketOrdersService.insertProductFlowpacketOrders(productFlowpacketOrders);
		    	
				return AjaxResult.success(200, "恭喜您，" + productName.substring(0, productName.indexOf("(")) + "办理成功");
			} else {
				// 办理失败
		    	productFlowpacketOrders.setOrdersStatus("2");
		    	productFlowpacketOrdersService.insertProductFlowpacketOrders(productFlowpacketOrders);
		    	
				String errdesc = jsonObject.getString("errdesc");
				return AjaxResult.error(201, errdesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error(201, "接口异常，请稍后再试");
		}
    }
    
}
