<%--
  Created by IntelliJ IDEA.
  Date: 15/11/26
  Time: 上午7:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<a href="<%= response.encodeURL("urlRewirteReceive.jsp?username=liuyiling")%>
<%--页面重定向可以这样写--%>
<%
  response.sendRedirect(response.encodeRedirectURL("urlRewirteReceive.jsp?username=liuyiling"));
%>
</body>
</html>
