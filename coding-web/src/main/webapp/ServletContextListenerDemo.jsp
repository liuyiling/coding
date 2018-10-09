<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<ul>
    <c:forEach items="${countries}" var="country">
        <li>${country.value}</li>
    </c:forEach>
</ul>
</body>
</html>
