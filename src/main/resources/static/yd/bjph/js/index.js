$(function() {
    var reqUrl = "http://www.zpffz.com";
    var msgPuop = $("#msg-puop")[0]; //错误消息提示层
    var $getCode = $(".get-code") // 发送验证码
    var phoneNumber = $("#phone-number")[0]; // 手机输入框
    var vefCode = $("#vef-code")[0]; // 验证码输入框
    var $btnSubmit = $(".btn-sbumit"); // 提交按钮
    var $modal = $(".modal"); // 弹层
    var $closeModal = $(".modal").find(".close"); // 关闭弹层按钮
    var $confirm = $("#confirm");
    var codeResult = {X: 0, y: 0, A: 0};


    var getQuery = function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == variable) {
                return pair[1];
            }
        }
        return false;
    };

    // 表单验证
    function Validator() {
      this.checkInfo = [];
      this.wran = [];
    }

    Validator.prototype.strategies = {
      noEmpty: function (value, error) {
        if (value === "") return error;
        return true;
      },
      isMobile: function (value, error) {
        var phoneReg = /^1[3456789]\d{9}$/;
        if (!phoneReg.test(value)) return error;
        return true;
      },
    };
    Validator.prototype.add = function (dom, showDom, starategyArr) {
        var _this = this;
        this.wran.push(showDom);
        starategyArr.forEach(function (ele) {
            _this.checkInfo.push(function () {
                var arr = ele.strategy.split(':');
                var type = arr.shift();
                arr.unshift(dom.value);
                arr.push(ele.error);
                var msg = _this.strategies[type].apply(_this, arr);
                console.log('msg', msg)
                if(msg !== true) {
                    message.show(showDom, msg);
                }
                return msg;
            })
        })
    };
    Validator.prototype.check = function () {
        var lock = true;
        this.wran.forEach(function(ele) {
            ele.innerText = '';
        })
        for(var i = 0; i< this.checkInfo.length; i++) {
            if(this.checkInfo[i]() !== true) {
                lock = false;
                return lock;
            } 
        }
        return lock;
    };
    // 倒计时
    var time = {
        isNot: false,
        countTimer: 0, 
        countDown: function(t) {
        this.isNot = true;
        this.countTimer = setInterval(function(){
            t--;
            if (t <= 0) {
            this.clearCountTimer();
            } else {
            $getCode.text("重新获取("+ t + ")s");
            }
        }, 1000);
        },
        clearCountTimer: function() {
            this.isNot = false;
            clearInterval(this.countTimer);
            $getCode.text("获取验证码");
        },
    };
    // 消息
    var message = {
      timer: 0,
      show: function (el, msg) {
        $(el).text(msg).fadeIn(200);
        this.hide(el, "");
      },
      hide: function (el, msg) {
        this.timer && clearInterval(this.timer);
        this.timer = setInterval(function (){
          $(el).fadeOut(200, function () {
            $(this).text(msg);
          });
        }, 3000);
      },
    };

    
        // 获取验证码
        $getCode.on("click", function () {
        if (time.isNot) return;
        var req = {
            mobile: phoneNumber.value,
            originUrl: getQuery("originUrl"),
        };
        $.ajax({
            type: "post",
            url: reqUrl + "/api/dq/bjph/sendCode",
            data: req,
        })
            .done(function (res) {
            console.log("res", res);
            if (res.code === 200) {
                time.countDown(60);
                codeResult = res.data;
                message.show(msgPuop, "验证码已下发");
            } else {
                message.show(msgPuop, res.msg);
            }
            })
            .fail(function (err) {
            var res = JSON.parse(err.responseText);
            console.log("err", res);
            message.show(msgPuop, res.msg);
            });
        });

        // 校验输入
        var validator = new Validator();
        $btnSubmit.click(function () {
        validator.add(phoneNumber, msgPuop, [
            {
            strategy: "noEmpty",
            error: "请输入手机号",
            },
            {
            strategy: "isMobile",
            error: "手机号码有误，请重新输入",
            },
        ]);
        validator.add(vefCode, msgPuop, [
            {
            strategy: "noEmpty",
            error: "请输入验证码",
            },
        ]);

        if (validator.check()) {
            $modal.fadeIn(300, function () {
            $("#x").text((codeResult.X || "x") + "元");
            $("#y").text((codeResult.Y || "y") + "元");
            $("#a").text((codeResult.A || "A") + "G");
            });
        }
        });
        
        // 提交数据
        $confirm.on("click", function () {
        var req = {
            mobile: phoneNumber.value,
            validCode: vefCode.value,
            originUrl: getQuery("originUrl"),
        };
        $modal.fadeOut(300);
        $.ajax({
            type: "post",
            url: reqUrl + "/api/dq/bjph/saveOrders",
            data: req,
            success: function (res) {
            console.log("res", res);
            if(res.code === 200) {
	        	countLog.init(function () {
	            }, {
	              isCopy: 0,
	              pageType: 0
	            });
            }
            var msg =
                res.code === 200
                ? "订购成功！会员权益请在北京移动APP内领取"
                : res.msg;
            message.show(msgPuop, msg);
            },
            error: function (err) {
            var res = JSON.parse(err.responseText);
            message.show(msgPuop, res.msg);
            },
        });
        });
	
        // 关闭弹层
        $closeModal.on("click", function () {
        $modal.fadeOut(300);
        });

})