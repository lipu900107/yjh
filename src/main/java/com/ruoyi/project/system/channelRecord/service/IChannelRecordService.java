package com.ruoyi.project.system.channelRecord.service;

import com.ruoyi.project.system.channelRecord.domain.ChannelRecord;

import java.util.List;

/**
 * 渠道记录 服务层
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
public interface IChannelRecordService 
{
	/**
     * 查询渠道记录信息
     * 
     * @param id 渠道记录ID
     * @return 渠道记录信息
     */
	public ChannelRecord selectChannelRecordById(Integer id);
    
    /**
     * 查询渠道记录列表
     * 
     * @param channelRecord 渠道记录信息
     * @return 渠道鉴权信息
     */
	public ChannelRecord selectChannelRecord(ChannelRecord channelRecord);
	
	/**
     * 查询渠道记录列表
     * 
     * @param channelRecord 渠道记录信息
     * @return 渠道记录集合
     */
	public List<ChannelRecord> selectChannelRecordList(ChannelRecord channelRecord);
	
	/**
     * 导入渠道数据
     * 
     * @param list 渠道数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importChannelRecord(List<ChannelRecord> list, Boolean isUpdateSupport);
	
	/**
     * 新增渠道记录
     * 
     * @param channelRecord 渠道记录信息
     * @return 结果
     */
	public int insertChannelRecord(ChannelRecord channelRecord);
	
	/**
     * 修改渠道记录
     * 
     * @param channelRecord 渠道记录信息
     * @return 结果
     */
	public int updateChannelRecord(ChannelRecord channelRecord);
		
	/**
     * 删除渠道记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteChannelRecordByIds(String ids);
	
}
