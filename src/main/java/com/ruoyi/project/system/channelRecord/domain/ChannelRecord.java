package com.ruoyi.project.system.channelRecord.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 渠道记录表 channel_record
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
public class ChannelRecord extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 渠道记录表 */
	private Integer id;
	/**  */
    @Excel(name = "手机密文")
	private String mobile;
	/**  */
	private String mobileMask;
	/**  */
    @Excel(name = "渠道标识")
	private String channelAppid;
	/**  */
    @Excel(name = "导入时间")
	private Date createTime;
	/**  */
	private String responseBody;
	/**  */
	private String callbackBody;
	
	private String originIp;
	
    @Excel(name = "地址")
	private String originUrl;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setMobile(String mobile) 
	{
		this.mobile = mobile;
	}

	public String getMobile() 
	{
		return mobile;
	}
	public String getMobileMask() {
		return mobileMask;
	}

	public void setMobileMask(String mobileMask) {
		this.mobileMask = mobileMask;
	}

	public void setChannelAppid(String channelAppid) 
	{
		this.channelAppid = channelAppid;
	}

	public String getChannelAppid() 
	{
		return channelAppid;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setResponseBody(String responseBody) 
	{
		this.responseBody = responseBody;
	}

	public String getResponseBody() 
	{
		return responseBody;
	}

    public String getCallbackBody() {
		return callbackBody;
	}

	public void setCallbackBody(String callbackBody) {
		this.callbackBody = callbackBody;
	}

	public String getOriginIp() {
		return originIp;
	}

	public void setOriginIp(String originIp) {
		this.originIp = originIp;
	}

	public String getOriginUrl() {
		return originUrl;
	}

	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mobile", getMobile())
            .append("mobileMask", getMobileMask())
            .append("channelAppid", getChannelAppid())
            .append("createTime", getCreateTime())
            .append("responseBody", getResponseBody())
            .append("callbackBody", getCallbackBody())
            .append("originIp", getOriginIp())
            .append("originUrl", getOriginUrl())
            .toString();
    }
}
