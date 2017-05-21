<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="format-detection" content="telephone=no">
	<meta name="renderer" content="webkit">
	<title>地址信息</title>
	<link rel="stylesheet" href="/lib/css/global.css">
	<link rel="stylesheet" href="/lib/css/default.css">
</head>
<body id="address_info">
 <div class="address_info">
 	  <p><img src="/lib/img/t6.png" alt=""><b>${vipBusiness.city}</b></p>
 	  <ul>
 	 	<li class="hx"><p><b>门店：</b>${vipBusiness.name}</p></li>
 	 	<li class="hx"><p><b>地址：</b>${vipBusiness.addr}</p></li></li>
 	 	<li><p><b>电话：</b>${vipBusiness.tel}</p></li></li>
 	 </ul>
 </div>

 <%@ include file="common/footer.jsp"%>

</body>
</html>