package com.ruoyi.project.system.channelRefererLog.mapper;

import com.ruoyi.project.system.channelRefererLog.domain.ChannelRefererLog;
import java.util.List;	

/**
 * 渠道请求日志 数据层
 * 
 * @author ruoyi
 * @date 2020-07-19
 */
public interface ChannelRefererLogMapper 
{
	/**
     * 查询渠道请求日志信息
     * 
     * @param id 渠道请求日志ID
     * @return 渠道请求日志信息
     */
	public ChannelRefererLog selectChannelRefererLogById(Integer id);
	
	/**
     * 查询渠道请求日志列表
     * 
     * @param channelRefererLog 渠道请求日志信息
     * @return 渠道请求日志集合
     */
	public List<ChannelRefererLog> selectChannelRefererLogList(ChannelRefererLog channelRefererLog);
	
	/**
     * 新增渠道请求日志
     * 
     * @param channelRefererLog 渠道请求日志信息
     * @return 结果
     */
	public int insertChannelRefererLog(ChannelRefererLog channelRefererLog);
	
	/**
     * 修改渠道请求日志
     * 
     * @param channelRefererLog 渠道请求日志信息
     * @return 结果
     */
	public int updateChannelRefererLog(ChannelRefererLog channelRefererLog);
	
	/**
     * 删除渠道请求日志
     * 
     * @param id 渠道请求日志ID
     * @return 结果
     */
	public int deleteChannelRefererLogById(Integer id);
	
	/**
     * 批量删除渠道请求日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteChannelRefererLogByIds(String[] ids);
	
}