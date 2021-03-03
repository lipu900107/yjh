$(function() {
    var arrinfo = ["谢谢参与"];
    var count = 3;

    // init
    complete().then(eraser);
    
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
                			console.log(res.message);
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
                	        		$(".user-phone").text(result);
                	            }else{
                        			console.log(res.msg);
                	            }
                	        },
                	        error:function(res){
                    			console.log("请检查网络");
                	        }
                	    })
                	},
                	error:function(res){
            			console.log(res.message);
                	}
                });
            }
        },
        error:function(res){
			console.log("请检查网络");
        }
    });
    
    function eraser() {
        console.log('count', count)
        if (count <= 0) {
            $("#layer").hide();
            $("#reset").hide();
            return;
        }
        $("#layer").eraser({
            size: 100, //设置橡皮擦大小
            completeRatio: 0.5, //设置擦除面积比例
            completeFunction: function() {
                if (count > 0) {
                    complete().then(function() {
                        $("#reset").show();
                    })
                }
            }, //大于擦除面积比例触发函数
        });
    }

    // 计算结果
    function complete() {
        var prize = getPrize(); 
        return prize.then(function (res) {
            if(res.code === 200) {
                var resultMsg = (res.data && res.data.desc) || arrinfo[0];
                count = (res.data && res.data.num) || 0;
               
                $("#result").text(resultMsg);
                $(".count").html("您还有" + count + "次刮奖机会");
            }
        });
    }
    
    // 奖品结果
    function getPrize() {
        return $.ajax({
          type: "post",
          url: "http://yjh.mmnum.com/znlj/bussniess/getPrize",
          dataType: "json",
        });
    }
    // 
    $("#reset").click(function() {
        location.reload();
    })

    var modalText = [
      {
        title: "活动规则",
        content:
          " <p>一、7月3日-12月30日，用户进入活动页面，刮取奖品，有机会获得好礼。</p> <p>二、每位用户每天有1次参与机会。</p> <p>三、奖品包括话费劵（最高188元）；2G流量日包6折劵；5GB流量日包7折；100M流量日包赠送劵；500M流量日包赠送劵；1GB流量日包。</p>",
      },
      {
        title: "我的奖品",
        content: '<p class="tips">您还未中奖！</p>',
      },
    ];

    // 活动规则
    $("#btn1").click(function () {
        showModal(modalText[0]);
    });
    // 我的奖品
    $("#btn2").click(function () {
        showModal(modalText[1]);
    });

    function showModal(context) {
        var $modal = $(".modal");
        $modal.find(".title").html(context.title);
        $modal.find(".modal-body").html(context.content);
        $modal.fadeIn();
    }

    var $closeBtn = $(".modal").find(".close-btn");
    $closeBtn.click(function () {
        $(".modal").fadeOut();
    });
});