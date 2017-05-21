<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/11
  Time: 19:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="footer">
  <ul>
    <li>
      <a href="/vip/vipHomePage.do?business_id=${businessID}&member_id=${memberID}">
        <img src="/lib/img/shouye.png" alt="">首页</a>
    </li>
    <li>
      <a href="/vip/vipMessages.do?business_id=${businessID}&member_id=${memberID}">
        <img src="/lib/img/xiaoxi.png" alt="">消息</a>
    </li>
    <li>
      <a href="">
        <img src="/lib/img/fenxiang.png" alt="">分享</a>
    </li>
    <li>
      <a href="/vip/vipPersonalPage.do?business_id=${businessID}&member_id=${memberID}">
        <img src="/lib/img/me.png" alt="">我的</a>
    </li>
  </ul>
</div>
