<%--
  Created by IntelliJ IDEA.
  User: zhenghaohao
  Date: 2018/5/24
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>修改信息</title>
</head>
<body>

<form id="studentForm" action="${pageContext.request.contextPath}/student/update_student.do" method="post">
    <input type="hidden" name="id" value="${student.id }"/> 修改学生信息：

    <table width="100%" border="1">
        <tr>
            <td>学生姓名</td>
            <td><input type="text" name="name" value="${student.name}"></td>
        </tr>
        <tr>
            <td>生日</td>
            <td><input type="text" name="birthday" value="<fmt:formatDate value="${student.birthday}" pattern="yyyy-MM-dd"/>"></td>
        </tr>
        <tr>
            <td>学生专业</td>
            <td><input type="text" name="major" value="${student.major}"></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
