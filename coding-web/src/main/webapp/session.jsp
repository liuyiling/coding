<%--
  Created by IntelliJ IDEA.
  Date: 15/11/26
  Time: 上午7:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  request.setCharacterEncoding("UTF-8");

  if("POST".equals(request.getMethod())){
    if("liuyiling".equals(request.getParameter("username"))){
      session.setAttribute("username","liuyiling");
      response.sendRedirect(request.getContextPath() + "/welcome.jsp");
      return;
    }
  }


%>
<html>
<head>
    <title></title>
</head>
<body>
<form action="session.jsp" method="post">
  <input type="text" name="username">
  <input type="submit" value="session">
</form>
</body>
</html>
