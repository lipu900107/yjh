package com.ruoyi.project.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReqValidate {
	
	private static final Logger LOGGER = LogManager.getLogger(ReqValidate.class);

	public static boolean validateUrl(String urlReq, String accessUrlReq, String logId) {
		if(StringUtils.isEmpty(urlReq) || StringUtils.isEmpty(accessUrlReq)){
			LOGGER.debug("{}|urlReq={}|urls={}", logId, urlReq, accessUrlReq);
			return false;
		}
		
		// url支持列表形式，以逗号分隔
		String[] accessUrl = accessUrlReq.split(",");
		for (String tempUrl : accessUrl) {
			// url地址，对问号？前部分进行精准匹配
			String[] urls = tempUrl.split("\\?");
			String url = urls.length > 0 ? urls[0] : tempUrl;
			// 配置的url包含于请求参数url
			if (urlReq.contains(url)) {
				LOGGER.debug("{}|validate redirectUrl success.urlReq={}|urls={}", logId, urlReq, accessUrlReq);
				return true;
			}
		}
		LOGGER.warn("{}|validate redirectUrl failed.urlReq={}|urls={}", logId, urlReq, accessUrlReq);
		return false;
	}
	
	public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2="^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";// 验证手机号
        if(StringUtils.isNotBlank(str)){
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
	}
}
