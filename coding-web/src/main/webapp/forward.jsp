<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Date date = (Date) request.getAttribute("date");
  String name = (String) request.getAttribute("name");
%>
<html>
<head>
    <title></title>
</head>
<body>
从ForwardServlet中取到的Date为:
<%= new SimpleDateFormat("yyyy-MM-dd").format(date)%>
从ForwardServlet中取到的字符串为:
<%= name%>
</body>
</html>
