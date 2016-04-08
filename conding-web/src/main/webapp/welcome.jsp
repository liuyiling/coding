<%--
  Created by IntelliJ IDEA.
  Date: 15/11/26
  Time: 上午7:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String username = (String) session.getAttribute("username");
%>
<html>
<head>
    <title></title>
</head>
<body>
登陆的用户
<%=username%>
</body>
</html>
