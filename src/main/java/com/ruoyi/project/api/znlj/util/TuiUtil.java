package com.ruoyi.project.api.znlj.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.ruoyi.common.utils.HttpUtil;

public class TuiUtil {
	
	public static String getMobile(String mobile,String orderId){
		String url = "http://m.7hotest.com/user/getMobile";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("mobile", mobile);
		map.put("timestamp", dateName);
		String result = HttpUtil.doGet(url, map);
		return result;
	}

}
