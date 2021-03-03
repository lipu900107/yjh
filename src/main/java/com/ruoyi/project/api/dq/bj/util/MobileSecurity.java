package com.ruoyi.project.api.dq.bj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import com.ai.ipu.basic.cipher.DES;
import com.ai.ipu.basic.cipher.RSA;

public class MobileSecurity {

	private static RSAPublicKey publicKey;
	private static ThreadLocal<String> randomDesKeyThreadLocal = new ThreadLocal<String>();
	private static ThreadLocal<SecretKey> secretKeyThreadLocal = new ThreadLocal<SecretKey>();

	public static void init() {
		String randomDesKey = String.valueOf((8.9999999E7d * Math.random()) + 1.0E7d);
		randomDesKeyThreadLocal.set(randomDesKey);
		try {
			secretKeyThreadLocal.set(DES.getKey(randomDesKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getResKey() {
		Map<String, String> postParam = new HashMap<String, String>();
		postParam.put(BjConstant.Server.ACTION, BjConstant.MobileSecurity.RES_KEY_ACTION);
		init();
		postParam.put(BjConstant.Server.KEY, getDesKey());
		try {
			return new DataMap(responseDecrypt(HttpTool.httpRequest(getRequestUrl(), HttpTool.toQueryStringWithEncode(postParam), "POST"))).getString("KEY");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getDesKey() {
		try {
			return RSA.encrypt(getPublicKey(), (String) randomDesKeyThreadLocal.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String requestEncrypt(String data) {
		try {
			return DES.encryptString((Key) secretKeyThreadLocal.get(), data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String responseDecrypt(String data) {
		try {
			return DES.decryptString((Key) secretKeyThreadLocal.get(), data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private static RSAPublicKey getPublicKey() {
		if (publicKey == null) {
			try {
				ClassPathResource cpr = new ClassPathResource("public_key.properties");
				InputStream bais = cpr.getInputStream();
				publicKey = RSA.loadPublicKey(bais);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return publicKey;
	}

	public static String decryptReader(Reader cipherReader) {
		try {
			String plainText;
			BufferedReader bReader = new BufferedReader(cipherReader, 1024);
			StringBuilder sb = new StringBuilder();
			if (cipherReader.ready()) {
				while ((plainText = bReader.readLine()) != null) {
					sb.append(plainText);
				}

				plainText = DES.decryptString(DES.getKey(getResKey()), sb.toString());
				return plainText;
			}

			plainText = DES.decryptString(DES.getKey(getResKey()), sb.toString());
			return plainText;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (cipherReader != null) {
					cipherReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	//云商盟接口请求
	public static String post(String action, String data, String url, String cookie) {
		MobileSecurity.init();
		String result = "";
		String ts = null;
		try {
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);

			if(StringUtils.isNotBlank(cookie)) {
				postMethod.addRequestHeader("Cookie", cookie);
			}

			postMethod.setParameter("data", MobileSecurity.requestEncrypt(data));
			postMethod.setParameter("action", action);
			postMethod.setParameter("key", MobileSecurity.getDesKey());
			client.executeMethod(postMethod);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = reader.readLine()) != null) {
				stringBuffer.append(str);
			}
			ts = stringBuffer.toString();
			result = MobileSecurity.responseDecrypt(ts);
			postMethod.releaseConnection();
		} catch (Exception e) {
			System.out.println("云商盟接口请求结果异常：" + ts);
			e.printStackTrace();
		}
		return result;
	}

	private static String getRequestUrl() {
		String baseUrl = getRequestBaseUrl();
		return baseUrl + getRequestServlet();
	}

	private static String getRequestBaseUrl() {
		String host = getRequestHost();
		return host + getRequestPath();
	}

	private static String getRequestHost() {
		return "http://service.bj.10086.cn";
	}

	private static String getRequestPath() {
		return "/app/channel-app";
	}

	private static String getRequestServlet() {
		return "/mobiledata";
	}
}
