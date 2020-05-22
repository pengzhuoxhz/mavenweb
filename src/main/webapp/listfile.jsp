<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>下载文件显示页面</title>
</head>

<body>
<!-- 遍历Map集合 -->
<c:forEach var="me" items="${fileNameMap}">
    <c:url value="/download" var="downurl">
        <c:param name="filename" value="${me.key}"></c:param>
    </c:url>
    <a href="${downurl}">${me.value}</a>
    <br/>
</c:forEach>
</body>
</html>