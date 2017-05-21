<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/15
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<script>
  window.onload = function(){
    wxShareStart();
  }
  function wxShareStart() {
    wx.config({
      debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
      appId: '${weixinGzh.appId}', // 必填，公众号的唯一标识
      timestamp:${signs.timestamp}, // 必填，生成签名的时间戳
      nonceStr: '${signs.nonceStr}', // 必填，生成签名的随机串
      signature: '${signs.signature}',// 必填，签名，见附录1
      jsApiList: [
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
      ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function () {
      //alert("wx ready,");
      funcMenuShareTimeline();
      funcMenuShareAppMessage();
    });

    wx.error(function (res) {
      //alert("wx error," + res.toString());
    });
  }
  function funcMenuShareTimeline() {
    wx.onMenuShareTimeline({
      title: "${vipBusiness.name}",
      link: "${signs.url}",
      imgUrl: "http://weixin.iwpoo.com:4869/cf64d43ad4e4db90a35b187c65b2adbf",
      success: function () {
       // alert("share success");
      },
      cancel: function () {
       // alert("share cancel");
      }
    });
  }

  function funcMenuShareAppMessage(){
    wx.onMenuShareAppMessage({
      title: "${vipBusiness.name}",
      desc: "微信会员卡，方便携带，永不挂失！",
      link: "${signs.url}",
      imgUrl: "http://weixin.iwpoo.com:4869/cf64d43ad4e4db90a35b187c65b2adbf",
      success: function(){
        //alert("share success");
      },
      cancel: function(){
        //alert("share cancel");
      }
    });
  }

</script>
