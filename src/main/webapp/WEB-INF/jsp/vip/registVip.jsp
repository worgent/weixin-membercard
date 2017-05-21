<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/9
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" Content="text/xml;charset=utf-8">
    <meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <title>台球会员卡</title>
    <script type="text/javascript" src="/js/jquery-1.8.1.min.js"></script>
    <script type="text/javascript" src="/js/checked.js"></script>

    <link rel="stylesheet" href="/css/member-data.css">
</head>

<body>
<form method="post" action=""  id="registerForm"  name="registerForm" >
    <div class="mainDIV">
        <div class="box name">
            <label for="fname">姓名:</label>
            <input type="text" name="fname" id="fname" placeholder="请输入姓名">
        </div>
        <div class="box phone">
            <label for="lname">手机:</label>
            <input type="text" name="lname" id="lname" placeholder="请输入手机号">
        </div>
        <div class="box gender">
            <label for="sex">性别</label>
            <select name="sex" id="sex">
                <option value="0">请选择性别</option>
                <option value="1">男</option>
                <option value="2">女</option>
            </select>
        </div>
        <div class="box birthday">
            <label for="bday">生日：</label>
            <input type="date" name="bday" id="bday">
        </div>
        <div class="box chacked">
            <input type="text" id="verfication_code" name="verfication_code" placeholder="输入验证码">
            <input type="button" id="" value="获取验证码"
                   onclick="getReceiveMemberCardVCode(this,event,'registerForm', 'lname', 'registVipCode')">
            <!--<img src="/lib/img/1.png" alt="">-->
        </div>
        <input type="button" name=""  onclick="toRegister()" value="提交">

    </div>
</form>
<script type="text/javascript">
    var intervalId,buttonObj;
    //发送下一条短信需要间隔的秒数
    var seconds = 60;
    function getReceiveMemberCardVCode(clickObj, evt, formId, teleName,ch_type){
        var form = document.getElementById(formId);
        var req = {
            phone: $.trim(form[teleName].value),
            business_id:${businessID},
            type:ch_type
        }
        if(!req.phone){
            alert("请输入手机号", 1000);return;
        }
        clickObj.setAttribute("disabled", "disabled");
        clickObj.value = "正在发送，请稍候...";
        $.ajax({
            url: "/check_code/getSMsCode.do",
            type:"post",
            data:req,
            dataType:"JSON",
            success: function(res){
                if(0 == res.errno){
                    clickObj.value = '验证码发送成功';
                    buttonObj = clickObj;
                    intervalId = setInterval("ticker()",1000);
                }else{
                    alert(res.error, 1500);
                    return false;
                }
            }
        });
    }
    function ticker(){
        seconds --;
        if(seconds > 55){
            //提示消息显示5秒钟
        }else if(seconds>0){
            buttonObj.value = seconds+"秒后可重新获取";
        }else{
            clearInterval(intervalId);
            buttonObj.removeAttribute("disabled");
            buttonObj.value = "获取验证码";
            seconds = 60;
            buttonObj = null;
        }
    }
</script>

<script>

    function registCheck(){
        //姓名
        var nameVal = $("#fname").val();
        if( nameVal == "" ){
            alert("请输入姓名");
            return false;
        }
        //手机
        var telVal = $("#lname").val();
        var isTel = isMobil(telVal);
        if(isTel==false){
            alert("请输入正确的手机号");
            return false;
        }
        //性别
        var sexVal = $("#sex").val();
        if( sexVal == 0 ){
            alert("请选择性别");
            return false;
        }
        //生日
        var bdayVal = $("#bday").val();
        if( bdayVal == "" ){
            alert("请选择生日");
            return false;
        }
        return true;
    }
    //注册完成方法
    function toRegister(){
        //alert("toRegister");
        if( registCheck() == false ) return;

        jQuery.ajax({
            url: "/vip/ajaxRegist.do?business_id=${businessID}&open_id=${openID}",
            type: "post",
            dataType: "json",
            cache: false,
            async: false,
            data:$('#registerForm').serialize(),
            success: function(ret){
                if(ret.errno == 0){
                    alert("注册成功");
                    location.href = "/vip/vipHomePage.do?business_id=${businessID}&member_id="+ret.member_id;
                }else{
                    alert(ret.error);
                }
            },
            error:function(){
                alert("error");
            }
        })
    }
</script>
</body>

</html>
