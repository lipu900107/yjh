//服务协议内容展示
var reqUrl = "yjh.mmnum.com";
var znljAppId = "300011976341";
var encrySign = "";

$('.login').on('click', function () {
	var regsign = YDRZ.getSign("300011976341", "1.0");
	console.log("regsign:" + regsign);
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
            	encrySign = res.data.sign;
            	console.log("encrySign:" + JSON.stringify(encrySign));
            	
                bodyFixed();
                $('.serviceRuleBox').show();
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
});

var getMobile = {
	type: 'post',
    url: "http://" + reqUrl + "/znlj/bussniess/getAuthMobile",
    headers: {
        "appid": znljAppId
    },
    dataType: 'json',
    success:function(res){
        if(res.code == '200'){
    		$(".mobiles").text(res.data.mobile);
    	    $(".quit").show().siblings("button").hide()
        }else{
        	return tips({
                msg: res.msg,
                btn: '确定'
            })
        }
    }
};
function change() {
    if (!$('.service>div').hasClass('disagree') && regTel.test($('.changeIpt1').val())) {
        $('.commonBtn').removeClass('disabled').removeAttr("disabled");
    } else {
        $('.commonBtn').addClass('disabled').attr("disabled", "disabled");
    }
}
// 增加查看服务协议功能，服务协议勾线 按钮禁用等功能
$('.service>div').on('click', function () {
    $(this).attr('class', $(this).hasClass('agree') ? 'disagree' : 'agree');
    change();
});
$(".switch1").on("click", function () {
    $(".chooseLogin").show()
    $(".nowLogin").hide()
})
$(".switch2").on("click", function () {
    $(".chooseLogin").hide()
    $(".nowLogin").show()
})
$(".quit").on("click",function(){
    getCode()
})
//用输入的手机号登录
$(".agreeBtn").on("click", function () {
    mobile = $('.changeIpt1').val();
    if (mobile == '') {
        return tips({
            msg: '请输入手机号码',
            btn: '确定'
        });
    }
    if (!regTel.test(mobile)) {
        return tips({
            msg: '请输入正确的手机号码',
            btn: '确定'
        });
    }
    
    $(".serviceRuleBox").fadeOut(100);
    $(".tips").fadeIn(500).fadeOut(200);
    
    var data = {
		mobile: "dMdk6FlKn12u8B8nphm9J1kMLY5B/cGJdR6HNClctcgE99/7Suec3/Up4gA4iIXtds2H9kSpm0oBQkfKOBFhsOW8q2dJb6USEv8uXNFQ9c6igs7IZb0gwS5Sb9F3YQCVTpw6Mo85D0n1SKSSRfzwOzbYSD67iZtMy4DlRyurX0k="
    }
    $.ajax({
        type: 'post',
        url: "http://" + reqUrl + "/znlj/bussniess/sendAuthMsg",
        data: data,
        headers: {
            "appid": znljAppId
        },
        dataType: 'json',
        success:function(res){
            if(res.code == '200'){
            	window.setInterval(function(){$.ajax(getMobile)},2000);
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

})
//使用本机号码登录
$(".thisNumBtn").on("click", function () {
    if (!$('.service>div').hasClass('disagree')) {
        mobile = '本机号码';
    } else {
        $(".serviceRuleBox").hide()
        return tips({
            title: '提示',
            msg: '请勾选服务协议',
            btn: '确定',
            closeFn: function () {
                $(".mask").hide()
                $(".serviceRuleBox").show()
            }
        });
    }
    $(".serviceRuleBox").fadeOut(100)
    $(".tips").fadeIn(500).fadeOut(200);
    
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
})
//校验手机号
// function chooseActivity() {
//     $('.mask3').show();
//     $.ajax({
//         type: "get",
//         url: '',
//         data: {
//             mobile: mobile
//         },
//         dataType: 'json',
//         success: (function (res) {
//             $('.mask3').hide();
//             // if (res.resCode != '200') {
//             //   return tips({
//             //     msg: res.resDesc,
//             //     btn: '确定',
//             //     closeFn: getCode
//             //   });
//             // }
//             $(".serviceRuleBox").hide()
//             tips({
//                 title: '提示',
//                 msg: '您输入的手机号暂时无法登录',
//                 btn: '确定',
//                 closeFn: getCode
//             });
//             stopBodyScroll();
//         }),
//         error: (function (res) {
//             $('.mask3').hide();
//             tips({
//                 title: '提示',
//                 msg: '您的手机号暂时无法登录',
//                 btn: '确定',
//                 closeFn: getCode
//             });
//         })
//     });
// }
