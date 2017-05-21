<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
<%
    String path = request.getContextPath();

    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

<body>
    <div class="main">
        <div class="header col-xs-12">
            <div class="header-userinfo">
                <span>用户名:</span>
                <span><a href="#">${userName}</a></span>
                <a href="/admin/toLogin.do">注销</a>
            </div>

        </div>
        <div class="left col-xs-2">
            <ul class="nav nav-tabs nav-stacked" data-spy="affix" data-offset-top="125" id="nav-id">
                <li><a id="iframe_1" href="#">商户管理</a></li>
                <li><a href="#">充值查询</a></li>
                <li><a href="#">消费查询</a></li>
                <li><a href="#">用户充值统计</a></li>
                <li><a href="#">用户消费统计</a></li>
                <li><a href="#">会员数据统计</a></li>
                <li><a href="#">会员列表</a></li>

            </ul>
        </div>
        <div class="right col-xs-10">

            <div id="iframe-div" role="dialog" tabIndex="-1">
                <input type="hidden" value="${accountId}" id="account-id"/>
                <div class="index-iframe-modal-body"><%--src="/call_back/toAuthorize.do?account=${accountId}&choseType=1"--%>
                    <iframe style="zoom: 0.6;" height="100%" src="/admin/gzhManageSerach.do?account=${accountId}&choseType=1" frameBorder="0" width="100%" id="iframeId"></iframe>
                </div>
            </div>

        </div>

    </div>

    <script type="text/javascript">
    </script>
</body>

</html>