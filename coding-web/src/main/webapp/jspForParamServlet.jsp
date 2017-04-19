<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>request接收中文参数乱码问题</title>
</head>

<body>
<form action="${pageContext.request.contextPath}/servlet/ParamServlet" method="post">
    var1：<input type="text" name="var1"/>
    var2：<input type="text" name="var2"/>
    <input type="submit" value="post方式提交表单">
</form>

<form action="${pageContext.request.contextPath}/servlet/ParamServlet" method="get">
    姓名：<input type="text" name="queryParameter"/>
    <input type="submit" value="get方式提交表单">
</form>
</body>
</html>