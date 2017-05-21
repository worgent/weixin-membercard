<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="format-detection" content="telephone=no">
	<meta name="renderer" content="webkit">
	<title>我的账单</title>
	<link rel="stylesheet" href="/lib/css/global.css">
	<link rel="stylesheet" href="/lib/css/default.css">
	<script type="text/javascript" src="/lib/js/jweixin-1.0.0.js"></script>

</head>
<body id="myBill">
    <div class="main">
        <div class="details">
            <ul>
                <li class="sx">收入:</li>
                <li class="sx">支出:</li>
                <li>
                    <input type="month" value="${timeMark}">
                </li>
            </ul>
            <ul class="number">
                <li>${feeInput}</li>
                <li>${feeOutput}</li>
            </ul>
        </div>
        <c:forEach items="${jsonList}" var="json" varStatus="vs">
            <div class="myBill_det">
                <c:choose>
                    <c:when test="${json.type== 'recharge'}">
                       <img src="/lib/img/det2.png" alt="">
                        <ul>
                            <li>充值/现金充值</li>
                            <li>${json.time}</li>
                        </ul>
                        <b>${json.fee}元</b>
                        
                    </c:when>
                    <c:when test="${json.type== 'pay'}">

                       <img src="/lib/img/det1.png" alt="">
                        <ul>
                            <li>会员卡/余额支付</li>
                            <li>${json.time}</li>
                        </ul>
                        <b>${json.fee}元</b>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </div>
  <%@ include file="common/footer.jsp"%>
  <%@ include file="common/weixinShare.jsp"%>

</body>
</html>