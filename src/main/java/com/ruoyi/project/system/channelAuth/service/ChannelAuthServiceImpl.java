package com.ruoyi.project.system.channelAuth.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.channelAuth.mapper.ChannelAuthMapper;
import com.ruoyi.project.system.channelAuth.domain.ChannelAuth;
import com.ruoyi.project.system.channelAuth.service.IChannelAuthService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 渠道鉴权 服务层实现
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
@Service
public class ChannelAuthServiceImpl implements IChannelAuthService 
{
	@Autowired
	private ChannelAuthMapper channelAuthMapper;

	/**
     * 查询渠道鉴权信息
     * 
     * @param id 渠道鉴权ID
     * @return 渠道鉴权信息
     */
    @Override
	public ChannelAuth selectChannelAuthById(Integer id)
	{
	    return channelAuthMapper.selectChannelAuthById(id);
	}
    
    /**
     * 查询渠道鉴权信息
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 渠道鉴权信息
     */
    @Override
    public ChannelAuth selectChannelAuth(ChannelAuth channelAuth)
	{
	    return channelAuthMapper.selectChannelAuth(channelAuth);
	}
	
	/**
     * 查询渠道鉴权列表
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 渠道鉴权集合
     */
	@Override
	public List<ChannelAuth> selectChannelAuthList(ChannelAuth channelAuth)
	{
	    return channelAuthMapper.selectChannelAuthList(channelAuth);
	}
	
    /**
     * 新增渠道鉴权
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 结果
     */
	@Override
	public int insertChannelAuth(ChannelAuth channelAuth)
	{
	    return channelAuthMapper.insertChannelAuth(channelAuth);
	}
	
	/**
     * 修改渠道鉴权
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 结果
     */
	@Override
	public int updateChannelAuth(ChannelAuth channelAuth)
	{
	    return channelAuthMapper.updateChannelAuth(channelAuth);
	}

	/**
     * 删除渠道鉴权对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteChannelAuthByIds(String ids)
	{
		return channelAuthMapper.deleteChannelAuthByIds(Convert.toStrArray(ids));
	}
	
}
