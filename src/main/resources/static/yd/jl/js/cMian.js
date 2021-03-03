/**
 * Created by lushan on 2019/3/30.
 */
Vue.config.productionTip = false;
var vm = new Vue({
  el: '#app',
  data: {
    isshowStatus: false,
    isShowRlt: false,
    selectType: 1,
    isShowTip: false,
    isShowTip1: false,
    isBtnClose: false,
    isSendFri: 0,
    Unum: '',
    verifiyCode: '',
    productId: [
      '',
      'JL22CAZ102484330',
      'JL22CAZ102364350',
    ],
    productName: [
      '',
      '30元移动流量包(营销)',
      '50元移动流量包(营销)',
    ],
    originUrl: '',
    tipMsg: '',
    tipMsg1: '',
    wait: 60
  },
  created () {
    var _self = this;
    _self.originUrl = _self.GetUrlParameter('channel') || '';
  },
  methods: {
    getVerifiyCode () {
      var _self = this;
      var reg = 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/;
      if (this.isSendFri === 0) {
        if (!reg.test(_self.Unum)) {
          _self.isShowTip1 = true;
          _self.tipMsg1 = '请输入正确手机号！';
          return ;
        }
      }
      _self.isshowStatus = true;
      if (this.isSendFri === 0) {
        $.ajax({
          url: 'http://yd.prophetdata.cn/api/dq/jl/sendCode',
          type: "POST",
          dataType: 'json',
          data: {
            mobile: _self.Unum,
            originUrl: _self.originUrl
          },
          success: function (data) {
            if (data.code == 200) {
            } else {
              _self.isShowTip1 = true;
              _self.tipMsg1 = data.msg;
            }
          },
          error:function () {
            window.alert("接口出错了！");
          }
        });
        this.isSendFri = 1;
      }
      if (_self.wait === 0) {
        _self.isshowStatus = false;
        _self.isSendFri = 0;
        _self.wait = 60;
      } else {
        _self.wait--;
        setTimeout(() => {
          _self.getVerifiyCode();
        }, 1000);
      }
    },
    GetUrlParameter (paras) {
      var url = location.href;
      var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
      var paraObj = {}
      for (i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
      }
      var returnValue = paraObj[paras.toLowerCase()];
      if (typeof(returnValue) == "undefined") {
        return "";
      } else {
        return returnValue;
      }
    },
    qdBtn () {
      var _self = this;
      _self.isShowRlt = false;
      _self.isBtnClose = false;
      _self.selectType = window.selectType;
      if (_self.Unum === '') {
        _self.isShowTip1 = true;
        _self.tipMsg1 = '请输入手机号！';
        return ;
      }
      var reg = 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/;
      if (!reg.test(_self.Unum)) {
        _self.isShowTip1 = true;
        _self.tipMsg1 = '请输入正确手机号！';
        return ;
      }
        if (_self.verifiyCode === '') {
        _self.isShowTip1 = true;
        _self.tipMsg1 = '请输入验证码！';
        return ;
      }
      this.isShowTip = true;
      _bxmPlatformFn();
    },
    sunMsg () {
      var _self = this;
      if (_self.isBtnClose) {
        _self.isShowTip = false;
        return ;
      }
      _bxmPlatformFn();
      _self.isShowTip = false;
      $.ajax({
        url: 'http://yd.prophetdata.cn/api/dq/jl/saveOrders',
        type: "POST",
        dataType: 'json',
        data: {
          mobile: _self.Unum,
          originUrl: _self.originUrl,
          validCode: _self.verifiyCode,
          productId: _self.productId[window.selectType],
          productName: _self.productName[window.selectType]
        },
        success: function (data) {
          if (data.code == 200) {
            _self.isShowTip1 = true;
            _self.isShowRlt = true;
            _self.isBtnClose = true;
            _self.tipMsg1 = data.msg
          } else {
            _self.isBtnClose = true;
            _self.isShowTip1 = true;
            _self.isShowRlt = true;
            _self.tipMsg1 = data.msg
          }
        },
        error:function () {
          window.alert("接口出错了！");
        }
      });
    },
    closeTip () {
      this.isShowTip = false;
    },
    closeTip1 () {
      this.isShowTip1 = false;
    }
  }
});

var selectType = 1;

$('#rSelect').on('click', function (e) {
  $('#rSelected').show();
  $('#lSelected').hide();
  $('#lSelect').show();
  $('#botWord').hide();
  $('#botWord_1').show();
  selectType = 2;
});
$('#lSelect').on('click', function (e) {
  $('#lSelect').hide();
  $('#rSelected').hide();
  $('#lSelected').show();
  $('#rSelect').show();
  $('#botWord_1').hide();
  $('#botWord').show();
  selectType = 1;
});

var mask = document.getElementById('mask'); // 弹窗dom对象
mask.addEventListener('touchmove', function(e) {
  e.preventDefault();
}, false);

var mask_1 = document.getElementById('mask_1'); // 弹窗dom对象
mask_1.addEventListener('touchmove', function(e) {
  e.preventDefault();
}, false);
