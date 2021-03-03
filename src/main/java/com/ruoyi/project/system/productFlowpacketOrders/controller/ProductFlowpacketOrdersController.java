package com.ruoyi.project.system.productFlowpacketOrders.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.productFlowpacketOrders.domain.ProductFlowpacketOrders;
import com.ruoyi.project.system.productFlowpacketOrders.service.IProductFlowpacketOrdersService;

/**
 * 流量包产品订单 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
@Controller
@RequestMapping("/system/productFlowpacketOrders")
public class ProductFlowpacketOrdersController extends BaseController
{
    private String prefix = "system/productFlowpacketOrders";
	
	@Autowired
	private IProductFlowpacketOrdersService productFlowpacketOrdersService;
	
	@RequiresPermissions("system:productFlowpacketOrders:view")
	@GetMapping()
	public String productFlowpacketOrders()
	{
	    return prefix + "/productFlowpacketOrders";
	}
	
	/**
	 * 查询流量包产品订单列表
	 */
	@RequiresPermissions("system:productFlowpacketOrders:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProductFlowpacketOrders productFlowpacketOrders)
	{
		startPage();
		Map<String, Object> map = productFlowpacketOrders.getParams();
		Object beginTimeObj = map.get("beginTime");
		Object endTimeObj = map.get("endTime");
		
		if(beginTimeObj == null) {
			beginTimeObj = DateUtils.getDate() + " 00:00:00";
		}
		if(endTimeObj == null) {
			endTimeObj = DateUtils.getDate() + " 23:59:59";
		}
		
		productFlowpacketOrders.getParams().put("beginTime", beginTimeObj);
		productFlowpacketOrders.getParams().put("endTime", endTimeObj);
        List<ProductFlowpacketOrders> list = productFlowpacketOrdersService.selectProductFlowpacketOrdersList(productFlowpacketOrders);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出流量包产品订单列表
	 */
	@RequiresPermissions("system:productFlowpacketOrders:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProductFlowpacketOrders productFlowpacketOrders)
    {
    	List<ProductFlowpacketOrders> list = productFlowpacketOrdersService.selectProductFlowpacketOrdersList(productFlowpacketOrders);
        ExcelUtil<ProductFlowpacketOrders> util = new ExcelUtil<ProductFlowpacketOrders>(ProductFlowpacketOrders.class);
        return util.exportExcel(list, "productFlowpacketOrders");
    }
	
	/**
	 * 新增流量包产品订单
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存流量包产品订单
	 */
	@RequiresPermissions("system:productFlowpacketOrders:add")
	@Log(title = "流量包产品订单", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProductFlowpacketOrders productFlowpacketOrders)
	{		
		return toAjax(productFlowpacketOrdersService.insertProductFlowpacketOrders(productFlowpacketOrders));
	}

	/**
	 * 修改流量包产品订单
	 */
	@GetMapping("/edit/{uuid}")
	public String edit(@PathVariable("uuid") String uuid, ModelMap mmap)
	{
		ProductFlowpacketOrders productFlowpacketOrders = productFlowpacketOrdersService.selectProductFlowpacketOrdersById(uuid);
		mmap.put("productFlowpacketOrders", productFlowpacketOrders);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存流量包产品订单
	 */
	@RequiresPermissions("system:productFlowpacketOrders:edit")
	@Log(title = "流量包产品订单", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProductFlowpacketOrders productFlowpacketOrders)
	{		
		return toAjax(productFlowpacketOrdersService.updateProductFlowpacketOrders(productFlowpacketOrders));
	}
	
	/**
	 * 删除流量包产品订单
	 */
	@RequiresPermissions("system:productFlowpacketOrders:remove")
	@Log(title = "流量包产品订单", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(productFlowpacketOrdersService.deleteProductFlowpacketOrdersByIds(ids));
	}
	
}
