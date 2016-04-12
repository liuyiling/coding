<%--
  Created by IntelliJ IDEA.
  Date: 15/11/26
  Time: 上午7:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String username = request.getParameter("username");
%>
<html>
<head>
    <title></title>
</head>
<body>
获取到的参数：<%= username%>
</body>
</html>
