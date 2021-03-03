package com.ruoyi.project.system.channelAuth.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 渠道鉴权表 channel_auth
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
public class ChannelAuth extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 渠道鉴权表 */
	private Integer id;
	/**  */
	private String publicKey;
	/**  */
	private String privateKey;
	/**  */
	private String whiteIps;
	/**  */
	private String channelAppid;
	/**  */
	private String channelAppkey;
	/**  */
	private String channelName;
	/**  */
	private Date createTime;
	/**  */
	private Integer testNum;
	/**  */
	private String authStatus;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setPublicKey(String publicKey) 
	{
		this.publicKey = publicKey;
	}

	public String getPublicKey() 
	{
		return publicKey;
	}
	public void setPrivateKey(String privateKey) 
	{
		this.privateKey = privateKey;
	}

	public String getPrivateKey() 
	{
		return privateKey;
	}
	public void setWhiteIps(String whiteIps) 
	{
		this.whiteIps = whiteIps;
	}

	public String getWhiteIps() 
	{
		return whiteIps;
	}
	public void setChannelAppid(String channelAppid) 
	{
		this.channelAppid = channelAppid;
	}

	public String getChannelAppid() 
	{
		return channelAppid;
	}
	public void setChannelAppkey(String channelAppkey) 
	{
		this.channelAppkey = channelAppkey;
	}

	public String getChannelAppkey() 
	{
		return channelAppkey;
	}
	public void setChannelName(String channelName) 
	{
		this.channelName = channelName;
	}

	public String getChannelName() 
	{
		return channelName;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	
    public Integer getTestNum() {
		return testNum;
	}

	public void setTestNum(Integer testNum) {
		this.testNum = testNum;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("publicKey", getPublicKey())
            .append("privateKey", getPrivateKey())
            .append("whiteIps", getWhiteIps())
            .append("channelAppid", getChannelAppid())
            .append("channelAppkey", getChannelAppkey())
            .append("channelName", getChannelName())
            .append("createTime", getCreateTime())
            .append("testNum", getTestNum())
            .append("authStatus", getAuthStatus())
            .toString();
    }
}
