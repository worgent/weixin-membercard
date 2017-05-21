<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="format-detection" content="telephone=no">
	<meta name="renderer" content="webkit">
	<title>个人信息</title>
	<link rel="stylesheet" href="/lib/css/global.css">
	<link rel="stylesheet" href="/lib/css/default.css">
	<script type="text/javascript" src="/lib/js/jweixin-1.0.0.js"></script>

</head>
<body  id="info">
  <div class="header">
  	 <div class="head">
  	 	<img src="/lib/img/head.jpg" alt="">
  	 	<ul>
  	 		<li>${vipMember.name}</li>
  	 		<li>普通会员</li>
  	 	</ul>
  	 	<div class="shezhi">
  	 		<img src="/lib/img/sz.png" alt="">
  	 	</div>
  	 </div>
  </div>
  <div class="main">
  	<div class="details">
  		<ul>
  			<li class="sx">优惠券</li>
  			<li class="sx">代金券</li>
  			<li class="sx">积分</li>
  			<li>余额</li>
  		</ul>
  		<ul class="number">
  			<li>0</li>
  			<li>0</li>
  			<li>0</li>
  			<li>${balance}</li>
  		</ul>
  	</div>
  	<div class="menu" id="menu">
  		<ul>
  			<li class="bsx"><a href="/vip/vipCoupon.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t1.png" alt=""><span>我的代金券</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  			<li class="bsx"><a href="/vip/vipGift.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t2.png" alt=""><span>我的礼品卷</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  			<li class="bsx"><a href="/vip/vipActivity.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t3.png" alt=""><span>我的活动</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  			<li><a href="/vip/vipBill.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t4.png" alt=""><span>我的账单</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  		</ul>
  		<ul class="mt20">
  			<li class="bsx"><a href="tel:${vipBusiness.tel}"><img src="/lib/img/t5.png" alt=""><span>${vipBusiness.tel}</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  			<li class="bsx"><a href="addrinfo.html"><img src="/lib/img/t6.png" alt=""><span>${vipBusiness.addr}</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  			<li class="bsx"><a href="/vip/vipChangePassword.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t7.png" alt=""><span>会员卡密码</span><img src="/lib/img/my_dy.png" alt=""></a></li>
  		</ul>
  	</div>
  	<div class="h"></div>
  </div>
  <%@ include file="common/footer.jsp"%>
  <%@ include file="common/weixinShare.jsp"%>

</body>
</html>