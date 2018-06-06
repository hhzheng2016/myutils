<%--
  Created by IntelliJ IDEA.
  User: zhenghaohao
  Date: 2018/5/24
  Time: 9:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>学生列表</title>
</head>

<body>

<form action="${pageContext.request.contextPath }/search.do" method="post">
    查询条件：
    <table width="100%" border=1>
        <tr>
            <td>姓名:<input type="text" name="student.name"/></td>
            <td>专业:<input type="text" name="student.price"/></td>
            <td><input type="submit" value="查询"/></td>
        </tr>
    </table>
    <table width="100%" border="1">
        <tr>
            <td>ID</td>
            <td>姓名</td>
            <td>生日</td>
            <td>专业</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${studentList}" var="student">
            <tr>
                <td>${student.id}</td>
                <td>${student.name}</td>
                <td><fmt:formatDate value="${student.birthday}" pattern="yyyy-MM-dd"/> </td>
                <td>${student.major}</td>
                <td><a href="${pageContext.request.contextPath}/student/edit.do?id=${student.id}">修改信息</a>
                    ||<a href="${pageContext.request.contextPath}/student/delete.do?id=${student.id}">删除记录</a></td>
            </tr>
        </c:forEach>
    </table>
</form>
<a href="${pageContext.request.contextPath}/student/register.do">添加学生</a>
<a href="${pageContext.request.contextPath}/student/export.do">导出excel</a>
</body>
</html>
