package com.ruoyi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.ruoyi.common.utils.HttpUtil;

public class YsmGoldUtil {
	
	public static void main(String[] args) throws Exception {

		String url = "http://m.7hotest.com/user/getMobile";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		String dateName = df.format(calendar.getTime());
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", "taw-1111111");
		map.put("mobile", "15757116144");
		map.put("timestamp", dateName);
		String result = HttpUtil.doGet(url, map);
		System.out.println(result);
    }
	
}
