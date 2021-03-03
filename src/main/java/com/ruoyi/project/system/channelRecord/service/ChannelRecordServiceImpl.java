package com.ruoyi.project.system.channelRecord.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.api.znlj.util.ConstantConfig;
import com.ruoyi.project.common.util.AESUtil;
import com.ruoyi.project.system.channelRecord.domain.ChannelRecord;
import com.ruoyi.project.system.channelRecord.mapper.ChannelRecordMapper;

/**
 * 渠道记录 服务层实现
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
@Service
public class ChannelRecordServiceImpl implements IChannelRecordService 
{
    private static final Logger log = LoggerFactory.getLogger(ChannelRecordServiceImpl.class);
    
	@Autowired
	private ChannelRecordMapper channelRecordMapper;

	/**
     * 查询渠道记录信息
     * 
     * @param id 渠道记录ID
     * @return 渠道记录信息
     */
    @Override
	public ChannelRecord selectChannelRecordById(Integer id)
	{
	    return channelRecordMapper.selectChannelRecordById(id);
	}
    
    /**
     * 查询渠道记录列表
     * 
     * @param channelRecord 渠道记录信息
     * @return 渠道鉴权信息
     */
	public ChannelRecord selectChannelRecord(ChannelRecord channelRecord) {
		return channelRecordMapper.selectChannelRecord(channelRecord);
	}
	
	/**
     * 查询渠道记录列表
     * 
     * @param channelRecord 渠道记录信息
     * @return 渠道记录集合
     */
	@Override
	public List<ChannelRecord> selectChannelRecordList(ChannelRecord channelRecord)
	{
		return channelRecordMapper.selectChannelRecordList(channelRecord);
	}
	
	/**
     * 导入渠道数据
     * 
     * @param list 渠道数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
	@Override
    public String importChannelRecord(List<ChannelRecord> list, Boolean isUpdateSupport) 
	{
		if (StringUtils.isNull(list) || list.size() == 0)
        {
            throw new BusinessException("导入渠道数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ChannelRecord channelRecord : list)
        {
        	if(StringUtils.isEmpty(channelRecord.getMobile()))continue;
            try
            {
                // 验证是否存在这个用户
            	String msisdnmask = channelRecord.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            	channelRecord.setMobile(AESUtil.aesEncrypt(channelRecord.getMobile(), ConstantConfig.znlj_appkey));
            	ChannelRecord cr = selectChannelRecord(channelRecord);
            	if (StringUtils.isNull(cr))
                {
                	channelRecord.setMobileMask(msisdnmask);
        			channelRecord.setCreateTime(new Date());
        			channelRecord.setResponseBody("自动导入");
        			channelRecord.setOriginIp("");
        			insertChannelRecord(channelRecord);
        			successNum++;
                    successMsg.append("<br/>" + successNum + "、手机号 " + msisdnmask + " 导入成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、手机号 " + msisdnmask + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
	}
	
    /**
     * 新增渠道记录
     * 
     * @param channelRecord 渠道记录信息
     * @return 结果
     */
	@Override
	public int insertChannelRecord(ChannelRecord channelRecord)
	{
	    return channelRecordMapper.insertChannelRecord(channelRecord);
	}
	
	/**
     * 修改渠道记录
     * 
     * @param channelRecord 渠道记录信息
     * @return 结果
     */
	@Override
	public int updateChannelRecord(ChannelRecord channelRecord)
	{
	    return channelRecordMapper.updateChannelRecord(channelRecord);
	}

	/**
     * 删除渠道记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteChannelRecordByIds(String ids)
	{
		return channelRecordMapper.deleteChannelRecordByIds(Convert.toStrArray(ids));
	}
	
}
