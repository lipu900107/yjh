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
            bodyFixed();
        	
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
            			return tips({
                            msg: res.message,
                            btn: '确定'
                        });
            		}
            		
            		var data = {
            			apptype: 5,
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
            	        		alert("登录成功");
            	        		$(".mobiles").text(result);
            	        	    $(".quit").show().siblings("button").hide()
            	            }else{
            	            	return tips({
            	                    msg: res.msg,
            	                    btn: '确定'
            	                })
            	            }
            	        },
            	        error:function(res){
            	        	return tips({
            	                msg: "请检查网络",
            	                btn: '确定'
            	            })
            	        }
            	    })
            	},
            	error:function(res){	//错误回调
            		return tips({
                        msg: res.message,
                        btn: '确定'
                    });
            	}
            });
        }
    },
    error:function(res){
    	return tips({
            msg: "请检查网络",
            btn: '确定'
        })
    }
});
	
	