<%--
  Created by IntelliJ IDEA.
  Date: 16/5/20
  Time: 下午4:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String path = request.getContextPath();%>
<%--<%String strContent =--%>
          <%--"Date,Temperature\n" +--%>
          <%--"2008-05-01,75\n" +--%>
          <%--"2008-05-08,70\n" +--%>
          <%--"2008-05-09,80\n";--%>
<%--%>--%>
<html>
<head>
  <script type="text/javascript"
          src="<%=path%>/dygraph-combined-dev.js"></script>
</head>
<body>
<form action="servlet/TestServlet" method="post" dir="ltr">
    <input name="liveId" type="text"><br>
    <input value="查询" type="submit"><br>
</form>

<div id="graphdiv"></div>
<script type="text/javascript">
  g = new Dygraph(

          // containing div
          document.getElementById("graphdiv"),

          // CSV or path to a CSV file.
          "Date,Temperature\n" +
          "2008-05-01,75\n" +
          "2008-05-08,70\n" +
          "2008-05-09,80\n"
          <%--<%=strContent%>--%>

  );
</script>
</body>
</html>
