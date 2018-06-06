<%--
  Created by IntelliJ IDEA.
  User: zhenghaohao
  Date: 2018/5/28
  Time: 9:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form id="studentForm" action="${pageContext.request.contextPath}/student/add_student.do" method="post">
    添加学生：
    <table width="100%" border="1">
        <tr>
            <td>学生姓名</td>
            <td><input type="text" name="name" ></td>
        </tr>
        <tr>
            <td>生日</td>
            <td><input type="text" name="birthday" ></td>
        </tr>
        <tr>
            <td>学生专业</td>
            <td><input type="text" name="major" ></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
