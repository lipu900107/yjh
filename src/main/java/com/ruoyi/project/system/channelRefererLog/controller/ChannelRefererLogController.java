package com.ruoyi.project.system.channelRefererLog.controller;

import java.util.Date;
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
import com.ruoyi.project.system.channelRefererLog.domain.ChannelRefererLog;
import com.ruoyi.project.system.channelRefererLog.service.IChannelRefererLogService;

/**
 * 渠道请求日志 信息操作处理
 * 
 * @author ruoyi
 * @date 2020-07-19
 */
@Controller
@RequestMapping("/system/channelRefererLog")
public class ChannelRefererLogController extends BaseController
{
	private String prefix = "system/channelRefererLog";

	@Autowired
	private IChannelRefererLogService channelRefererLogService;

	@RequiresPermissions("system:channelRefererLog:view")
	@GetMapping()
	public String channelRefererLog()
	{
		return prefix + "/channelRefererLog";
	}

	/**
	 * 查询渠道请求日志列表
	 */
	@RequiresPermissions("system:channelRefererLog:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ChannelRefererLog channelRefererLog)
	{
		startPage();
		List<ChannelRefererLog> list = channelRefererLogService.selectChannelRefererLogList(channelRefererLog);
		return getDataTable(list);
	}


	/**
	 * 导出渠道请求日志列表
	 */
	@RequiresPermissions("system:channelRefererLog:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ChannelRefererLog channelRefererLog)
	{
		Map<String, Object> map = channelRefererLog.getParams();
		Object beginTimeObj = map.get("beginTime");
		Object endTimeObj = map.get("endTime");
		if(beginTimeObj == null && endTimeObj == null) {
			return error("开始时间和结束时间不能同时为空。");
		}
		if(beginTimeObj == null) {
			return error("开始时间不能为空。");
		}
		
		Date endTime = new Date();
		String beginTimeStr = beginTimeObj.toString();
		if(endTimeObj != null) {
			String endTimeStr = endTime.toString();
			endTime = DateUtils.dateTime(endTimeStr, "yyyy-MM-dd HH:mm:ss");
		}
		int second = DateUtils.getDistanceSecondTime(DateUtils.dateTime(beginTimeStr, "yyyy-MM-dd HH:mm:ss"), endTime);
		if(second > 60*60*24*15) {	// 不能查询大于15天的信息
			return error("不能查询大于15天的信息。");
		}
		
		channelRefererLog.getParams().put("beginTime", beginTimeObj);
		channelRefererLog.getParams().put("endTime", endTimeObj);
		List<ChannelRefererLog> list = channelRefererLogService.selectChannelRefererLogList(channelRefererLog);
		ExcelUtil<ChannelRefererLog> util = new ExcelUtil<ChannelRefererLog>(ChannelRefererLog.class);
		return util.exportExcel(list, "channelRefererLog");
	}

	/**
	 * 新增渠道请求日志
	 */
	@GetMapping("/add")
	public String add()
	{
		return prefix + "/add";
	}

	/**
	 * 新增保存渠道请求日志
	 */
	@RequiresPermissions("system:channelRefererLog:add")
	@Log(title = "渠道请求日志", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ChannelRefererLog channelRefererLog)
	{		
		return toAjax(channelRefererLogService.insertChannelRefererLog(channelRefererLog));
	}

	/**
	 * 修改渠道请求日志
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ChannelRefererLog channelRefererLog = channelRefererLogService.selectChannelRefererLogById(id);
		mmap.put("channelRefererLog", channelRefererLog);
		return prefix + "/edit";
	}

	/**
	 * 修改保存渠道请求日志
	 */
	@RequiresPermissions("system:channelRefererLog:edit")
	@Log(title = "渠道请求日志", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ChannelRefererLog channelRefererLog)
	{		
		return toAjax(channelRefererLogService.updateChannelRefererLog(channelRefererLog));
	}

	/**
	 * 删除渠道请求日志
	 */
	@RequiresPermissions("system:channelRefererLog:remove")
	@Log(title = "渠道请求日志", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(channelRefererLogService.deleteChannelRefererLogByIds(ids));
	}

}
