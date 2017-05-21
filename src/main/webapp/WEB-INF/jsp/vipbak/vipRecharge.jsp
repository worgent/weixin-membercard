<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"  isELIgnored="false"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//
//String appId = request.getParameter("appid");
//String timeStamp = request.getParameter("timeStamp");
//String nonceStr = request.getParameter("nonceStr");
//String packageValue = request.getParameter("package");
//String paySign = request.getParameter("sign");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>微信支付</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>

  <body>
		<br><br><br><br><br><br><br>
  			<div style="text-align:center;size:30px;"><input type="button" style="width:200px;height:80px;" value="微信支付" onclick="callpay()"></div>
  </body>
  <body>
  <section class="body">
	  <div class="ordernum">订单号：${vipRechargeOrder.outTradeNo}</div>
	  <div class="money">共计金额￥${vipRechargeOrder.fee/100}</div>
	  <div class="time">下单时间：${vipRechargeOrder.createTime}</div>
	  <a href="javascript:callpay();" class="btn" id="getBrandWCPayRequest">确认支付</a>
  </section>

  <section id="dialog" class="dialog">
	  <div class="dialog_mask">&nbsp;</div>
	  <div class="dialog_body">
		  <p id="dialog_content">
			  正在查询支付结果...
		  </p>
		  <div class="dialog_loading"><span></span></div>
	  </div>
  </section>
  </body>

  <script type="text/javascript">
  	function callpay(){
		 WeixinJSBridge.invoke('getBrandWCPayRequest',{
			 <%--"appId" : "<%=appId%>","timeStamp" : "<%=timeStamp%>", "nonceStr" : "<%=nonceStr%>", "package" : "<%=packageValue%>","signType" : "MD5", "paySign" : "<%=paySign%>"  		 "appId" : "<%=appId%>","timeStamp" : "<%=timeStamp%>", "nonceStr" : "<%=nonceStr%>", "package" : "<%=packageValue%>","signType" : "MD5", "paySign" : "<%=paySign%>"--%>
			 <%--"appId" : "${appId}","timeStamp" : "${timeStamp}", "nonceStr" : "${nonceStr}", "package" : "${packageValue}","signType" : "MD5", "paySign" : "${paySign}"--%>
			${finaPackage}
		 },function(res){
				WeixinJSBridge.log(res.err_msg);
// 				alert(res.err_code + res.err_desc + res.err_msg);
	            if(res.err_msg == "get_brand_wcpay_request:ok"){  
	                alert("微信支付成功!");  
	            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
	                alert("用户取消支付!");  
	            }else{  
	               alert("支付失败!");  
	            }  
			})
		}
  </script>
</html>
