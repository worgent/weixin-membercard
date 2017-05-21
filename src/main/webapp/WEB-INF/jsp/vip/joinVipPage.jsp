<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<html>
<head>
<meta http-equiv="Content-Type" Content="text/html;charset=utf-8">
<meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<script type="text/javascript" src="../js/jquery-1.11.2.min.js" ></script>
<script type="text/javascript" src="../js/common.js" ></script>
<link rel="stylesheet" type="text/css" href="/lib/css/index.css"/>
</head>
<body>
	<div class="main">
		<div class="banner">
			<img src="/lib/img/one-banner.png"/>
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