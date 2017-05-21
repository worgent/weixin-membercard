<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
   /* out.print(request.getAttribute("pages"));*/
%>
<!DOCTYPE html>
<html>

<head>
    <title>注册内容</title>
    <link href="/admin-lib/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/admin-lib/css/bootstrap-select.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/admin-lib/css/style.css">

    <link rel="stylesheet" href="/admin-lib/css/adminia.css" rel="stylesheet">

    <script src="/admin-lib/js/jquery.min.js"></script>
    <script src="/admin-lib/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <script src="/admin-lib/js/bootstrap-select.js"></script>
    <script src="/admin-lib/js/bootstrap-datetimepicker.js"></script>
    <script src="/admin-lib/js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="/admin-lib/js/default.js"></script>

    <script src="/admin-lib/js/jquery.flot.js"></script>
    <script src="/admin-lib/js/line.js"></script>

</head>
<style type="text/css">

</style>

<body>
<div class="main">

    <div class="content" id="content-id">

        <form class="bs-example bs-example-form my-form" action="" method="post" enctype="multipart/form-data" id="register-formid" style="height: 100%;">
            <div class="part">
                <h3 style="margin: 0;margin-bottom: 10px">注册内容</h3>
                <span class="title-span">基本信息</span>

                <div class="part_0">
                    <div class="register-input-group" id="userNameDiv">
                        <span class="input-group-lg lable"><b style="color:red">*</b>用户名:</span><span>${registerMsg}</span>
                        <input type="text" class="form-control" placeholder="用户名" name="userName" required="true" id="userName" onblur="chickUserName()">
                    </div>

                    <div class="register-input-group">
                        <span class="iinput-group-lg lable"><b style="color:red">*</b>密码:</span>
                        <input type="password" class="form-control" placeholder="密码" name="password" required="true" id="password1"
                               onblur="passWordTwo()">
                    </div>
                    <div class="register-input-group" id="passwordDiv">
                        <span class="input-group-lg lable"><b style="color:red">*</b>确认密码:</span>
                        <input type="password" class="form-control" placeholder="确认密码" required="true" id="password2"
                               onblur="passWordTwo()">

                    </div>
                    <div class="register-input-group" id="telDiv">
                        <span class="input-group-lg lable"><b style="color:red">*</b>手机号码:</span>
                        <input type="text" class="form-control chickInput" placeholder="手机号码" name="phoneNum" required="true" id="tel">
                    </div>
                    <div class="register-input-group" id="emailDiv">
                        <span class="input-group-lg lable"><b style="color:red">*</b>常用邮箱:</span>
                        <input type="text" class="form-control chickInput" placeholder="常用邮箱" name="email" required="true" id="email">

                    </div>
                </div>

                <div class="button-a register-button">
                    <a href="/admin/toLogin.do">
                        <button type="button" class="btn btn-default">返回</button>
                    </a>
                    <button type="button" class="btn btn-default" id="registerButton" onclick="registerAjax()">立即注册</button>
                </div>
            </div>
        </form>
    </div>
    <div class="tip" id="tip-id" style="display:none;font-size: 15px;margin: 0 auto;width: 500px;margin-top: 40px;">
        <span id="time" style="font-size: 20px;font-weight: 800;">6</span>&nbsp;&nbsp;秒钟后自动跳转，如果不跳转，请点击下面的链接
        <a href="/admin/toIndex.do">首页</a>
    </div>
    <%--<div class="bottom" style="height:100px"></div>--%>


</div>

<script type="text/javascript">
    function registerAjax(){
        chickUserName();
        passWordTwo();
        $("#emailMesSpan").css("display", "none");
        $("#telMesSpan").css("display", "none");
        var phone = $.trim($("#tel").val())
        var emailName = $.trim($("#email").val());

        if(phone == ""){
            $("#telDiv").after(
                    "<span style=\"color:red;font-size: 10px\" id='telMesSpan'><b>*</b>手机号码不能为空！</span>"
            )
            return false;
        }
        if(emailName == ""){
            $("#emailDiv").after(
                    "<span style=\"color:red;font-size: 10px\" id='emailMesSpan'><b>*</b>邮箱不能为空！</span>"
            )
            return false;
        }

        $.ajax({
            type: "POST",
            url:'/admin/register.do',
            data:$('#register-formid').serialize(),// 你的formid
            async: false,
            success: function(data) {
                if(data==1){
                    $("#content-id").css("display","none");
                    $("#tip-id").css("display","block");
                    countDown('/admin/toIndex.do');
                }else{
                    alert("请检查填写信息或与管理员联系");
                }
            },
            error: function(request) {
                alert("Connection error");
            }
        });
    }
    //注册成功后3秒自动跳转
    function countDown(url){
            var delay = document.getElementById("time").innerHTML;
            if(delay > 0) {
                delay--;
                document.getElementById("time").innerHTML = delay;
            } else {
                window.top.location.href = url;
            }
            setTimeout("countDown('" + url + "')", 1000);
    }

    function passWordTwo() {
        //alert("ddsd");
        $("#passwordMesSpan").css("display", "none");
        var pwd1 = $("#password1").val();
        var pwd2 = $("#password2").val();
        var pwdL = $("#password1").val().length;
        if(pwd1 == "" || pwd2 == ""){
            $("#passwordDiv").after(
                    "<span style=\"color:red;font-size: 10px\" id='passwordMesSpan'><b>*</b>密码不能为空！</span>"
            )
            return false;
        }
        if (pwd1 != pwd2) {

            $("#passwordDiv").after(
                    "<span style=\"color:red;font-size: 10px\" id='passwordMesSpan'><b>*</b>两次密码不一致！</span>"
            )
            /*disabled="disabled"*/
            return false;
        } else {
            if (pwdL > 16) {
                $("#passwordDiv").after(
                        "<span style=\"color:red;font-size: 10px\" id='passwordMesSpan'><b>*</b>密码长度超出16位！</span>"
                )
                return false;
            }
            $("#passwordMesSpan").css("display", "none");
        }
    }


    function chickUserName(){
        $("#usernameMesSpan").css("display", "none");
        var userNameVal = $("#userName").val();
        if(userNameVal != ""){
            $.ajax({
                url:'/admin/chickRegisterUserName.do',
                data:{userNameVal:userNameVal},
                dataType:'json',
                success: function(data){
                    if(data.length != 0){
                        // alert(data.length);
                        $("#userNameDiv").after(
                                "<span style=\"color:red;font-size: 10px\" id='usernameMesSpan'><b>*</b>用户名已经被注册</span>"
                        )
                        $("#registerButton").attr("disabled", true);
                        return false;
                    }else{
                        $("#userNameDiv").after(
                                "<span style=\"color:green;font-size: 10px\" id='usernameMesSpan'><b>*</b>用户名可以使用</span>"
                        )
                        $("#registerButton").attr("disabled", false);
                    }

                },
                error: function(type){

                    alert('Ajax error!');

                }
            });
        }else{
            $("#userNameDiv").after(
                    "<span style=\"color:red;font-size: 10px\" id='usernameMesSpan'><b>*</b>用户名不能为空</span>"
            )
            $("#registerButton").attr("disabled", true);
            return false;
        }

    }

</script>
</body>
</html>