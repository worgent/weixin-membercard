package weixin.assistant.controller;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.model.kfaccount.KfcustomSend;
import org.jeewx.api.core.req.model.kfaccount.MsgArticles;
import org.jeewx.api.core.req.model.kfaccount.MsgNews;
import org.jeewx.api.core.req.model.kfaccount.MsgText;
import org.jeewx.api.wxsendmsg.JwKfaccountAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;
import weixin.assistant.constants.WeixinConstant;
import weixin.assistant.model.*;
import weixin.assistant.service.WeixinAuthService;
import weixin.assistant.service.WeixinVipService;
import weixin.assistant.util.RequestUtil;
import weixin.assistant.util.TimeUtil;
import weixin.assistant.util.WeixinUtil;
import weixin.guanjia.core.entity.message.resp.NewsMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.service.impl.WechatService;
import weixin.guanjia.core.util.MessageUtil;
import weixin.vip.model.Page;
import weixin.vip.service.AdminService;
import weixin.wxpay.util.WxpayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;


/**
 * Created by worgen on 2015/9/1.
 */
@Controller
@RequestMapping("/call_back")
public class WeixinController {

    private static Logger log = Logger.getLogger("Businesslog");
    @Autowired
    private WeixinAuthService weixinAuthService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private WeixinVipService weixinVipService;

    @Autowired
    private AdminService adminService;

    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model){

        return "weixin/index";
    }

    /*
推送component_verify_ticket协议;推送取消授权
在公众号第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。
 */
    @RequestMapping("bind_gzh_event")
    public String bindGzhEvent(HttpServletRequest request,Model mode) throws IOException, AesException, DocumentException {
        log.debug("event.jsp------------------------");

        String decryptCode = weixinAuthService.decryptMessage(request);
        //
        weixinAuthService.dealComponentPush(decryptCode);
        return "call_back/bind_gzh_event";
    }

    @RequestMapping("toAuthorize")
    public @ResponseBody String toAuthorize(HttpServletRequest request, Model model){
        String businessID = request.getParameter("businessID");
        log.debug("this is log4j debug businessID," + businessID);
        if( businessID == null || businessID.isEmpty()){
            businessID = "0";
        }
        //如果已经有授权信息，无需登录
//        WeixinGzhAuthorizer weixinGzhAuthorizer =
//                weixinAuthService.getWeixinGzhAuthorizer(Integer.parseInt(accountID));
//        if( weixinGzhAuthorizer != null ){
//            model.addAttribute("message", "账号"+accountID+"已获得授权");
//            return "weixin/message";
//        }
        String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="
                + weixinAuthService.getComponentAppID()
                +"&pre_auth_code="
                + weixinAuthService.getPreAuthCode().getCode()
                + "&redirect_uri="
                + weixinAuthService.getRedirectUrl().replace("%businessID%", businessID);

       // model.addAttribute("authorizeUrl", url);
        //return "weixin/index";
        return url;
    }

    //http://weili.tunnel.mobi/call_back/authorizer_access_token.do?auth_code=queryauthcode@@@KfJgq0cvfWWAMQ-MRtkl4e1HsCadA9QTXe5qlP8FsRgU43ybH-1uO_oT-XR7382i&expires_in=3600
    @RequestMapping("/*/authorizer_access_token")
    public String authorizeAccessToken(HttpServletRequest request,Model model) {
        String path = request.getServletPath();
        String businessID = path.replace("/call_back/", "").replace("/authorizer_access_token.do", "");
        if( businessID == null || businessID.isEmpty()){
            businessID = "0";
        }
        String authCode = request.getParameter("auth_code");
        long expiresIn = Long.parseLong(request.getParameter("expires_in"));
        int ret = weixinAuthService.authorizeGzh(Integer.parseInt(businessID), authCode, expiresIn);
        log.debug("authorizeAccessToken,"+businessID+","+authCode+","+expiresIn+","+ret);
        return "call_back/authorizer_access_token";
    }

    @RequestMapping("toJSAuthorize")
    public String toJSAuthorize(HttpServletRequest request, Model model){
        String gzhID = request.getParameter("gzh_id");
        String realPath = request.getParameter("real_path");
        WeixinGzh weixinGzh = weixinAuthService.getWeixinGzh(Integer.parseInt(gzhID));
        if( weixinGzh == null ){
            model.addAttribute("message", "账号"+gzhID+"信息不存在");
            return "weixin/message";
        }
        log.debug("this is log4j debug gzhID," + gzhID);
        //如果已经有授权信息，无需登录
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                + "appid="
                + weixinGzh.getAppId()
                + "&redirect_uri="
                + weixinAuthService.getJSRedirectUrl().
                replace("%gzh_id%", gzhID)
                + "&response_type=code&scope="
                + "snsapi_base"
                + "&state="+realPath
                +"&component_appid="
                + weixinAuthService.getComponentAppID()
                + "#wechat_redirect";

        model.addAttribute("authorizeUrl", url);
        return "weixin/js_index";
    }
    //    https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&component_appid=component_appid#wechat_redirect
    //redirect_uri?code=CODE&state=STATE&appid=APPID
    @RequestMapping("/*/js_authorizer_access_token")
    public String jsAuthorizeAccessToken(HttpServletRequest request,Model mode){
        String path = request.getServletPath();
        String gzhID = path.replace("/call_back/", "").replace("/js_authorizer_access_token.do", "");


        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String appID = request.getParameter("appid");

        int ret = weixinAuthService.userAuthorize(Integer.parseInt(gzhID), appID, code, state);
        log.debug("jsAuthorizeAccessToken,userAuthorize"
                +gzhID+","
                +appID+","
                +code+","
                +state+","
                + ret);
        return "call_back/authorizer_access_token";
    }

    @RequestMapping("/*/msg_event")
    public void wechatPost(HttpServletResponse response,
                           HttpServletRequest request) throws IOException, AesException, WexinReqException, DocumentException, ParserConfigurationException, SAXException {
        String path = request.getServletPath();
        String appID = path.replace("/call_back/", "").replace("/msg_event.do", "");
        log.debug("getContextPath,"+request.getContextPath());
        log.debug("getServletPath,"+request.getServletPath());
        log.debug("wechatID,"+appID);
        String respMessage = null;

        boolean bVip = false;
        //调用客服接口

        // try {
        // 默认返回的文本消息内容
        String respContent = "请求处理异常，请稍候尝试！";
        // xml请求解析
        String xml = weixinAuthService.decryptMessage(request);
        log.debug("coreService,xml,"+xml);

        Document doc = null;
        doc = DocumentHelper.parseText(xml); // 将字符串转为XML
        Element rootElement = doc.getRootElement(); // 获取根节点smsReport
        //Map<String, String> requestMap = MessageUtil.parseXml(request);
        // 发送方帐号（open_id）
        String fromUserName = rootElement.elementTextTrim("FromUserName");
        // 公众帐号
        String toUserName = rootElement.elementTextTrim("ToUserName");
        // 消息类型
        String msgType = rootElement.elementTextTrim("MsgType");
        String msgId = rootElement.elementTextTrim("MsgId");
        //消息内容
        String content = rootElement.elementTextTrim("Content");
        log.info("------------微信客户端发送请求---------------------   |   fromUserName:"+fromUserName+"   |   ToUserName:"+toUserName+"   |   msgType:"+msgType+"   |   msgId:"+msgId+"   |   content:"+content);
        //根据微信ID,获取配置的全局的数据权限ID
        log.info("-toUserName--------"+toUserName);
        WeixinGzhAuthorizer weixinGzhAuthorizer = weixinAuthService.getWeixinGzhAuthorizer(appID);
        //测试号特殊情况

        //String sys_accountId = weixinAccountService.findByToUsername(toUserName).getId();
        //log.info("-sys_accountId--------"+sys_accountId);
        //ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
        // 默认回复此文本消息
        TextMessageResp textMessage = new TextMessageResp();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setContent("default content");
        // 将文本消息对象转换成xml字符串
        respMessage = MessageUtil.textMessageToXml(textMessage);
        //【微信触发类型】文本消息
        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
            log.info("------------微信客户端发送请求------------------【微信触发类型】文本消息---");
            //respMessage = "REQ_MESSAGE_TYPE_TEXT";//doTextResponse(content,toUserName,textMessage,bundler,
            //sys_accountId,respMessage,fromUserName,request,msgId,msgType);
            respContent = "您发送的是文本消息！";
            //
            String message = rootElement.elementTextTrim("Content");
            if( message.equals("vip") ){
                //推送会员卡
                bVip = true;
            }
            //处理微信测试号文本信息
            //测试2 模拟粉丝发送文本消息给专用测试公众号，第三方平台方需根据文本消息的内容进行相应的响应：
            if( true || appID.equals(WeixinConstant.testAppID) ) {
                log.debug("testAppID,text,"+message);
                if (message.equals("TESTCOMPONENT_MSG_TYPE_TEXT")) {
                    TextMessageResp textResp = new TextMessageResp();
                    textResp.setCreateTime(new Date().getTime());
                    textResp.setFromUserName(toUserName);
                    textResp.setToUserName(fromUserName);
                    textResp.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
                    textResp.setContent("TESTCOMPONENT_MSG_TYPE_TEXT_callback");
                    //textResp.setContent("why not");
                    respMessage = MessageUtil.textMessageToXml(textResp);
                    long timestamp = TimeUtil.now();
                    String nounce = WxpayUtil.getNonceStr();
                    String cryptMsg = weixinAuthService.cryptMessage(respMessage, String.valueOf(timestamp), nounce);
                    log.debug("test2," + respMessage);
                    log.debug("test2,crypt" + cryptMsg);
                    PrintWriter out = response.getWriter();
                    out.print(cryptMsg);
                    out.close();
                    //TestUtil.testDecrypt(cryptMsg);
                    return;
                }
                //
                //测试3 模拟粉丝发送文本消息给专用测试公众号，第三方平台方需在5秒内返回空串表明暂时不回复，然后再立即使用客服消息接口发送消息回复粉丝
                if ( message.contains("QUERY_AUTH_CODE:")) {
                    String preAuthCode = message.replace("QUERY_AUTH_CODE:", "");
                    PrintWriter out = response.getWriter();
                    out.print("success");
                    out.close();
                    weixinAuthService.authorizeGzh(0, preAuthCode, 7200);
                    WeixinGzhAuthorizer weixinGzhAuthorizer2 = weixinAuthService.getWeixinGzhAuthorizer(appID);
                    if( weixinGzhAuthorizer2 == null ){
                        log.error("weixinGzhAuthorizer null,"+appID);
                        return;
                    }
                    KfcustomSend kfcustomSend = new KfcustomSend();
                    kfcustomSend.setAccess_token(weixinGzhAuthorizer2.getAuthorizerAccessToken());
                    kfcustomSend.setTouser(fromUserName);
                    kfcustomSend.setMsgtype("text");
                    MsgText msgText = new MsgText();
                    msgText.setContent(preAuthCode + "_from_api");
                    kfcustomSend.setText(msgText);
                    String strRet = JwKfaccountAPI.sendKfMessage(kfcustomSend);
                    log.debug("sendKfMessage," + JSON.toJSONString(kfcustomSend) + "," + strRet);
                    return;
                }
            }
        }
        //【微信触发类型】图片消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
            respContent = "您发送的是图片消息！";
        }
        //【微信触发类型】地理位置消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
            respContent = "您发送的是地理位置消息！";
        }
        //【微信触发类型】链接消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
            respContent = "您发送的是链接消息！";
        }
        //【微信触发类型】音频消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
            respContent = "您发送的是音频消息！";
        }
        //【微信触发类型】事件推送
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {

            log.info("------------微信客户端发送请求------------------【微信触发类型】事件推送---");
            // 事件类型
            String eventType = rootElement.elementTextTrim("Event");
            //处理测试号事件
            //测试1 模拟粉丝触发专用测试公众号的事件，并推送事件消息到专用测试公众号，第三方平台方开发者需要提取推送XML信息中的event值，并在5秒内立即返回按照下述要求组装的文本消息给粉丝。
            if( appID.equals(WeixinConstant.testAppID) ) {
                log.debug("testAppID,event,"+eventType);

                TextMessageResp textResp = new TextMessageResp();
                textResp.setCreateTime(new Date().getTime());
                textResp.setFromUserName(toUserName);
                textResp.setToUserName(fromUserName);
                textResp.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
                textResp.setContent(eventType+"from_callback");
                respMessage = MessageUtil.textMessageToXml(textResp);
                long timestamp = TimeUtil.now();
                String nounce = WxpayUtil.getNonceStr();
                String cryptMsg = weixinAuthService.cryptMessage(respMessage, String.valueOf(timestamp), nounce);
                log.debug("test1," + respMessage);
                log.debug("test1,crypt" + cryptMsg);
                log.debug("test1," + cryptMsg);
                PrintWriter out = response.getWriter();

                out.print(cryptMsg);
                out.close();
                return;
            }
            // 订阅
            if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                respContent = "您发送的是消息类：EVENT_TYPE_SUBSCRIBE";
                //respMessage = doDingYueEventResponse(requestMap, textMessage, bundler, respMessage, toUserName, fromUserName, respContent, sys_accountId);
            }
            // 取消订阅
            else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
            }
            // 自定义菜单点击事件
            else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                respContent = "您发送的是消息类：EVENT_TYPE_CLICK";
                //respMessage = doMyMenuEvent(requestMap, textMessage, bundler, respMessage, toUserName, fromUserName, respContent, sys_accountId, request);
            }
        }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        if( weixinGzhAuthorizer == null ){
            if( appID.equals(WeixinConstant.testAppID) )
                log.error("weixinGzhAuthorizer null,"+appID);
            return;
        }
        PrintWriter out = response.getWriter();
        out.print("success");
        out.close();

        if( bVip == true ){
            weixinVipService.responseVip(appID, fromUserName);
        }else {
            KfcustomSend kfcustomSend = new KfcustomSend();
            kfcustomSend.setAccess_token(weixinGzhAuthorizer.getAuthorizerAccessToken());
            kfcustomSend.setTouser(fromUserName);
            kfcustomSend.setMsgtype("text");
            MsgText msgText = new MsgText();
            msgText.setContent("服务器正在响应，请耐心等待");
            kfcustomSend.setText(msgText);
            String strRet = JwKfaccountAPI.sendKfMessage(kfcustomSend);
            log.debug("sendKfMessage," + JSON.toJSONString(kfcustomSend) + "," + strRet);
        }
    }
}
