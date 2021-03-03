package com.ruoyi.project.system.productOrdersLog.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.productOrdersLog.domain.ProductOrdersLog;
import com.ruoyi.project.system.productOrdersLog.service.IProductOrdersLogService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 产品订单日志 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-03-27
 */
@Controller
@RequestMapping("/system/productOrdersLog")
public class ProductOrdersLogController extends BaseController
{
    private String prefix = "system/productOrdersLog";
	
	@Autowired
	private IProductOrdersLogService productOrdersLogService;
	
	@RequiresPermissions("system:productOrdersLog:view")
	@GetMapping()
	public String productOrdersLog()
	{
	    return prefix + "/productOrdersLog";
	}
	
	/**
	 * 查询产品订单日志列表
	 */
	@RequiresPermissions("system:productOrdersLog:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProductOrdersLog productOrdersLog)
	{
		startPage();
        List<ProductOrdersLog> list = productOrdersLogService.selectProductOrdersLogList(productOrdersLog);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出产品订单日志列表
	 */
	@RequiresPermissions("system:productOrdersLog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProductOrdersLog productOrdersLog)
    {
    	List<ProductOrdersLog> list = productOrdersLogService.selectProductOrdersLogList(productOrdersLog);
        ExcelUtil<ProductOrdersLog> util = new ExcelUtil<ProductOrdersLog>(ProductOrdersLog.class);
        return util.exportExcel(list, "productOrdersLog");
    }
	
	/**
	 * 新增产品订单日志
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存产品订单日志
	 */
	@RequiresPermissions("system:productOrdersLog:add")
	@Log(title = "产品订单日志", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProductOrdersLog productOrdersLog)
	{		
		return toAjax(productOrdersLogService.insertProductOrdersLog(productOrdersLog));
	}

	/**
	 * 修改产品订单日志
	 */
	@GetMapping("/edit/{uuid}")
	public String edit(@PathVariable("uuid") String uuid, ModelMap mmap)
	{
		ProductOrdersLog productOrdersLog = productOrdersLogService.selectProductOrdersLogById(uuid);
		mmap.put("productOrdersLog", productOrdersLog);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存产品订单日志
	 */
	@RequiresPermissions("system:productOrdersLog:edit")
	@Log(title = "产品订单日志", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProductOrdersLog productOrdersLog)
	{		
		return toAjax(productOrdersLogService.updateProductOrdersLog(productOrdersLog));
	}
	
	/**
	 * 删除产品订单日志
	 */
	@RequiresPermissions("system:productOrdersLog:remove")
	@Log(title = "产品订单日志", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(productOrdersLogService.deleteProductOrdersLogByIds(ids));
	}
	
}
