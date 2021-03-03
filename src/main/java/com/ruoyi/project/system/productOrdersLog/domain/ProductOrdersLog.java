package com.ruoyi.project.system.productOrdersLog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 产品订单日志表 product_orders_log
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
public class ProductOrdersLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 主键id */
	private String uuid;
	/** 办理手机号 */
	private String mobile;
	/** 请求接口 */
	private String requestInterfaces;
	/** 请求参数 */
	private String requestParam;
	/** 响应结果 */
	private String responseResult;
	/** 请求ip */
	private String originIp;
	/** 来源 */
	private String originUrl;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 产品省份 */
	private String productProvice;
	
	public ProductOrdersLog() {}
	
	public ProductOrdersLog(String uuid, String mobile, String requestInterfaces, 
			String requestParam, String responseResult, String originIp, String originUrl, 
			String productProvice) {
		this.uuid = uuid;
		this.mobile = mobile;
		this.requestInterfaces = requestInterfaces;
		this.requestParam = requestParam;
		this.responseResult = responseResult;
		this.originIp = originIp;
		this.originUrl = originUrl;
		this.productProvice = productProvice;
	}

	public void setUuid(String uuid) 
	{
		this.uuid = uuid;
	}

	public String getUuid() 
	{
		return uuid;
	}
	public void setMobile(String mobile) 
	{
		this.mobile = mobile;
	}

	public String getMobile() 
	{
		return mobile;
	}
	public void setRequestInterfaces(String requestInterfaces) 
	{
		this.requestInterfaces = requestInterfaces;
	}

	public String getRequestInterfaces() 
	{
		return requestInterfaces;
	}
	public void setRequestParam(String requestParam) 
	{
		this.requestParam = requestParam;
	}

	public String getRequestParam() 
	{
		return requestParam;
	}
	public void setResponseResult(String responseResult) 
	{
		this.responseResult = responseResult;
	}

	public String getResponseResult() 
	{
		return responseResult;
	}
	public void setOriginIp(String originIp) 
	{
		this.originIp = originIp;
	}

	public String getOriginIp() 
	{
		return originIp;
	}
	public void setOriginUrl(String originUrl) 
	{
		this.originUrl = originUrl;
	}

	public String getOriginUrl() 
	{
		return originUrl;
	}
	public void setStartTime(Date startTime) 
	{
		this.startTime = startTime;
	}

	public Date getStartTime() 
	{
		return startTime;
	}
	public void setEndTime(Date endTime) 
	{
		this.endTime = endTime;
	}

	public Date getEndTime() 
	{
		return endTime;
	}
	public void setProductProvice(String productProvice) 
	{
		this.productProvice = productProvice;
	}

	public String getProductProvice() 
	{
		return productProvice;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uuid", getUuid())
            .append("mobile", getMobile())
            .append("requestInterfaces", getRequestInterfaces())
            .append("requestParam", getRequestParam())
            .append("responseResult", getResponseResult())
            .append("originIp", getOriginIp())
            .append("originUrl", getOriginUrl())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("productProvice", getProductProvice())
            .toString();
    }
}
