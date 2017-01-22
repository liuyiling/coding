<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%--
  Created by IntelliJ IDEA.
  Date: 15/11/23
  Time: 上午11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Date date = (Date) request.getAttribute("date");

  String json = (String) request.getAttribute("json");
%>
<html>
<head>
    <title></title>
</head>
<body>
从ForwardServlet中取到的Date为：
<%= new SimpleDateFormat("yyyy-MM-dd").format(date)%>
<%= json%>
</body>
</html>
