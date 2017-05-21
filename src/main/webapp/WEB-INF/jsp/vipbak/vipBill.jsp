<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/11
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
  <meta charset="utf-8" />
  <link rel="stylesheet" type="text/css" href="/css/weimob_main.css" media="all" />
  <link rel="stylesheet" type="text/css" href="/css/weimob_dialog.css" media="all" />
  <script type="text/javascript" src="/js/jquery_min.js"></script>
  <script type="text/javascript" src="/js/weimob_dialog_min.js"></script>
  <script type="text/javascript" src="/js/weimob_main.js"></script>
  <title>会员卡</title>
  <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
  <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
  <meta name="Keywords" content="" />
  <meta name="Description" content="" />
  <!-- Mobile Devices Support @begin -->
  <meta content="application/xhtml+xml;charset=UTF-8" http-equiv="Content-Type">
  <meta content="telephone=no, address=no" name="format-detection">
  <meta name="apple-mobile-web-app-capable" content="yes" /> <!-- apple devices fullscreen -->
  <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
  <!-- Mobile Devices Support @end -->
  <link rel="shortcut icon" href="http://stc.weimob.com/img/favicon.ico" />
</head>
<body onselectstart="return true;" ondragstart="return false;">
<div class="container bill  ">
  <header>
    <div class="bill_header">
      <ul class="bill_detail box">
        <li>
          <label>收入:<span>0.04</span></label>
        </li>
        <li>
          <label>支出:<span>0.03</span></label>
        </li>
        <li>
          <div>
            <select name="date" id="monthSelectElement">
              <option selected="selected" value="201509">
                15年09月</option>
              <option  value="201508">
                15年08月</option>
              <option  value="201507">
                15年07月</option>
              <option  value="201506">
                15年06月</option>
              <option  value="201505">
                15年05月</option>
              <option  value="201504">
                15年04月</option>
            </select>
          </div>
        </li>
      </ul>
    </div>
  </header>
  <div class="body">
    <ul class="list_message list_bill">
      <li>
        <a href="javascript:;">
          <dl class="tbox">
            <dd>
              <span class="icon_7"><img src="" class="vhidden"  /></span>
              <div>会员卡</div>
            </dd>
            <dd>
              <h3>余额付款</h3>
              <p>2015-09-10 18:00:24</p>
            </dd>
            <dd>
              <label>-0.01元</label>
            </dd>
          </dl>
        </a>
      </li>
      <li>
        a href="javascript:;">
        <dl class="tbox">
          <dd>
            <span class="icon_7"><img src="" class="vhidden"  /></span>
            <div>会员卡</div>
          </dd>
          <dd>
            <h3>余额付款</h3>
            <p>2015-09-10 16:06:49</p>
          </dd>
          <dd>
            <label>-0.01元</label>
          </dd>
        </dl>
        </a>
      </li>
      <li>
        <a href="javascript:;">
          <dl class="tbox">
            <dd>
              <span class="icon_8"><img src="" class="vhidden"  /></span>
              <div>充值</div>
            </dd>
            <dd>
              <h3>现金充值/活动赠送</h3>
              <p>2015-09-10 11:43:59</p>
            </dd>
            <dd>
              <label>0.01元</label>
            </dd>
          </dl>
        </a>
      </li>
      <li>
        <a href="javascript:;">
          <dl class="tbox">
            <dd>
              <span class="icon_7"><img src="" class="vhidden"  /></span>
              <div>会员卡</div>
            </dd>
            <dd>
              <h3>余额付款</h3>
              <p>2015-09-08 15:50:16</p>
            </dd>
            <dd>
              <label>-0.01元</label>
            </dd>
          </dl>
        </a>
      </li>
      <li>
        <a href="javascript:;">
          <dl class="tbox">
            <dd>
              <span class="icon_8"><img src="" class="vhidden"  /></span>
              <div>充值</div>
            </dd>
            <dd>
              <h3>现金充值/活动赠送</h3>
              <p>2015-09-08 15:47:39</p>
            </dd>
            <dd>
              <label>0.01元</label>
            </dd>
          </dl>
        </a>
      </li>
      <li>
        <a href="javascript:;">
          <dl class="tbox">
            <dd>
              <span class="icon_8"><img src="" class="vhidden"  /></span>
              <div>充值</div>
            </dd>
            <dd>
              <h3>现金充值/活动赠送</h3>
              <p>2015-09-08 15:46:56</p>
            </dd>
            <dd>
              <label>0.01元</label>
            </dd>
          </dl>
        </a>
      </li>
      <li>
        <a href="javascript:;">
          <dl class="tbox">
            <dd>
              <span class="icon_8"><img src="" class="vhidden"  /></span>
              <div>充值</div>
            </dd>
            <dd>
              <h3>现金充值/活动赠送</h3>
              <p>2015-09-08 15:44:45</p>
            </dd>
            <dd>
              <label>0.01元</label>
            </dd>
          </dl>
        </a>
      </li>
    </ul>
  </div>

  <script>
    /**
     * 日期变更
     */
    $("#monthSelectElement").on("change",function(){

      url="/Webnewmemberscore/bill/pid/470983/wechatid/otRuZszZJK-3mBT0n9ftG2__nit0";

      if (url.indexOf('?') >= 0) {
        url= url+"&date="+$("#monthSelectElement").val();
      } else {
        url= url+"?date="+$("#monthSelectElement").val();
      }

      setTimeout("location.href='"+url+"'",20);
    })

  </script>

  <footer>
    <nav class="nav">
      <ul class="box">
        <li>
          <a href="/Webnewmemberscore/index/pid/470983/wechatid/otRuZszZJK-3mBT0n9ftG2__nit0" >
            <p class="card"></p>
            <span>会员卡</span>
          </a>
        </li>
        <li>
          <a href="/Webnewmemannounce/systemnotice?pid=470983&wechatid=otRuZszZJK-3mBT0n9ftG2__nit0" >
            <p id="Js-msg-num" class="msg" data-count="11" ></p>
            <span>消息</span>
          </a>
        </li>
        <li>
          <a href="/Webnewmemintegral/signin/pid/470983/wechatid/otRuZszZJK-3mBT0n9ftG2__nit0" >
            <p class="sign"></p>
            <span>签到</span>
          </a>
        </li>
        <li>
          <a href="/Webnewmemberscore/share/pid/470983/wechatid/otRuZszZJK-3mBT0n9ftG2__nit0" >
            <p class="share"></p>
            <span>分享</span>
          </a>
        </li>
        <li>
          <a href="/Webnewmemberscore/my/pid/470983/wechatid/otRuZszZJK-3mBT0n9ftG2__nit0" class="my on" >
            <p class="my" style="background-position-y:-40px;" ></p>
            <span>我的</span>
          </a>
        </li>
      </ul>
    </nav>
  </footer>
  <script type="text/javascript">
    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
      WeixinJSBridge.call('hideToolbar');
    });


  </script></div></body>
</html>
