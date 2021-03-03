package com.ruoyi.project.system.channelAuth.mapper;

import com.ruoyi.project.system.channelAuth.domain.ChannelAuth;
import java.util.List;	

/**
 * 渠道鉴权 数据层
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
public interface ChannelAuthMapper 
{
	/**
     * 查询渠道鉴权信息
     * 
     * @param id 渠道鉴权ID
     * @return 渠道鉴权信息
     */
	public ChannelAuth selectChannelAuthById(Integer id);
	
	/**
     * 查询渠道鉴权信息
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 渠道鉴权信息
     */
	public ChannelAuth selectChannelAuth(ChannelAuth channelAuth);
	
	/**
     * 查询渠道鉴权列表
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 渠道鉴权集合
     */
	public List<ChannelAuth> selectChannelAuthList(ChannelAuth channelAuth);
	
	/**
     * 新增渠道鉴权
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 结果
     */
	public int insertChannelAuth(ChannelAuth channelAuth);
	
	/**
     * 修改渠道鉴权
     * 
     * @param channelAuth 渠道鉴权信息
     * @return 结果
     */
	public int updateChannelAuth(ChannelAuth channelAuth);
	
	/**
     * 删除渠道鉴权
     * 
     * @param id 渠道鉴权ID
     * @return 结果
     */
	public int deleteChannelAuthById(Integer id);
	
	/**
     * 批量删除渠道鉴权
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteChannelAuthByIds(String[] ids);
	
}