<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    String path = request.getContextPath();

    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    //out.println("上下文路径为："+basePath+"<br>");
%>

<!DOCTYPE html>
<html>

<head>
    <title>台球会员卡管理系统</title>
    <link href="/admin-lib/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/admin-lib/css/style.css">


    <script src="/admin-lib/js/jquery.min.js"></script>
    <script src="/admin-lib/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <script src="/admin-lib/js/bootstrap-datetimepicker.js"></script>
    <script src="/admin-lib/js/default.js"></script>

</head>
<style type="text/css">

</style>

<body id="login">
<div class="main">
        <div class="login-div">
            <form action="/admin/login.do" method="post">
                <div class="input-group login-input">
                    <span class="input-group-addon input-group-lg">用户名:</span>
                    <input type="text" class="form-control" name="userName" placeholder="用户名" value="${userName}">
                </div>
                <div class="input-group login-input">
                    <span class="input-group-addon input-group-lg">密码:</span>
                    <input type="password" class="form-control" name="password" placeholder="密码"  value="${password}">
                </div>
                <div class="button-a login-button">
                    <button type="submit" class="btn btn-default">登录</button>
                    <a href="/admin/toRegisterPage.do"><button type="button" class="btn btn-default">注册</button></a>
                </div>
            </form>
            <span style="color: red">${errMsg}</span>
        </div>

</div>

<script type="text/javascript">
</script>
</body>

</html>