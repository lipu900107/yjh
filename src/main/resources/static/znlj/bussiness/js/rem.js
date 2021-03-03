(function(doc, win) {
	var docEl = doc.documentElement,
		resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		recalc = function() {
			var clientWidth = docEl.clientWidth;
			if(!clientWidth) return;
			docEl.style.fontSize = (clientWidth >= 720 ? 100 : clientWidth / 3.75) + 'px';
		};
	if(!doc.addEventListener) return;
	win.addEventListener(resizeEvt, recalc, false);
	doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);



function bodyFixed() {
	commonTop = $(window).scrollTop(); //获取页面的scrollTop；
	$('body').css("top", -commonTop + "px"); //给body一个负的top值；
	$('body').addClass('add_position'); //给body增加一个类，position:fixed;
}
function bodyScroll() {
	$('body').removeClass('add_position'); //去掉给body的类
	$(window).scrollTop(commonTop); //设置页面滚动的高度，如果不设置，关闭弹出层时页面会回到顶部。
}
function removeMask() {
	$(this).parents('.mask' + tipsNum).remove();
	bodyScroll();
}

function tips(options, num) {
  //stopBodyScroll();
  tipsNum = num == undefined ? '' : num;
  var buttonTmp;
  var title = options.title == undefined ? '' : options.title;
  if (options.btn2 == undefined) {
    buttonTmp = '<button class="closeBtn' + tipsNum + '">' + options.btn + '</button>';
  } else {
    buttonTmp = '<div class="dbBtnBox">' +
      '<button class="cancelBtn' + tipsNum + '">' + options.btn2 + '</button>' +
      '<button class="closeBtn' + tipsNum + '">' + options.btn + '</button>' +
      '</div>';
  }
  var tmp = '<div class="mask mask' + tipsNum + '">' +
    '<div class="tipsContent">' +
    '<h6> ' + title + ' </h6>' +
    '<p>' + options.msg + '</p>' + buttonTmp +
    '</div>' +
    '</div>';

  $('body').append(tmp);
  $('.closeBtn' + tipsNum).unbind().on('click', options.closeFn == undefined ? removeMask : options.closeFn);
  $('.cancelBtn' + tipsNum).unbind().on('click', options.cancelFn == undefined ? removeMask : options.cancelFn);
}
//手机号校验
var regTel = /^1(3|4|5|6|7|8|9)\d{9}$/;