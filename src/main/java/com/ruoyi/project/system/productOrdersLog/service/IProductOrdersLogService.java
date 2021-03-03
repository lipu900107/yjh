package com.ruoyi.project.system.productOrdersLog.service;

import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;
import java.util.List;

/**
 * 产品订单日志 服务层
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
public interface IProductOrdersLogService 
{
	/**
     * 查询产品订单日志信息
     * 
     * @param uuid 产品订单日志ID
     * @return 产品订单日志信息
     */
	public ProductOrdersLog selectProductOrdersLogById(String uuid);
	
	/**
     * 查询产品订单日志列表
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 产品订单日志集合
     */
	public List<ProductOrdersLog> selectProductOrdersLogList(ProductOrdersLog productOrdersLog);
	
	/**
     * 新增产品订单日志
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 结果
     */
	public int insertProductOrdersLog(ProductOrdersLog productOrdersLog);
	
	/**
     * 新增产品订单日志
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 结果
     */
	public int insertProductOrdersLog(ProductOrdersLog productOrdersLog, String originIp, String originUrl);
	
	/**
     * 修改产品订单日志
     * 
     * @param productOrdersLog 产品订单日志信息
     * @return 结果
     */
	public int updateProductOrdersLog(ProductOrdersLog productOrdersLog);
		
	/**
     * 删除产品订单日志信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProductOrdersLogByIds(String ids);
	
}
