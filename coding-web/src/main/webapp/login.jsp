<%--
  Created by IntelliJ IDEA.
  Date: 15/11/25
  Time: 上午10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" isErrorPage="true" %>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    if ("POST".equals(request.getMethod())) {
//        设置cookie的有效期,正数-几秒后消失，负数-窗口有效，0-删除
//        cookie1.setMaxAge(1000000);
//        设置跨域cookie
//        cookie1.setDomain("http://127.0.0.1");


        Cookie usernameCookie = new Cookie("username", request.getParameter("username"));
        Cookie visittimesCookie = new Cookie("visitTimes", "0");

        response.addCookie(usernameCookie);
        response.addCookie(visittimesCookie);

        response.sendRedirect(request.getContextPath() + "/cookie.jsp");

        return;
    }
%>
<html>
<head>
    <title>请先登录</title>
</head>
<body>
<div align="center" style="margin:10px; ">
    <fieldset>
        <legend>登录</legend>
        <form action="login.jsp" method="post">
            <table>
                <tr>
                    <td>
                    </td>
                    <td>
                        <span style="color:red; "><%= exception.getMessage() %></span>
                    </td>
                </tr>
                <tr>
                    <td>
                        帐号：
                    </td>
                    <td>
                        <input type="text" name="username" style="width:200px; ">
                    </td>
                </tr>
                <tr>
                    <td>
                        密码：
                    </td>
                    <td>
                        <input type="password" name="password" style="width:200px; ">
                    </td>
                </tr>
                <tr>
                    <td>
                    </td>
                    <td>
                        <input type="submit" value=" 登  录 " class="button">
                    </td>
                </tr>
            </table>
        </form>
    </fieldset>
</div>
</form>
</body>
</html>
