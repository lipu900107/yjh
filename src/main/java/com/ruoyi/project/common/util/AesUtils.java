package com.ruoyi.project.common.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.ruoyi.project.api.znlj.util.ConstantConfig;

public class AesUtils {

	public AesUtils() {
	}

	public static String aesEncrypt(String content, String password) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(password.getBytes());
		kgen.init(128, random);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(1, key);
		byte[] result = cipher.doFinal(byteContent);
		return result != null && result.length > 0?Base64.encodeBase64URLSafeString(result):null;
	}

	public static String aesDecrypt(String content, String password) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(password.getBytes());
		kgen.init(128, random);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(2, key);
		byte[] result = cipher.doFinal(Base64.decodeBase64(content));
		return result != null && result.length > 0?new String(result, "utf-8"):null;
	}

	public static String decrypt(String content, String password) throws Exception {
		byte[] keys = { 0x41, 0x72, 0x65, 0x79, 0x6F, 0x75, 0x6D, 0x79};
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(desKey);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE,securekey,new IvParameterSpec(keys));
		byte[] result=cipher.doFinal(Base64.decodeBase64(content));
		return result != null && result.length > 0?new String(result, "utf-8"):null;
	}

	public static String encryptor(String str,String Key){
		String s=null;
		try {
			byte[] keys = { 0x41, 0x72, 0x65, 0x79, 0x6F, 0x75, 0x6D, 0x79};
			DESKeySpec desKey = new DESKeySpec(Key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE,securekey,new IvParameterSpec(keys));    //初始化密码器，用密钥 secretKey 进入加密模式
			byte[] bytes=cipher.doFinal(str.getBytes());   //加密
			s= Base64.encodeBase64String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return "加密错误.";
		}
		return s;
	}

	public static void main(String[] args) throws Exception {
		String password = "dGRqcqOoYmqjqXh4a2p5eHpyZ3MxNDY1Mjg2MjAyNDcxMTQ2NTI4NjIwMjQ4MQ==";
		
		String s = AESUtil.aesDecrypt("o3EmJdG/6a6G0V737fAM0A==", ConstantConfig.znlj_appkey);
		System.out.println(s);
		
		/*
		System.out.println(AesUtils.encryptor(AESUtil.aesDecrypt("lM0jJDtfS5XrWkmBe9enhA==", ConstantConfig.znlj_appkey), password));
		
		String ordid = "15133876743";
        System.out.println(aesEncrypt(ordid, password));
		System.out.println(aesDecrypt("92F5d5rjC__vYkPuBCVC8g", password));
		System.out.println(decrypt("hEE6T8P6Un0gJacQvXNa+Q==", password));
		*/
		
		
		try {
			System.out.println(decrypt("gffvY66QVQDu30DSg5YY2w==", password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		System.out.println(AESUtil.DeCodeMobileAES("gffvY66QVQDu30DSg5YY2w==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("YQuw4gsxfmlD1Sjn/SwbHQ==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("oPoiTQoAXay1k+M3xnILwA==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("fKDP810SPwGM2V9fUpBrpg==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("ygL9MF1HlkhP42cea/VUaQ==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("JsbmVwAm6yuD9Fv+Rpkf/w==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("sso5jXNxYYz9i/L+TWUVig==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("GHNRf+g6kHSdK3m+wdZzXg==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("kma6IJfUgXa3Q53qYTJEPg==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("XmIQIv63pPBfZXdAyaEMBA==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("zX8vUJKKnpvw4z4kZ23nTw==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("fyon24vEQF4zPsY27iZlIQ==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("h64DvlVg4HAvyl0ATnqZOw==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("qqX/tZKivFCJAqcXGQYMpg==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("tPyG/A/9M9EUO6UW3XUzdw==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("8WBQgm+Hk51cQeuwJm4Jig==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("tZ17npbUkivDLe9adyskWg==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("ILUUgvWUQOvgUaXQMXdXPQ==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("SIjXqyvuKZ0iyemPlj/GXA==", ConstantConfig.znlj_appkey));
		System.out.println(AESUtil.DeCodeMobileAES("YEt6Udk1pc3S72t/6mTzsQ==", ConstantConfig.znlj_appkey));
		*/
		
		try {
			String str = "15133876743";
			String encryptorStr = encryptor(str, password);
			System.out.println("加密=" + encryptorStr);

			String decryptStr = decrypt("iZjIxkEkKyfUVDtj1ThuGQ==", "bf7a9fd35c51defae2357e7e17ad2c1d");
			System.out.println("解密=" + decryptStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
