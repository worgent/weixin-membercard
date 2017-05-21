<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" Content="text/xml;charset=utf-8">
    <meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <title>礼品卷</title>
    <!---->
    <link rel="stylesheet" href="/lib/css/footer.css">
    <link rel="stylesheet" href="/lib/css/style.css">
    <!-- <script src="/lib/js/jquery.min.js"></script>
    <script src="/lib/js/jquery.mobile.min.js"></script>-->
</head>

<body id="gift">
    <script>
        function changeStyle(inputId) {
            var yesUsed = document.getElementById("yesUse");
            var noUsed = document.getElementById("noUse");
            if (inputId == "yes") {

                noUsed.style.color = "deepskyblue"
                noUsed.style.backgroundColor = "white";
                yesUsed.style.color = "white";
                yesUsed.style.backgroundColor = "deepskyblue";
            } else {
                noUsed.style.color = "white"
                noUsed.style.backgroundColor = "deepskyblue";
                yesUsed.style.color = "deepskyblue";
                yesUsed.style.backgroundColor = "white";
            }

        }
    </script>
    <div class="homePage">
        <div class="top">
            <input class="no" id="noUse" type="button" value="未使用" onclick="changeStyle('no')">
            <input class="yes" id="yesUse" type="button" value="已使用" onclick="changeStyle('yes')">
        </div>
        <div class="content"></div>

    </div>
    <div class="homePage_2">
        <img src="/lib/img/lipinjuan.png" alt="">
    </div>
    <%@ include file="common/footer.jsp"%>

</body>

</html>