package com.ruoyi.project.system.productFlowpacketOrders.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.productFlowpacketOrders.mapper.ProductFlowpacketOrdersMapper;
import com.ruoyi.project.system.productFlowpacketOrders.domain.ProductFlowpacketOrders;
import com.ruoyi.project.system.productFlowpacketOrders.service.IProductFlowpacketOrdersService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 流量包产品订单 服务层实现
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
@Service
public class ProductFlowpacketOrdersServiceImpl implements IProductFlowpacketOrdersService 
{
	@Autowired
	private ProductFlowpacketOrdersMapper productFlowpacketOrdersMapper;

	/**
     * 查询流量包产品订单信息
     * 
     * @param uuid 流量包产品订单ID
     * @return 流量包产品订单信息
     */
    @Override
	public ProductFlowpacketOrders selectProductFlowpacketOrdersById(String uuid)
	{
	    return productFlowpacketOrdersMapper.selectProductFlowpacketOrdersById(uuid);
	}
	
	/**
     * 查询流量包产品订单列表
     * 
     * @param productFlowpacketOrders 流量包产品订单信息
     * @return 流量包产品订单集合
     */
	@Override
	public List<ProductFlowpacketOrders> selectProductFlowpacketOrdersList(ProductFlowpacketOrders productFlowpacketOrders)
	{
	    return productFlowpacketOrdersMapper.selectProductFlowpacketOrdersList(productFlowpacketOrders);
	}
	
    /**
     * 新增流量包产品订单
     * 
     * @param productFlowpacketOrders 流量包产品订单信息
     * @return 结果
     */
	@Override
	public int insertProductFlowpacketOrders(ProductFlowpacketOrders productFlowpacketOrders)
	{
	    return productFlowpacketOrdersMapper.insertProductFlowpacketOrders(productFlowpacketOrders);
	}
	
	/**
     * 修改流量包产品订单
     * 
     * @param productFlowpacketOrders 流量包产品订单信息
     * @return 结果
     */
	@Override
	public int updateProductFlowpacketOrders(ProductFlowpacketOrders productFlowpacketOrders)
	{
	    return productFlowpacketOrdersMapper.updateProductFlowpacketOrders(productFlowpacketOrders);
	}

	/**
     * 删除流量包产品订单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProductFlowpacketOrdersByIds(String ids)
	{
		return productFlowpacketOrdersMapper.deleteProductFlowpacketOrdersByIds(Convert.toStrArray(ids));
	}
	
}
