package com.ruoyi.project.system.productFlowpacketOrders.service;

import com.ruoyi.project.system.productFlowpacketOrders.domain.ProductFlowpacketOrders;
import java.util.List;

/**
 * 流量包产品订单 服务层
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
public interface IProductFlowpacketOrdersService 
{
	/**
     * 查询流量包产品订单信息
     * 
     * @param uuid 流量包产品订单ID
     * @return 流量包产品订单信息
     */
	public ProductFlowpacketOrders selectProductFlowpacketOrdersById(String uuid);
	
	/**
     * 查询流量包产品订单列表
     * 
     * @param productFlowpacketOrders 流量包产品订单信息
     * @return 流量包产品订单集合
     */
	public List<ProductFlowpacketOrders> selectProductFlowpacketOrdersList(ProductFlowpacketOrders productFlowpacketOrders);
	
	/**
     * 新增流量包产品订单
     * 
     * @param productFlowpacketOrders 流量包产品订单信息
     * @return 结果
     */
	public int insertProductFlowpacketOrders(ProductFlowpacketOrders productFlowpacketOrders);
	
	/**
     * 修改流量包产品订单
     * 
     * @param productFlowpacketOrders 流量包产品订单信息
     * @return 结果
     */
	public int updateProductFlowpacketOrders(ProductFlowpacketOrders productFlowpacketOrders);
		
	/**
     * 删除流量包产品订单信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProductFlowpacketOrdersByIds(String ids);
	
}
