<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	session.setAttribute("what", "铅笔");
%>

<!DOCTYPE html >
<html>
<head>
<meta charset="utf-8">
<title>cookie session demo</title>
</head>
<body>
Response：有！
<a href="how-much.jsp">多少钱一支？</a>
</body>
</html>