<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html >
<html>
<head>
<meta charset="utf-8">
<title>cookie session demo</title>
</head>
<body>

<%
	String what = (String)session.getAttribute("what");
	String resp = null;
    if( "铅笔".equals(what)) {
    	resp = "$1";
    } else {
    	resp = "I don't know";
    }
    
    out.print(resp);
%>

</body>
</html>