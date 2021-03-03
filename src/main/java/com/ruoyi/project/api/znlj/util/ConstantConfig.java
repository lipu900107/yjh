package com.ruoyi.project.api.znlj.util;

public interface ConstantConfig {
	
	static String tokenValidateUrl_old = "http://112.13.96.207:10080/test/api/uniTokenValidate";
	
	static String AES_PWD = "dGRqcqOoYmqjqXh4a2p5eHpyZ3MxNDY1Mjg2MjAyNDcxMTQ2NTI4NjIwMjQ4MQ==";
	
	// 测试环境
	/*
	static String tokenValidateUrl = "http://120.197.235.102/api/uniTokenValidate/";
	static String accessTokenUrl = "https://117.161.4.206:443/simmessageapi/esapi/accessToken";
	static String refreshTokenUrl = "https://117.161.4.206:443/simmessageapi/esapi/refreshToken";
	static String sendAuthMsgUrl = "https://117.161.4.206:443/simmessageapi/esapi/sendAuthMsg";
	
	static String callbackUrl = "http://47.104.171.147/znlj/bussniess/callback";

	static final String znlj_appid = "000340";
	static final String znlj_appkey = "123456";
	
	static final String znlj_rsa_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFc87HsmTttf6IbAtgOlxlmyEYFYtpJL3LASABk7u2Dco9AngJHYoHSjpEn33U/XELBJaxqb07RKKyf7hEQgeaCnYx1VmEKUtmshEh8k1S4yIlqKz9s9qb0/dcCyIvVW10GrXkPpwTkxNThdB+F3JpVFSF0Aah+wofNpYy4SUslQIDAQAB";
	static final String znlj_rsa_privatekey = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAIVzzseyZO21/ohsC2A6XGWbIRgVi2kkvcsBIAGTu7YNyj0CeAkdigdKOkSffdT9cQsElrGpvTtEorJ/uERCB5oKdjHVWYQpS2ayESHyTVLjIiWorP2z2pvT91wLIi9VbXQateQ+nBOTE1OF0H4XcmlUVIXQBqH7Ch82ljLhJSyVAgMBAAECgYAoLhoQHjItMCKWyJEUQ/4VyYNJURNMcPeD667LSsO1qKk/fULO28n3L+4jQzILstMaUiNdEpIbCitCOxor0wWed4MNGDsOOUlWfY6N6EIrpueLHtzdxsDkRpCR6cuuh4o/OOXALusE9kF/9IQ9AnJLJEc/6D9icauTr5/XsFkJ8QJBAM+KAAbQRrGhZqFYyMVGnDUK3w6FXrwg9Ecs0QrHPUbGeLO1GJtFBHLcmv7sduHL01taPA/lF+wNJxBSay1rA5cCQQCknSZmLCjplIS5ayKLoWo5Y3veFgUnlw2IDsd7ETKtVpy0UItvTKEx4M4kIZuzCf8u3bD6G3wIeZq/hJs5qOazAkBB185aWwmSoVomJjzMGbLFQUWzHa0IkovtaNKJUNyn75+ro/DCkgrvRf4Gko7E5B2SBfa4ND56rVGPZBaMuj7RAkAyglm+7fvbuAuFjT77Uxrx4vml6mHIhQvM3KQOufcvwqywkypFi2DGmjEGWx2YMRAQxEtCYt6LBy0ZaMnsRkwLAj8gmMyTOtImJMP8VOrfnwe7y055kzQI9p+ptZ/PBcX9tmWZVM5RzVtewfampD22MjTyxxNQ/Uz2yK+U+LvoEWQ=";
	
	static final String templateId = "";
	*/
	
	// 生产环境
	
	static String tokenValidateUrl = "https://token.cmpassport.com:8300/uniapi/uniTokenValidate";
	
	static String accessTokenUrl = "https://112.13.96.207:10080/simmessageapi/esapi/accessToken";
	static String refreshTokenUrl = "https://112.13.96.207:10080/simmessageapi/esapi/refreshToken";
	static String sendAuthMsgUrl = "https://112.13.96.207:10080/simmessageapi/esapi/sendAuthMsg";
	
	static String callbackUrl = "http://yjh.mmnum.com/znlj/bussniess/callback";

	static final String znlj_appid = "300011976341";
	static final String znlj_appkey = "3F127BC3B1218E77ECF1A9B90963B1F4";
	
	static final String znlj_rsa_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXzQesGNV9q+ZXosF8pUpmnF6g7iBnPWTEZND1hljuSsTtK4KegIyg2r7cyZwSArxaO7mnLkZ61rKZKueCAI0fOsTMWgni0em3BSybjGMb3wb4CpQoRWv4St+pvGaQeufp0PN4fSPLoW28OD6YKQc62891v6wkCMAtEOeB47T7JwIDAQAB";
	static final String znlj_rsa_privatekey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJfNB6wY1X2r5leiwXylSmacXqDuIGc9ZMRk0PWGWO5KxO0rgp6AjKDavtzJnBICvFo7uacuRnrWspkq54IAjR86xMxaCeLR6bcFLJuMYxvfBvgKlChFa/hK36m8ZpB65+nQ83h9I8uhbbw4PpgpBzrbz3W/rCQIwC0Q54HjtPsnAgMBAAECgYAN7OUTSIPtL2PGDkwpnxAgMaAMq0uVrPAdhBIXM+fclEdUanVlDO3Zo7d2kZgbntygenIhgAE/K4reuizapCpN2mhi4ZpWIJdl/vz3Lb9g8ciSeZWO+2DSCS6TxK7zknDTnfWUGQkek3GzcA6azlVxxRpWveP/D9GKeHHu+KUC4QJBAOnFmsaDm6BIhGMLECQXxVALbyQHuYfTcZJ/UeiDtOoTLb8PhI5r3uYBSYERw64YKE2PZDvfM2NyFH7oA6vE7wsCQQCmPB2QPInWRRyPROFOd1aCUFibQ1avS/1aKPJYSLOmcgc8QC6VanFkYA/aAUZjaxC+cqlvleJYofgfsSr9WKXVAkAOtsZYKl54Rzg197NLYkekEyQqs2XFG9TxMJxaMzgG57Agb3ybbkS4W2ph+llDsveOcjEP56uXXc3WcwoQHLQjAkEAig/XhdmhqOHRbHQKo/6dTLGqRZlRv1lfW6gyTnxjKFQAClxL1DCJaJIX2DnC2gMr7uCQNGrJiE9NIhUk3TDo2QJBAMMkJ304dgs1xigNgr/VInDsN/eN9KYEbxPbKSjkJVIk5AlRN2x+1/9dPk85Q9m9VRGdyMp+0Y2/uXt1XHPvYT8=";
	
	static final String templateId = "";
	
	/**
	 * 有数
	 */
	static final String ys_apid = "736692";
	static final String ys_apkey = "AD3691E089EA3367E0533FAE990AC30C";
	static final String ys_app_id = "300011997188";
    static final String ys_app_key = "4215D28F54725F2AC92F74BBAC7C0610";
    static final String client_code = "by-ys1-yjh";
    static final String applykey_url = "https://open.mmarket.com/cap/dataservice/ApplyKey2.proxy";
    static final String opendata_url = "https://open.mmarket.com/cap/dataservice/appDataServiceAuthen.proxy";
    static final String publicKey_1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2FMmfMMs3iV1tpCW5LWy7WnershYTbWyKKVtjVhRk0NXgg509XUtKQaVVxNRunI5hryK6j9fV5c5kjrGJupfI3/8HIvQGCO/zpCJPX9uVDi+uQnrqKhgLFnKRKlHWIhMBrI2C/qaE7p1T5FOgRLnWDi7RIKP/+GgPbpeG1AAi3QIDAQAB";
	
}
