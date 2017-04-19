<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>在Form表单中使用验证码</title>
    <script type="text/javascript">
        //刷新验证码
        function changeImg(){
            document.getElementById("validateCodeImg").src="${pageContext.request.contextPath}/servlet/RandomImageServlet";
        }
    </script>
</head>

<body>
<form action="${pageContext.request.contextPath}/servlet/CheckServlet" method="post">
    验证码：<input type="text" name="validateCode"/>
    <img alt="验证码看不清，换一张" src="${pageContext.request.contextPath}/servlet/RandomImageServlet" id="validateCodeImg" onclick="changeImg()">
    <a href="javascript:void(0)" onclick="changeImg()">看不清，换一张</a>
    <br/>
    <input type="submit" value="提交">
</form>
</body>
</html>