<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Locale" %>
<%--
  Created by IntelliJ IDEA.
  Date: 15/11/22
  Time: 上午9:53
  To change this template use File | Settings | File Templates.
--%>

<%--jsp在运行的时候会被Tomcat自动编译成HttpServlet,所以他也是另外一种形式的Servlet--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="head.jsp"%>
<%--<%@ include file="forward.jsp" %>--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%--可编辑Java代码--%>

<%!
    //声明全局变量，本地变量在\<\%中声明就可以
    private String overallVaribale = "test";

    //声明方法
    private boolean isValid(String ip) {
        return true;
    }
%>


<%
    /**
     * 这里也可以注释,可以编写java代码
     * 这就是jsp脚本
     */
    Locale locale = request.getLocale();
    Calendar calendar = Calendar.getInstance(locale);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);

    String greeting = "";

    if (hour <= 12) {
        greeting = "am";
    } else {
        greeting = "pm";
    }
    System.out.println("jsp脚本运行");



//    return
//    注意使用return语句，在return之后的代码，包括html都不进行输出

%>


<html>
<head>
    <title></title>
    <div>
        <%@ include file="head.jsp"%>
    </div>
</head>
<body>
第一个使用IDEA构建的maven-web


<%--这是jsp的注释,
jsp的输出,如果是对象,调用toString方法--%>
当前显示的是用jsp获得到的时间<%=greeting%>


<%--jsp可以和html完美的结合,if,案例如下--%>
<%
    String param = request.getParameter("param");
    if ("1".equals(param)) {
%>
html结合1
<%
} else if ("2".equals(param)) {
%>
html结合2
<%
    }
%>


<%
    for (int i = 0; i < 5; i++) {
%>
<%--输出某个类的时候，是调用该类的toString方法--%>
<%=i%>
<%
    }
%>




<%-- 使用JavaBean对象的示例代码 --%>
<%--<jsp:useBean id="person" class="model.Person" scope="page"></jsp:useBean>--%>
<%--<jsp:setProperty name="person" property="*"></jsp:setProperty>--%>
<%--<jsp:getProperty name="person" property="name"></jsp:getProperty>--%>

</body>
</html>
