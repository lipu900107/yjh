if (getReferer() == 'https://yjh.mmnum.com/znlj/bussiness/index_six.html') {
//服务协议内容展示
var reqUrl = "yjh.mmnum.com";
var znljAppId = storage.getItem('appid');
var aOld = storage.getItem('aOld');
var appType = "";

var regsign = YDRZ.getSign("300011976341", "1.0");
var data = {
	sign: regsign
}
$.ajax({
    type: 'post',
    url: "https://" + reqUrl + "/znlj/bussniess/getSign",
    async: false,
    data: data,
    headers: {
        "appid": znljAppId,
        "aOld": aOld
    },
    dataType: 'json',
    success:function(res){
        if(res.code == '200'){
        	var encrySign = res.data.sign;
        	
        	YDRZ.getTokenInfo({
            	data:{
            		version: '1.0', 	//接口版本号 （必填）
            		appId: "300011976341", 	//应用Id （必填）
            		sign: encrySign,	//RSA加密后的sign（必填）
            		openType: '1', 		//统一填写1（必填）
            		expandParams: "",
            		isTest: ""			//是否启用测试线地址（传0时为启用不为0或者不传时为不启用）
            	},
            	success:function(res){	//成功回调
            		var regToken = res.token;
            		var regUserInfomation = res.userInformation;
            		
            		if("000000" != res.code) {
            			//console.log("getTokenInfo:" + res.message);
            		}
            		
            		var data = {
            			apptype: 5,
            			token: regToken,
            			userInfomation: regUserInfomation
        		    }
            		
            		var result = "";
            		$.ajax({
            	        type: 'post',
            	        url: "https://" + reqUrl + "/znlj/bussniess/getUserInfo",
            	        data: data,
            	        headers: {
            	            "appid": znljAppId,
            	            "aOld": aOld
            	        },
            	        dataType: 'json',
            	        success:function(res){
            	            if(res.code == '200'){
            	            	result = res.data.mobile;
                    			//console.log("getTokenInfo:" + result);
            	            }else{
                    			//console.log("getTokenInfo:" + res.msg);
            	            }
            	        },
            	        error:function(res){
                			console.log("getTokenInfo:请检查网络");
            	        }
            	    })
            	},
            	error:function(res){	//错误回调
        			// console.log("getTokenInfo:" + res.message);
            	}
            });
        } else {
        	// console.log("getSign:" + res.msg);
        }
    },
    error:function(res){
		console.log("getSign:请检查网络");
    }
});
}
	
	