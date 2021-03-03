package com.ruoyi.project.common.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import com.ruoyi.project.api.znlj.util.ConstantConfig;


/** */
/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class RSAUtils extends RsaUtil {

	/** */
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/** */
	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/** */
	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/** */
	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/** */
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/** */
	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/** */
	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data
	 *            待加签preSign
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = decryptBASE64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return encryptBASE64(signature.sign());
	}

	/** */
	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data
	 *            待加签preSign
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @param sign
	 *            数字签名
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = decryptBASE64(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(decryptBASE64(sign));
	}

	/** */
	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = decryptBASE64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = decryptBASE64(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data
	 *            源数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = decryptBASE64(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data
	 *            源数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = decryptBASE64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap
	 *            密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/** */
	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap
	 *            密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * java端公钥加密
	 * 
	 * @throws Exception
	 */
	public static String encryptedData(String data, String PUBLICKEY) throws Exception {
		data = encryptBASE64(encryptByPublicKey(data.getBytes(), PUBLICKEY));
		return data;
	}

	/**
	 * java端私钥解密
	 * 
	 * @throws Exception
	 */
	public static String decryptData(String data, String PRIVATEKEY) throws Exception {
		String temp = "";
		byte[] rs = decryptBASE64(data);
		temp = new String(RSAUtils.decryptByPrivateKey(rs, PRIVATEKEY), "UTF-8"); // 以utf-8的方式生成字符串
		return temp;
	}

	public static void main(String[] args) throws Exception {
		/*String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFc87HsmTttf6IbAtgOlxlmyEYFYtpJL3LASABk7u2Dco9AngJHYoHSjpEn33U/XELBJaxqb07RKKyf7hEQgeaCnYx1VmEKUtmshEh8k1S4yIlqKz9s9qb0/dcCyIvVW10GrXkPpwTkxNThdB+F3JpVFSF0Aah+wofNpYy4SUslQIDAQAB";
		String privateKey = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAIVzzseyZO21/ohsC2A6XGWbIRgVi2kkvcsBIAGTu7YNyj0CeAkdigdKOkSffdT9cQsElrGpvTtEorJ/uERCB5oKdjHVWYQpS2ayESHyTVLjIiWorP2z2pvT91wLIi9VbXQateQ+nBOTE1OF0H4XcmlUVIXQBqH7Ch82ljLhJSyVAgMBAAECgYAoLhoQHjItMCKWyJEUQ/4VyYNJURNMcPeD667LSsO1qKk/fULO28n3L+4jQzILstMaUiNdEpIbCitCOxor0wWed4MNGDsOOUlWfY6N6EIrpueLHtzdxsDkRpCR6cuuh4o/OOXALusE9kF/9IQ9AnJLJEc/6D9icauTr5/XsFkJ8QJBAM+KAAbQRrGhZqFYyMVGnDUK3w6FXrwg9Ecs0QrHPUbGeLO1GJtFBHLcmv7sduHL01taPA/lF+wNJxBSay1rA5cCQQCknSZmLCjplIS5ayKLoWo5Y3veFgUnlw2IDsd7ETKtVpy0UItvTKEx4M4kIZuzCf8u3bD6G3wIeZq/hJs5qOazAkBB185aWwmSoVomJjzMGbLFQUWzHa0IkovtaNKJUNyn75+ro/DCkgrvRf4Gko7E5B2SBfa4ND56rVGPZBaMuj7RAkAyglm+7fvbuAuFjT77Uxrx4vml6mHIhQvM3KQOufcvwqywkypFi2DGmjEGWx2YMRAQxEtCYt6LBy0ZaMnsRkwLAj8gmMyTOtImJMP8VOrfnwe7y055kzQI9p+ptZ/PBcX9tmWZVM5RzVtewfampD22MjTyxxNQ/Uz2yK+U+LvoEWQ=";
		*/
		
		String mobile = "13999999999";
		System.out.println("mobile:" + mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
		
		System.out.println("mobile:" + AESUtil.aesEncrypt(mobile, ConstantConfig.znlj_appkey));
		System.out.println("mobile:" + AESUtil.aesDecrypt(AESUtil.aesEncrypt(mobile, ConstantConfig.znlj_appkey), ConstantConfig.znlj_appkey));
		
		/*String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXzQesGNV9q+ZXosF8pUpmnF6g7iBnPWTEZND1hljuSsTtK4KegIyg2r7cyZwSArxaO7mnLkZ61rKZKueCAI0fOsTMWgni0em3BSybjGMb3wb4CpQoRWv4St+pvGaQeufp0PN4fSPLoW28OD6YKQc62891v6wkCMAtEOeB47T7JwIDAQAB";
		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJfNB6wY1X2r5leiwXylSmacXqDuIGc9ZMRk0PWGWO5KxO0rgp6AjKDavtzJnBICvFo7uacuRnrWspkq54IAjR86xMxaCeLR6bcFLJuMYxvfBvgKlChFa/hK36m8ZpB65+nQ83h9I8uhbbw4PpgpBzrbz3W/rCQIwC0Q54HjtPsnAgMBAAECgYAN7OUTSIPtL2PGDkwpnxAgMaAMq0uVrPAdhBIXM+fclEdUanVlDO3Zo7d2kZgbntygenIhgAE/K4reuizapCpN2mhi4ZpWIJdl/vz3Lb9g8ciSeZWO+2DSCS6TxK7zknDTnfWUGQkek3GzcA6azlVxxRpWveP/D9GKeHHu+KUC4QJBAOnFmsaDm6BIhGMLECQXxVALbyQHuYfTcZJ/UeiDtOoTLb8PhI5r3uYBSYERw64YKE2PZDvfM2NyFH7oA6vE7wsCQQCmPB2QPInWRRyPROFOd1aCUFibQ1avS/1aKPJYSLOmcgc8QC6VanFkYA/aAUZjaxC+cqlvleJYofgfsSr9WKXVAkAOtsZYKl54Rzg197NLYkekEyQqs2XFG9TxMJxaMzgG57Agb3ybbkS4W2ph+llDsveOcjEP56uXXc3WcwoQHLQjAkEAig/XhdmhqOHRbHQKo/6dTLGqRZlRv1lfW6gyTnxjKFQAClxL1DCJaJIX2DnC2gMr7uCQNGrJiE9NIhUk3TDo2QJBAMMkJ304dgs1xigNgr/VInDsN/eN9KYEbxPbKSjkJVIk5AlRN2x+1/9dPk85Q9m9VRGdyMp+0Y2/uXt1XHPvYT8=";
		
		String date1 = encryptedData("13999999999", publicKey);// 公钥加密密钥A，此处为55555
		System.out.println(date1);
		String date2 = decryptData(date1, privateKey);
		System.out.println(date2);
		System.out.println("----");*/
		
		/*String mobile = AESUtil.DeCodeMobileAES("xCkyapFFttH3O+8+eXXPmQ==", ConstantConfig.znlj_appkey);
		System.out.println("mobile:" + mobile);
		System.out.println("----");*/
		
		/*
		try {
			String verifySign = "Ok5P6uQ6c5M8xGY8k9uG7ZWz2F0z5I6U9UwvK3CdYNeKxnH5Zv/nRC1yCg0/hr5oK0ljTnGhDTpr0jEdpVHe8rKeJbEeXS8GuilpFCFGiO5HN0BpZzI4hMgmpZhap1P0EGih0pDr61Z0t4tYyqT/6DdMY36qrq1D9HsBIHqLoQ0=";
			String sign = "000340GLIAJ7F52PL6KDF92HJKFKHG69HGN4KI20200602141912136J8H182HO958S8E84IM73VE118CC3JCGL1.2";
			boolean flag = verify(sign.getBytes("UTF-8"), publicKey, verifySign);
			System.out.println("flag:" + flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/**
		 * 生成密钥对
		Map<String,Object> keyMap = genKeyPair();
		String publicKey = getPublicKey(keyMap);
		String privateKey = getPrivateKey(keyMap);
		System.out.println("publicKey:" + publicKey);
		System.out.println("privateKey:" + privateKey);
		System.out.println("--------");
		 */
		
		/**
		 * 使用随机数生成appid(6位)
		Random random = new Random();  
		int randomNum = Math.abs(random.nextInt());
		randomNum = randomNum % (999999 - 100000 + 1) + 100000;
		System.out.println("randomNum:" + randomNum);
		System.out.println("--------");
		 */
		
		
		/**
		 * 使用UUID生成appkey
		String randomKey = ToolUtils.getUUID();
		System.out.println("randomKey:" + randomKey);
		System.out.println("--------");
		 */
	}

}