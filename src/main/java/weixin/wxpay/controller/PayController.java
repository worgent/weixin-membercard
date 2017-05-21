package weixin.wxpay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.WxPayDto;
import com.demo.WxPayResult;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.InputSource;
import weixin.assistant.constants.WeixinConstant;
import weixin.assistant.model.*;
import weixin.assistant.service.WeixinAuthService;
import weixin.assistant.service.WeixinPayService;
import weixin.vip.service.MemberService;
import weixin.vip.service.VipPayService;
import weixin.wxpay.util.WxpayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by worgen on 2015/8/28.
 */
@Controller
@RequestMapping("/pay")
public class PayController {
    private static final Logger log = Logger.getLogger("BussinessLog");
    @Autowired
    private WeixinAuthService weixinAuthService;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private VipPayService vipPayService;

    @RequestMapping("/getJsCode")
    public String getJsCode(HttpServletRequest request, Model model){

        String code = request.getParameter("code");
        String state = request.getParameter("state");

        log.debug("code,"+code);
        log.debug("state,"+state);

        model.addAttribute("message", "success");
        return "weixin/message";
    }
    //发起支付请求，参数为公众号，支付详情，支付费用
    @RequestMapping("toJSPay")
    public String toJSPay(HttpServletRequest request, Model model){
        String gzhID = request.getParameter("gzh_id");
        String title = request.getParameter("title");
        String fee = request.getParameter("fee");

        WeixinGzh weixinGzh = weixinAuthService.getWeixinGzh(Integer.parseInt(gzhID));
        if( weixinGzh == null ){
            model.addAttribute("message", "账号"+gzhID+"信息不存在");
            return "weixin/message";
        }
        log.debug("this is log4j debug gzhID," + gzhID);
        String redirectUrl = "http://"+ WeixinConstant.domain+"/testcharge/getPayHtmlRedirect.do";
        redirectUrl = URLEncoder.encode(redirectUrl);

        JSONObject jsonStat = new JSONObject();
        jsonStat.put("gzhID", gzhID);
        jsonStat.put("title", title);
        jsonStat.put("fee", fee);

        //如果已经有授权信息，无需登录
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                + "appid="
                + weixinGzh.getAppId()
                + "&redirect_uri="
                + redirectUrl
                + "&response_type=code&scope="
                + "snsapi_base"
                + "&state="+URLEncoder.encode(jsonStat.toString())
                + "&component_appid="
                + weixinAuthService.getComponentAppID()
                + "#wechat_redirect";

        model.addAttribute("authorizeUrl", url);
        return "weixin/js_index";
    }
    @RequestMapping("/getPayHtmlRedirect")
    public String getPayHtmlRedirect(HttpServletRequest request, Model model){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String appID = request.getParameter("appid");
        JSONObject jsonStat = JSON.parseObject(URLDecoder.decode(state));
        String gzhID = jsonStat.getString("gzhID");
        String title = jsonStat.getString("title");
        String fee = jsonStat.getString("fee");
        int ret = weixinAuthService.userAuthorize(Integer.parseInt(gzhID), appID, code, state);
        log.debug("jsAuthorizeAccessToken,userAuthorize"
                +gzhID+","
                +appID+","
                +code+","
                +state+","
                + ret);
        WeixinGzhPay weixinGzhPay = weixinPayService.getWeixinGzhPay(Integer.parseInt(gzhID));
        if( weixinGzhPay == null ){
            model.addAttribute("message", "weixinGzhPay null,"+gzhID);
            return "weixin/message";
        }
        WeixinUserAuthorizer weixinUserAuthorizer = weixinAuthService.getWeixinUserAuthorizerByUserID(ret);
        if( weixinUserAuthorizer == null ){
            model.addAttribute("message", "weixinUserAuthorizer null");
            return "weixin/message";
        }
        String openID = weixinUserAuthorizer.getOpenId();
       // appID = "wxd52a218ec4c652c4";
      //  openID = "oHo1mtzPRw1fFCt_d8pLgQtcdr04";
        //微信支付jsApi
        WxPayDto tpWxPay = new WxPayDto();
        tpWxPay.setNotifyUrl(weixinGzhPay.getNotifyUrl());
        tpWxPay.setPartner(weixinGzhPay.getMchId());
        tpWxPay.setPartnerKey(weixinGzhPay.getMchKey());
        tpWxPay.setAppId(appID);
        tpWxPay.setOpenId(openID);
        tpWxPay.setOrderId(WxpayUtil.getNonceStr());
        tpWxPay.setSpbillCreateIp("127.0.0.1");
        tpWxPay.setBody(title);
        tpWxPay.setTotalFee(fee);

        String finaPackage = WxpayUtil.getPackage(tpWxPay);
        //生成订单

        model.addAttribute("finaPackage", finaPackage);

        return "wxpay/pay";
    }


    //
    @RequestMapping("/testcharge/getRechargeHtml")
    public @ResponseBody JSONObject getRechargeHtml(HttpServletRequest request){
        String paraFee = request.getParameter("money");
        float fFee = Float.parseFloat(paraFee);
        int fee = (int) (fFee*100);
        String outTradeNo = WxpayUtil.getNonceStr();
        String title = "微普台球测试充值";
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        JSONObject jsonAttach = new JSONObject();
        jsonAttach.put("type", "recharge");
        JSONObject jsonRet = vipPayService.getPayUrl(businessID, memberID, fee,
                outTradeNo, title,
                "/pay/testcharge/toRechargeHtml.do",
                JSONObject.toJSONString(jsonAttach));

        //生成订单
        VipRechargeOrder vipRechargeOrder = new VipRechargeOrder();
        vipRechargeOrder.setBusinessId(businessID);
        vipRechargeOrder.setMemberId(memberID);
        vipRechargeOrder.setCreateTime(new Date());
        vipRechargeOrder.setOutTradeNo(outTradeNo);
        vipRechargeOrder.setType(1);
        vipRechargeOrder.setFee(fee);
        memberService.insertVipRechargeOrder(vipRechargeOrder);

        String url = jsonRet.getString("pay_url")+ "&recharge_id="
                + vipRechargeOrder.getId();
        jsonRet.remove("pay_url");
        jsonRet.put("pay_url", url);
        return jsonRet;
    }
    @RequestMapping("/testcharge/toRechargeHtml")
    public String toRechargeHtml(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        String paraRechargeID = request.getParameter("recharge_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        int recharge_id = Integer.parseInt(paraRechargeID);
        String finaPackage = URLDecoder.decode(request.getParameter("finaPackage"));

        VipRechargeOrder vipRechargeOrder = memberService.getVipRechargeOrder(recharge_id);
        if( vipRechargeOrder == null ){
            log.error("vipRechargeOrder null,"+recharge_id);
            return "vip/error";
        }
         model.addAttribute("vipRechargeOrder", vipRechargeOrder);
         model.addAttribute("finaPackage", finaPackage);
         model.addAttribute("businessID", businessID);
         model.addAttribute("memberID", memberID);
        return "vip/vipRecharge";
    }

    //
    @RequestMapping("/testcharge/getPayHtml")
    public @ResponseBody JSONObject getPayHtml(HttpServletRequest request){
        String paraFee = request.getParameter("money");
        String paraPayType = request.getParameter("paytype");
        String payPassword = request.getParameter("pay_pass");
        int payType = Integer.parseInt(paraPayType);
        float fFee = Float.parseFloat(paraFee);
        int fee = (int) (fFee*100);
        String outTradeNo = WxpayUtil.getNonceStr();
        String title = "微普台球测试消费";
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        // model.addAttribute("finaPackage", finaPackage);
        // model.addAttribute("businessID", businessID);
        // model.addAttribute("memberID", memberID);
        if( payType == 1 ) {
            JSONObject jsonAttach = new JSONObject();
            jsonAttach.put("type", "pay");
            JSONObject jsonRet = vipPayService.getPayUrl(businessID, memberID, fee,
                    outTradeNo, title,
                    "/pay/testcharge/toPayHtml.do", JSONObject.toJSONString(jsonAttach));

            //生成订单
            VipPayOrder vipPayOrder = new VipPayOrder();
            vipPayOrder.setBusinessId(businessID);
            vipPayOrder.setMemberId(memberID);
            vipPayOrder.setCreateTime(new Date());
            vipPayOrder.setOutTradeNo(outTradeNo);
            vipPayOrder.setPayType(1);
            vipPayOrder.setFee(fee);
            memberService.insertVipPayOrder(vipPayOrder);

            String url = jsonRet.getString("pay_url")+ "&pay_id="
                    + vipPayOrder.getId();
            jsonRet.remove("pay_url");
            jsonRet.put("pay_url", url);

            return jsonRet;
        }
        else if( payType == 2 ){
            JSONObject jsonRet = new JSONObject();
            jsonRet.put("errno", 0);
            //判断支付密码
            if( memberService.checkBalancePassword(memberID, payPassword) == false ){
                jsonRet.put("errno", 4);
                jsonRet.put("error", "密码不正确");
                return jsonRet;
            }
            //余额支付，判断余额是否足够
            if( memberService.balanceEnough(memberID, fee) == false ){
                jsonRet.put("errno", 3);
                jsonRet.put("error", "余额不足");
                return jsonRet;
            }
            else{
                memberService.costBalance(memberID, fee);
                jsonRet.put("pay_result_url", "/vip/paySuccess.do?business_id="
                        + businessID
                        + "&member_id="
                        + memberID
                        + "&fee="
                        + fee);
                return jsonRet;
            }
        }else{
            log.error("exception,"+payType);
            JSONObject jsonRet = new JSONObject();
            jsonRet.put("errno", 2);
            return jsonRet;
        }
    }
    @RequestMapping("/testcharge/toPayHtml")
    public String toPayHtml(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        String paraPayID = request.getParameter("pay_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        int pay_id = Integer.parseInt(paraPayID);
        String finaPackage = URLDecoder.decode(request.getParameter("finaPackage"));

        VipPayOrder vipPayOrder = memberService.getVipPayOrder(pay_id);
        if( vipPayOrder == null ){
            log.error("vipPayOrder null,"+pay_id);
            return "vip/error";
        }
        model.addAttribute("vipPayOrder", vipPayOrder);
        model.addAttribute("finaPackage", finaPackage);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipPay";
    }
    @RequestMapping("/testcharge/*/notify")
    public void notify(HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        String path = request.getServletPath();
        String gzhID = path.replace("/pay/testcharge/", "").replace("/notify.do", "");

        //把如下代码贴到的你的处理回调的servlet 或者.do 中即可明白回调操作
        log.debug("微信支付回调数据开始," + gzhID);
        //示例报文
//		String xml = "<xml><appid><![CDATA[wxb4dc385f953b356e]]></appid><bank_type><![CDATA[CCB_CREDIT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[1228442802]]></mch_id><nonce_str><![CDATA[1002477130]]></nonce_str><openid><![CDATA[o-HREuJzRr3moMvv990VdfnQ8x4k]]></openid><out_trade_no><![CDATA[1000000000051249]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[1269E03E43F2B8C388A414EDAE185CEE]]></sign><time_end><![CDATA[20150324100405]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1009530574201503240036299496]]></transaction_id></xml>";
        String inputLine;
        String notityXml = "";
        String resXml = "";

        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("接收到的报文：" + notityXml);

        Map m = parseXmlToList2(notityXml);
        WxPayResult wpr = new WxPayResult();
        wpr.setAppid(m.get("appid").toString());
        wpr.setBankType(m.get("bank_type").toString());
        wpr.setCashFee(m.get("cash_fee").toString());
        wpr.setFeeType(m.get("fee_type").toString());
        wpr.setIsSubscribe(m.get("is_subscribe").toString());
        wpr.setMchId(m.get("mch_id").toString());
        wpr.setNonceStr(m.get("nonce_str").toString());
        wpr.setOpenid(m.get("openid").toString());
        wpr.setOutTradeNo(m.get("out_trade_no").toString());
        wpr.setResultCode(m.get("result_code").toString());
        wpr.setReturnCode(m.get("return_code").toString());
        wpr.setSign(m.get("sign").toString());
        wpr.setTimeEnd(m.get("time_end").toString());
        wpr.setTotalFee(m.get("total_fee").toString());
        wpr.setTradeType(m.get("trade_type").toString());
        wpr.setTransactionId(m.get("transaction_id").toString());
        String attach = m.get("attach").toString();
        JSONObject jsonAttach = JSONObject.parseObject(attach);
        boolean bSuccess = false;
        if("SUCCESS".equals(wpr.getResultCode())){
            bSuccess = true;
            //支付成功
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }

        log.debug("微信支付回调数据结束,"+gzhID);


        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
        if( jsonAttach.getString("type").equals("recharge") ){
            if(bSuccess == true){
                //更新订单状态，根据out_trade_no
                memberService.vipRechargeOrderSuccess(wpr.getOutTradeNo());
                log.info("wxpay,recharge,notify,success,"+gzhID+","+wpr.getOutTradeNo());
            }else{
                memberService.vipRechargeOrderFail(wpr.getOutTradeNo());
                log.info("wxpay,recharge,notify,fail,"+gzhID+","+wpr.getOutTradeNo());
            }
        }
        else if( jsonAttach.getString("type").equals("pay") ){
            if(bSuccess == true){
                //更新订单状态，根据out_trade_no
                memberService.vipPayOrderSuccess(wpr.getOutTradeNo());
                log.info("wxpay,pay,notify,success,"+gzhID+","+wpr.getOutTradeNo());
            }else{
                memberService.vipPayOrderFail(wpr.getOutTradeNo());
                log.info("wxpay,pay,notify,fail,"+gzhID+","+wpr.getOutTradeNo());
            }
        }
    }

    /**
     * description: 解析微信通知xml
     *
     * @param xml
     * @return
     * @author ex_yangxiaoyi
     * @see
     */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    private static Map parseXmlToList2(String xml) {
        Map retMap = new HashMap();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc = (Document) sb.build(source);
            Element root = doc.getRootElement();// 指向根节点
            List<Element> es = root.getChildren();
            if (es != null && es.size() != 0) {
                for (Element element : es) {
                    retMap.put(element.getName(), element.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }
}
