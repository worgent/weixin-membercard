<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
  /* out.print(request.getAttribute("pages"));*/
%>
<!DOCTYPE html>
<html>

<head>
  <title></title>
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

</head>
<style type="text/css">

</style>

<body>
<div class="main">


  <div class="tip" id="tip-id" style="font-size: 15px;margin: 0 auto;width: 500px;margin-top: 40px;">
    <span id="time" style="font-size: 20px;font-weight: 800;">6</span>&nbsp;&nbsp;秒钟后自动跳转，如果不跳转，请点击下面的链接
    <a href="/admin/toIndex.do">首页</a>
  </div>
  <%--<div class="bottom" style="height:100px"></div>--%>

</div>

<script type="text/javascript">

  $(function () {
    //alert("ssss");
    countDown("/admin/toIndex.do");
  })

  //注册成功后5秒自动跳转
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



</script>
</body>
</html>