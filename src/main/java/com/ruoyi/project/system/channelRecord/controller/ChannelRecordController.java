package com.ruoyi.project.system.channelRecord.controller;

import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.api.znlj.util.ConstantConfig;
import com.ruoyi.project.common.util.AESUtil;
import com.ruoyi.project.common.util.AesUtils;
import com.ruoyi.project.system.channelAuth.domain.ChannelAuth;
import com.ruoyi.project.system.channelAuth.service.IChannelAuthService;
import com.ruoyi.project.system.channelRecord.domain.ChannelRecord;
import com.ruoyi.project.system.channelRecord.service.IChannelRecordService;

/**
 * 渠道记录 信息操作处理
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
@Controller
@RequestMapping("/system/channelRecord")
public class ChannelRecordController extends BaseController
{
	private String prefix = "system/channelRecord";

	@Autowired
	private IChannelRecordService channelRecordService;

	@Autowired
	private IChannelAuthService channelAuthService;

	@RequiresPermissions("system:channelRecord:view")
	@GetMapping()
	public String channelRecord()
	{
		return prefix + "/channelRecord";
	}

	/**
	 * 查询渠道记录列表
	 */
	@RequiresPermissions("system:channelRecord:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ChannelRecord channelRecord)
	{
		startPage();
		String rb = channelRecord.getResponseBody();
		if(!StringUtils.isEmpty(rb) && "2".equals(rb)) {
			channelRecord.setResponseBody("inresponseto");
		}
		List<ChannelRecord> list = channelRecordService.selectChannelRecordList(channelRecord);
		return getDataTable(list);
	}


	/**
	 * 导出渠道记录列表
	 */
	@RequiresPermissions("system:channelRecord:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ChannelRecord channelRecord)
	{
		List<ChannelRecord> list = new ArrayList<ChannelRecord>();
		try {
			String rb = channelRecord.getResponseBody();
			if(!StringUtils.isEmpty(rb) && "2".equals(rb)) {
				channelRecord.setResponseBody("inresponseto");
			}
			List<ChannelRecord> channelRecords = channelRecordService.selectChannelRecordList(channelRecord);

			ChannelAuth channelAuth = new ChannelAuth();
			String channelAppId = channelRecord.getChannelAppid();
			if(!StringUtils.isEmpty(channelAppId)) {
				channelAuth.setChannelAppid(channelRecord.getChannelAppid());
				channelAuth = channelAuthService.selectChannelAuth(channelAuth);
			}
			String aesPwd = channelAuth != null && !StringUtils.isEmpty(channelAuth.getPrivateKey())?channelAuth.getPrivateKey():ConstantConfig.AES_PWD; 

			for (ChannelRecord cr : channelRecords) {
				if(!StringUtils.isEmpty(channelAppId) 
						&& (channelAppId.startsWith("ZXXL_TJ0619") || "ZXXL_TJ0619".equals(channelAppId) || "ZXXL_TJ0619_01".equals(channelAppId)
								|| "ZXXL_TJ0619_02".equals(channelAppId))) {
					if(StringUtils.isEmpty(cr.getOriginIp()) && "自动导入".equals(cr.getResponseBody())) {
						cr.setMobile(AesUtils.aesEncrypt(AESUtil.aesDecrypt(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						list.add(cr);
					} else {
						cr.setMobile(AesUtils.aesEncrypt(AESUtil.DeCodeMobileAES(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						list.add(cr);
					}
				} else {
					if(StringUtils.isEmpty(cr.getOriginIp()) && "自动导入".equals(cr.getResponseBody())) {
						cr.setMobile(AesUtils.encryptor(AESUtil.aesDecrypt(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						list.add(cr);
					} else {
						cr.setMobile(AesUtils.encryptor(AESUtil.DeCodeMobileAES(cr.getMobile(), ConstantConfig.znlj_appkey), aesPwd));
						list.add(cr);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ExcelUtil<ChannelRecord> util = new ExcelUtil<ChannelRecord>(ChannelRecord.class);
		return util.exportExcel(list, "渠道数据");
	}

	@Log(title = "渠道导入", businessType = BusinessType.IMPORT)
	@RequiresPermissions("system:channelRecord:import")
	@PostMapping("/importData")
	@ResponseBody
	public AjaxResult importData(MultipartFile file) throws Exception
	{
		ExcelUtil<ChannelRecord> util = new ExcelUtil<ChannelRecord>(ChannelRecord.class);
		List<ChannelRecord> list = util.importExcel(file.getInputStream());
		String message = channelRecordService.importChannelRecord(list, true);
		return AjaxResult.success(message);
	}

	@RequiresPermissions("system:channelRecord:view")
	@GetMapping("/importTemplate")
	@ResponseBody
	public AjaxResult importTemplate()
	{
		ExcelUtil<ChannelRecord> util = new ExcelUtil<ChannelRecord>(ChannelRecord.class);
		return util.importTemplateExcel("渠道数据");
	}

	/**
	 * 新增渠道记录
	 */
	@GetMapping("/add")
	public String add()
	{
		return prefix + "/add";
	}

	/**
	 * 新增保存渠道记录
	 */
	@RequiresPermissions("system:channelRecord:add")
	@Log(title = "渠道记录", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ChannelRecord channelRecord)
	{		
		return toAjax(channelRecordService.insertChannelRecord(channelRecord));
	}

	/**
	 * 修改渠道记录
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ChannelRecord channelRecord = channelRecordService.selectChannelRecordById(id);
		mmap.put("channelRecord", channelRecord);
		return prefix + "/edit";
	}

	/**
	 * 修改保存渠道记录
	 */
	@RequiresPermissions("system:channelRecord:edit")
	@Log(title = "渠道记录", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ChannelRecord channelRecord)
	{		
		return toAjax(channelRecordService.updateChannelRecord(channelRecord));
	}

	/**
	 * 删除渠道记录
	 */
	@RequiresPermissions("system:channelRecord:remove")
	@Log(title = "渠道记录", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(channelRecordService.deleteChannelRecordByIds(ids));
	}

}
