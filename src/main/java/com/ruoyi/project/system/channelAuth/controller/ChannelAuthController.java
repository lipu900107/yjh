package com.ruoyi.project.system.channelAuth.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.util.Md5Utils;
import com.ruoyi.project.system.channelAuth.domain.ChannelAuth;
import com.ruoyi.project.system.channelAuth.service.IChannelAuthService;
import com.ruoyi.project.system.dict.domain.DictData;
import com.ruoyi.project.system.dict.service.IDictDataService;

/**
 * 渠道鉴权 信息操作处理
 * 
 * @author ruoyi
 * @date 2020-05-31
 */
@Controller
@RequestMapping("/system/channelAuth")
public class ChannelAuthController extends BaseController
{
    private String prefix = "system/channelAuth";
	
	@Autowired
	private IChannelAuthService channelAuthService;
	
	@Autowired
	private IDictDataService dictDataService;
	
	@RequiresPermissions("system:channelAuth:view")
	@GetMapping()
	public String channelAuth()
	{
	    return prefix + "/channelAuth";
	}
	
	/**
	 * 查询渠道鉴权列表
	 */
	@RequiresPermissions("system:channelAuth:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ChannelAuth channelAuth)
	{
		startPage();
        List<ChannelAuth> list = channelAuthService.selectChannelAuthList(channelAuth);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出渠道鉴权列表
	 */
	@RequiresPermissions("system:channelAuth:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ChannelAuth channelAuth)
    {
    	List<ChannelAuth> list = channelAuthService.selectChannelAuthList(channelAuth);
        ExcelUtil<ChannelAuth> util = new ExcelUtil<ChannelAuth>(ChannelAuth.class);
        return util.exportExcel(list, "channelAuth");
    }
	
	/**
	 * 新增渠道鉴权
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存渠道鉴权
	 */
	@RequiresPermissions("system:channelAuth:add")
	@Log(title = "渠道鉴权", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ChannelAuth channelAuth)
	{		
		String privateKey = Md5Utils.strToMd5(channelAuth.getChannelAppid());
		channelAuth.setPrivateKey(privateKey);
		String appId = channelAuth.getChannelAppid();
		channelAuth.setPublicKey(appId);
		channelAuth.setChannelAppkey(appId);
		channelAuth.setCreateTime(new Date());
		
		DictData dict = new DictData();
		dict.setDictSort(new Long(25));
		dict.setDictLabel(channelAuth.getChannelName());
		dict.setDictValue(appId);
		dict.setDictType("channelApp");
		dict.setListClass("default");
		dict.setIsDefault("Y");
		dict.setStatus("0");
		dictDataService.insertDictData(dict);
		
		return toAjax(channelAuthService.insertChannelAuth(channelAuth));
	}

	/**
	 * 修改渠道鉴权
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ChannelAuth channelAuth = channelAuthService.selectChannelAuthById(id);
		mmap.put("channelAuth", channelAuth);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存渠道鉴权
	 */
	@RequiresPermissions("system:channelAuth:edit")
	@Log(title = "渠道鉴权", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ChannelAuth channelAuth)
	{	
		return toAjax(channelAuthService.updateChannelAuth(channelAuth));
	}
	
	/**
	 * 删除渠道鉴权
	 */
	@RequiresPermissions("system:channelAuth:remove")
	@Log(title = "渠道鉴权", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		// return toAjax(channelAuthService.deleteChannelAuthByIds(ids));
		if(StringUtils.isNotBlank(ids)) {
			String[] idArr = Convert.toStrArray(ids);
			ChannelAuth channelAuth = new ChannelAuth();
			for (int i = 0; i < idArr.length; i++) {
				String id = idArr[i];
				channelAuth = channelAuthService.selectChannelAuthById(Integer.valueOf(id));
				channelAuth.setAuthStatus("1");
				channelAuthService.updateChannelAuth(channelAuth);
			}
		}
		return AjaxResult.success();
	}
	
}
