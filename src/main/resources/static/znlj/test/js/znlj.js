//服务协议内容展示
var reqUrl = "yjh.mmnum.com";
var znljAppId = "300011976341";
var appType = "";

var regsign = YDRZ.getSign("300011976341", "1.0");
var data = {
	sign: regsign
}
$.ajax({
    type: 'post',
    url: "http://" + reqUrl + "/znlj/bussniess/getSign",
    data: data,
    headers: {
        "appid": znljAppId
    },
    dataType: 'json',
    success:function(res){
        if(res.code == '200'){
        	var encrySign = res.data.sign;
        	
        	YDRZ.getTokenInfo({
            	data:{
            		version: '1.0',
            		appId: "300011976341",
            		sign: encrySign,
            		openType: '1',
            		expandParams: "",
            		isTest: ""
            	},
            	success:function(res){
            		var regToken = res.token;
            		var regUserInfomation = res.userInformation;
            		
            		alert("getTokenInfo:" + res.message);
            		
            		var data = {
            			apptype: appType,
            			token: regToken,
            			userInfomation: regUserInfomation
        		    }
            		
            		var result = "";
            		$.ajax({
            	        type: 'post',
            	        url: "http://" + reqUrl + "/znlj/bussniess/getUserInfo",
            	        data: data,
            	        headers: {
            	            "appid": znljAppId
            	        },
            	        dataType: 'json',
            	        success:function(res){
            	            if(res.code == '200'){
            	            	result = res.data.mobile;
            	            	alert("result:" + result);
            	            }else{
            	            	alert("getUserInfo:" + res.msg);
            	            }
            	        },
            	        error:function(res){
            	        	alert("getUserInfo:请检查网络");
            	        }
            	    })
            	},
            	error:function(res){	//错误回调
            		console.log("getTokenInfo:" + res.message);
            	}
            });
        }
    },
    error:function(res){
    	alert("getSign:请检查网络");
    }
});
	
	