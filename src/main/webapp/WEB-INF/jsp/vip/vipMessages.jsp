<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>

<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="format-detection" content="telephone=no">
	<meta name="renderer" content="webkit">
	<title>系统消息</title>
	<link rel="stylesheet" href="/lib/css/global.css">
	<link rel="stylesheet" href="/lib/css/default.css">
	<script type="text/javascript" src="/lib/js/jweixin-1.0.0.js"></script>

</head>
<body  id="news">
  <div class="title">系 统 信 息</div>
  <div class="main">
        <c:forEach items="${vipMessageList}" var="vipMessage" varStatus="vs">
            <div class="bgc">
                <div class="myBill_det">
                    <c:choose>
                        <c:when test="${vipMessage.type < 4}">
                            <img src="/lib/img/news${vipMessage.type}.png" alt="">

                        </c:when>
                        <c:otherwise>
                            <img src="/lib/img/news3.png" alt="">
                        </c:otherwise>
                    </c:choose>
                    <ul>
                        <li>${vipMessage.title}</li>
                        <li>${vipMessage.createTime}
                        </li>
                    </ul>
                    <em></em>
                </div>
                <p>${vipMessage.content}</p>
            </div>
        </c:forEach>
        <div class="h"></div>
    </div>
  <%@ include file="common/footer.jsp"%>
  <%@ include file="common/weixinShare.jsp"%>
</body>
</html>