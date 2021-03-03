package com.ruoyi.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.project.common.util.HttpClientUtils;

/**
 * 获取IP方法
 * 
 * @author ruoyi
 */
public class IpUtils
{
	public static String getIpAddr(HttpServletRequest request)
	{
		if (request == null)
		{
			return "unknown";
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("X-Real-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}

		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	public static boolean internalIp(String ip)
	{
		byte[] addr = textToNumericFormatV4(ip);
		return internalIp(addr) || "127.0.0.1".equals(ip);
	}

	private static boolean internalIp(byte[] addr)
	{
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0)
		{
		case SECTION_1:
			return true;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4)
			{
				return true;
			}
		case SECTION_5:
			switch (b1)
			{
			case SECTION_6:
				return true;
			}
		default:
			return false;
		}
	}

	/**
	 * 将IPv4地址转换成字节
	 * 
	 * @param text IPv4地址
	 * @return byte 字节
	 */
	public static byte[] textToNumericFormatV4(String text)
	{
		if (text.length() == 0)
		{
			return null;
		}

		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try
		{
			long l;
			int i;
			switch (elements.length)
			{
			case 1:
				l = Long.parseLong(elements[0]);
				if ((l < 0L) || (l > 4294967295L))
					return null;
				bytes[0] = (byte) (int) (l >> 24 & 0xFF);
				bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
				bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 2:
				l = Integer.parseInt(elements[0]);
				if ((l < 0L) || (l > 255L))
					return null;
				bytes[0] = (byte) (int) (l & 0xFF);
				l = Integer.parseInt(elements[1]);
				if ((l < 0L) || (l > 16777215L))
					return null;
				bytes[1] = (byte) (int) (l >> 16 & 0xFF);
				bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 3:
				for (i = 0; i < 2; ++i)
				{
					l = Integer.parseInt(elements[i]);
					if ((l < 0L) || (l > 255L))
						return null;
					bytes[i] = (byte) (int) (l & 0xFF);
				}
				l = Integer.parseInt(elements[2]);
				if ((l < 0L) || (l > 65535L))
					return null;
				bytes[2] = (byte) (int) (l >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 4:
				for (i = 0; i < 4; ++i)
				{
					l = Integer.parseInt(elements[i]);
					if ((l < 0L) || (l > 255L))
						return null;
					bytes[i] = (byte) (int) (l & 0xFF);
				}
				break;
			default:
				return null;
			}
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		return bytes;
	}

	public static String getHostIp()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
		}
		return "127.0.0.1";
	}

	public static String getHostName()
	{
		try
		{
			return InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e)
		{
		}
		return "未知";
	}

	/**
	 * 
	 * 请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding 服务器端请求编码。如GBK,UTF-8等
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getIpIsp(String ip) throws Exception {
		String urlStr = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";
		String param = "resource_id=6006&ie=utf8&oe=gbk&format=json&tn=baidu&query=" + ip;
		String locationIsp = null;
		try {
			String result = HttpClientUtils.get(urlStr, param, "GBK");
			if(StringUtils.isEmpty(result)) return locationIsp;
			JSONObject jsonObject = JSONObject.parseObject(result.substring(result.indexOf("{")));
			String data = jsonObject.getString("data");
			JSONArray jsonArray = JSONArray.parseArray(data);
			if(jsonArray == null || jsonArray.size() == 0) return locationIsp;
			jsonObject = jsonArray.getJSONObject(0);
			if(jsonObject == null) return null;
			locationIsp = jsonObject.getString("location");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locationIsp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String originIp = "127.0.0.1";
		String ipIspStr = "";
		StringBuffer sb = new StringBuffer();
		
		try {
			String[] ips = originIp.replaceAll(" ", "").split(",");
			for (int i = 0; i < ips.length; i++) {
				String ip = ips[i];
				String locationIsp = IpUtils.getIpIsp(ip);
				if(StringUtils.isEmpty(locationIsp)) continue;
				sb.append(locationIsp).append(",");
			}
			
			if(sb.length() > 0) {
				ipIspStr = sb.toString().substring(0, sb.toString().lastIndexOf(","));
			}
			
			String ispStr = "无";
			if(ipIspStr.indexOf(",") == -1 && ipIspStr.indexOf("移动") > 0) {
				ispStr = "移动";
			} else if(ipIspStr.indexOf(",") == -1 && ipIspStr.indexOf("联通") > 0) {
				ispStr = "联通";
			} else if(ipIspStr.indexOf(",") == -1 && ipIspStr.indexOf("电信") > 0) {
				ispStr = "电信";
			} else if(ipIspStr.indexOf("局域网") > 0) {
				ispStr = "局域网";
			}
			
			System.out.println(ipIspStr + "|" + ispStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}