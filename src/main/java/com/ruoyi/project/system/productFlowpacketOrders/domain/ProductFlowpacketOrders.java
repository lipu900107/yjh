package com.ruoyi.project.system.productFlowpacketOrders.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 流量包产品订单表 product_flowpacket_orders
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
public class ProductFlowpacketOrders extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private String uuid;
	/** 订单号 */
	@Excel(name = "订单号")
	private String ordersNo;
	/** 订单手机号 */
	@Excel(name = "手机号")
	private String orderMobile;
	/** 订购验证码 */
	@Excel(name = "验证码")
	private String validateCode;
	/** 订单状态 */
	@Excel(name = "订单状态", readConverterExp = "1=成功,2=失败")
	private String ordersStatus;
	/** 订购时间 */
	@Excel(name = "订购时间")
	private Date createTime;
	/** 产品ID */
	@Excel(name = "产品编码")
	private String productId;
	/** 产品名称 */
	@Excel(name = "产品名称")
	private String productName;
	/** 产品省份 */
	@Excel(name = "产品省份")
	private String productProvice;
	/** 推广渠道 */
	@Excel(name = "所属工号")
	private String extensionChannel;
	/** 推广渠道类型 */
	private String extensionChannelType;
	/** 来源 */
	private String originUrl;
	/** ip */
	private String originIp;
	/** 回调结果 */
	private String callbackInfo;

	public void setUuid(String uuid) 
	{
		this.uuid = uuid;
	}

	public String getUuid() 
	{
		return uuid;
	}
	public void setOrdersNo(String ordersNo) 
	{
		this.ordersNo = ordersNo;
	}

	public String getOrdersNo() 
	{
		return ordersNo;
	}
	public void setOrderMobile(String orderMobile) 
	{
		this.orderMobile = orderMobile;
	}

	public String getOrderMobile() 
	{
		return orderMobile;
	}
	public void setValidateCode(String validateCode) 
	{
		this.validateCode = validateCode;
	}

	public String getValidateCode() 
	{
		return validateCode;
	}
	public void setOrdersStatus(String ordersStatus) 
	{
		this.ordersStatus = ordersStatus;
	}

	public String getOrdersStatus() 
	{
		return ordersStatus;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setProductId(String productId) 
	{
		this.productId = productId;
	}

	public String getProductId() 
	{
		return productId;
	}
	public void setProductName(String productName) 
	{
		this.productName = productName;
	}

	public String getProductName() 
	{
		return productName;
	}
	public void setProductProvice(String productProvice) 
	{
		this.productProvice = productProvice;
	}

	public String getProductProvice() 
	{
		return productProvice;
	}
	public void setExtensionChannel(String extensionChannel) 
	{
		this.extensionChannel = extensionChannel;
	}

	public String getExtensionChannel() 
	{
		return extensionChannel;
	}
	public void setExtensionChannelType(String extensionChannelType) 
	{
		this.extensionChannelType = extensionChannelType;
	}

	public String getExtensionChannelType() 
	{
		return extensionChannelType;
	}
	public void setOriginUrl(String originUrl) 
	{
		this.originUrl = originUrl;
	}

	public String getOriginUrl() 
	{
		return originUrl;
	}
	public void setOriginIp(String originIp) 
	{
		this.originIp = originIp;
	}

	public String getOriginIp() 
	{
		return originIp;
	}
	public void setCallbackInfo(String callbackInfo) 
	{
		this.callbackInfo = callbackInfo;
	}

	public String getCallbackInfo() 
	{
		return callbackInfo;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uuid", getUuid())
            .append("ordersNo", getOrdersNo())
            .append("orderMobile", getOrderMobile())
            .append("validateCode", getValidateCode())
            .append("ordersStatus", getOrdersStatus())
            .append("createTime", getCreateTime())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productProvice", getProductProvice())
            .append("extensionChannel", getExtensionChannel())
            .append("extensionChannelType", getExtensionChannelType())
            .append("originUrl", getOriginUrl())
            .append("originIp", getOriginIp())
            .append("callbackInfo", getCallbackInfo())
            .toString();
    }
}
