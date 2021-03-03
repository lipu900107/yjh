package com.ruoyi.project.system.productOrdersLog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.project.common.util.ToolUtils;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;
import com.ruoyi.project.system.productOrdersLog.mapper.ProductOrdersLogMapper;

/**
 * 产品订单日志 服务层实现
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
@Service
public class ProductOrdersLogServiceImpl implements IProductOrdersLogService 
{
	@Autowired
	private ProductOrdersLogMapper productOrdersLogMapper;

	/**
     * 查询产品订单日志信息
     * 
     * @param uuid 产品订单日志ID
     * @return 产品订单日志信息
     */
    @Override
	public ProductOrdersLog selectProductOrdersLogById(String uuid)
	{
	    return productOrdersLogMapper.selectProductOrdersLogById(uuid);
	}
	
	/**
     * 查询产品订单日志列表
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 产品订单日志集合
     */
	@Override
	public List<ProductOrdersLog> selectProductOrdersLogList(ProductOrdersLog productOrdersLog)
	{
	    return productOrdersLogMapper.selectProductOrdersLogList(productOrdersLog);
	}
	
    /**
     * 新增产品订单日志
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 结果
     */
	@Override
	public int insertProductOrdersLog(ProductOrdersLog productOrdersLog)
	{
	    return productOrdersLogMapper.insertProductOrdersLog(productOrdersLog);
	}
	
	/**
     * 新增产品订单日志
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 结果
     */
	@Override
	public int insertProductOrdersLog(ProductOrdersLog productOrdersLog, String originIp, String originUrl)
	{
		productOrdersLog.setOriginIp(originIp);
		productOrdersLog.setOriginUrl(originUrl);
		productOrdersLog.setUuid(ToolUtils.getUUID());
	    return productOrdersLogMapper.insertProductOrdersLog(productOrdersLog);
	}
	
	/**
     * 修改产品订单日志
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 结果
     */
	@Override
	public int updateProductOrdersLog(ProductOrdersLog productOrdersLog)
	{
	    return productOrdersLogMapper.updateProductOrdersLog(productOrdersLog);
	}

	/**
     * 删除产品订单日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProductOrdersLogByIds(String ids)
	{
		return productOrdersLogMapper.deleteProductOrdersLogByIds(Convert.toStrArray(ids));
	}
	
}
