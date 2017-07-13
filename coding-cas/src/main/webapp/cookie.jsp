<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>cookie demo</title>
</head>
<body>
<script type="text/javascript">

	// cookie 增删改查 c1=test1; c2=new; c3=test3
	var cookies = document.cookie;
	console.log(cookies);
	
	document.cookie="c3=test3";
	document.cookie="c2=new";
	// httpOnly 不允许js操作
	docment.cookie="c4=test4;expires=" + [GMT时间] + ";path=/;domain=www.test.com;secure"
	
	var d = new Date().getTime();
	d += 24 * 60 * 60 * 1000;
	
	document.cookie = "c1=test1;expires=" + new Date(d).toGMTString() ;
	document.cookie = "c2=" + escape("内容转码防错误") + "";
	
	// 去空格
	function trim(str) { 
		return str.replace(/(^\s*)|(\s*$)/g, "");
   }
	
	// 获取cookie值    c1=test1; c2=%12%34%56%78%33%aba%20bc escape unescape
	function getCookie(name) {
		var cookies = document.cookie.split(";");
		for (var i = 0, len = cookies.length; i < len; ++i) {
			var cookie = cookies[i].split("=");
			if (name === trim(cookie[0])) {
				return unescape(trim(cookie[1]));
			}
		}
	}
	
	console.log(getCookie("c1"));
	console.log(getCookie("c2"));
	
	// 新增cookie(如果存在同名cookie，起到修改作用)
	// expiresDay为有效天数
	// domain提升到顶级域时，可使用.test.com形式
	// 用js设置cookie，httpOnly属性浏览器支持情况不同，用js设置一般来说也没有意义
	// c1=中文2;expires=3-6 2014GMT;path=/;domain=.test.com;secure
	function addCookie(name, value, expiresDay, path, domain, isSecure) {
		var tmp = name + "=" + escape(value);
		
		if (expiresDay) {
			var d = new Date().getTime();
			d += expiresDay * 24 * 60 * 60 * 1000;
			tmp += ";expires=" + new Date(d).toGMTString();
		} 
		
		if (path) {
			tmp += ";path=" + path;
		}
		
		if (domain) {
			tmp += ";domain=" + domain;
		}
		
		if (isSecure) {
			tmp += ";secure";
		}
		
		console.log(tmp);
		document.cookie = tmp;
	}
	
	
	// 删除cookie，没有真正的删除，只能将cookie过期时间设置为过去时间，当浏览器关闭或重载页面时cookie被清除
	function delCookie(name) {
		document.cookie = name + "=anyval;expires=" + new Date(0).toGMTString();
	}
	
	delCookie("c1");
	console.log(getCookie("c1"));

	delCookie("new1");
	delCookie("new2");
	
	addCookie("new1", "新的一个");
	addCookie("new2", "2new", null, "/path1", null, true);
	
</script>
</body>
</html>