<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" Content="text双击查看原图ml;charset=utf-8">
    <meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <title>台球会员卡</title>
    <!---->
    <!--<link rel="stylesheet" href="/lib/css/jquery.mobile.min.css">-->
    <link rel="stylesheet" href="/lib/css/member-data.css">
    <!-- <script src="/lib/js/jquery.min.js"></script>
    <script src="/lib/js/jquery.mobile.min.js"></script>-->
</head>

<body>
    <form method="post" action="">
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
                    <input type="text" placeholder="输入验证码">
                    <input type="button" value="获取验证码">
                    <!--<img src="/lib/img/1.png" alt="">-->
            </div>
            <input type="submit" name="" id="" value="提交">

        </div>
    </form>
</body>

</html>