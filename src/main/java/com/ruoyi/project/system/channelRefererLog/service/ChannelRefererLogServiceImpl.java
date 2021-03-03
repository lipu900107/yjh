package com.ruoyi.project.system.channelRefererLog.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.channelRefererLog.mapper.ChannelRefererLogMapper;
import com.ruoyi.project.system.channelRefererLog.domain.ChannelRefererLog;
import com.ruoyi.project.system.channelRefererLog.service.IChannelRefererLogService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 渠道请求日志 服务层实现
 * 
 * @author ruoyi
 * @date 2020-07-19
 */
@Service
public class ChannelRefererLogServiceImpl implements IChannelRefererLogService 
{
	@Autowired
	private ChannelRefererLogMapper channelRefererLogMapper;

	/**
     * 查询渠道请求日志信息
     * 
     * @param id 渠道请求日志ID
     * @return 渠道请求日志信息
     */
    @Override
	public ChannelRefererLog selectChannelRefererLogById(Integer id)
	{
	    return channelRefererLogMapper.selectChannelRefererLogById(id);
	}
	
	/**
     * 查询渠道请求日志列表
     * 
     * @param channelRefererLog 渠道请求日志信息
     * @return 渠道请求日志集合
     */
	@Override
	public List<ChannelRefererLog> selectChannelRefererLogList(ChannelRefererLog channelRefererLog)
	{
	    return channelRefererLogMapper.selectChannelRefererLogList(channelRefererLog);
	}
	
    /**
     * 新增渠道请求日志
     * 
     * @param channelRefererLog 渠道请求日志信息
     * @return 结果
     */
	@Override
	public int insertChannelRefererLog(ChannelRefererLog channelRefererLog)
	{
	    return channelRefererLogMapper.insertChannelRefererLog(channelRefererLog);
	}
	
	/**
     * 修改渠道请求日志
     * 
     * @param channelRefererLog 渠道请求日志信息
     * @return 结果
     */
	@Override
	public int updateChannelRefererLog(ChannelRefererLog channelRefererLog)
	{
	    return channelRefererLogMapper.updateChannelRefererLog(channelRefererLog);
	}

	/**
     * 删除渠道请求日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteChannelRefererLogByIds(String ids)
	{
		return channelRefererLogMapper.deleteChannelRefererLogByIds(Convert.toStrArray(ids));
	}
	
}
