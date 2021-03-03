package com.ruoyi.project.system.channelRefererLog.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 渠道请求日志表 channel_referer_log
 * 
 * @author ruoyi
 * @date 2020-07-19
 */
public class ChannelRefererLog extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/**  */
    @Excel(name = "渠道标识")
	private String channelAppid;
	/**  */
	private String originIp;
	/**  */
    @Excel(name = "运营商")
	private String ipIsp;
	/**  */
	private String ipIspStr;
	/**  */
    @Excel(name = "地址")
	private String refererUrl;
	/**  */
	private Date createTime;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setChannelAppid(String channelAppid) 
	{
		this.channelAppid = channelAppid;
	}

	public String getChannelAppid() 
	{
		return channelAppid;
	}
	public void setOriginIp(String originIp) 
	{
		this.originIp = originIp;
	}

	public String getOriginIp() 
	{
		return originIp;
	}
	public void setIpIsp(String ipIsp) 
	{
		this.ipIsp = ipIsp;
	}

	public String getIpIsp() 
	{
		return ipIsp;
	}
	public void setIpIspStr(String ipIspStr) 
	{
		this.ipIspStr = ipIspStr;
	}

	public String getIpIspStr() 
	{
		return ipIspStr;
	}
	public void setRefererUrl(String refererUrl) 
	{
		this.refererUrl = refererUrl;
	}

	public String getRefererUrl() 
	{
		return refererUrl;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelAppid", getChannelAppid())
            .append("originIp", getOriginIp())
            .append("ipIsp", getIpIsp())
            .append("ipIspStr", getIpIspStr())
            .append("refererUrl", getRefererUrl())
            .append("createTime", getCreateTime())
            .toString();
    }
}
