<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/9
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
<html>
<head>
  <meta http-equiv="Content-Type" Content="text/html;charset=utf-8">
  <meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
  <meta content="telephone=no" name="format-detection" />
  <meta content="email=no" name="format-detection" />
  <link rel="stylesheet" type="text/css" href="/css/lixue.css"/>

</head>
<body>
<div class="main">
  <div class="banner">
    <img src="/images/one-banner.png"/>
  </div>
  <a class="button" href="/vip/registVip.do?business_id=${businessID}&open_id=${openID}">点击领取</a>
  <ul>
    <li class="a1">${vipBusiness.tel}</li>
    <li class="a2">${vipBusiness.addr}</li>
    <li class="a3">适用门店</li>
  </ul>
</div>
</body>
</html>
