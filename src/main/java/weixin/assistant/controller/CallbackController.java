//package weixin.assistant.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.qq.weixin.mp.aes.AesException;
//import weixin.assistant.model.AuthorizerAccount;
//import weixin.assistant.model.AuthorizerInfo;
//import weixin.assistant.service.SerializeService;
//import weixin.assistant.util.WeixinManager;
//import iwpoo.xigua.dao.GzhArticleMapper;
//import iwpoo.xigua.model.GzhArticle;
//import iwpoo.xigua.model.GzhArticleExample;
//import iwpoo.xigua.service.WeixinService;
//import org.apache.log4j.Logger;
//import org.dom4j.DocumentException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import weixin.guanjia.core.service.impl.WechatService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
///**
//* Created by worgen on 2015/8/17.
//*/
//@Controller
//@RequestMapping("/test")
//public class CallbackController {
//    private static Logger log = Logger.getLogger("Businesslog");
//
//
//    @Autowired
//    private WechatService wechatService;
//
//
//
////http://weili.tunnel.mobi/call_back/authorizer_access_token.do?auth_code=queryauthcode@@@KfJgq0cvfWWAMQ-MRtkl4e1HsCadA9QTXe5qlP8FsRgU43ybH-1uO_oT-XR7382i&expires_in=3600
//@RequestMapping("authorizer_access_token")
//public String authorizeAccessToken(HttpServletRequest request,Model mode){
//    String auth_code = request.getParameter("auth_code");
//    String expiresIn = request.getParameter("expires_in");
//    //
//    AuthorizerInfo authorizerInfo = new AuthorizerInfo(
//            WeixinManager.getInstance().getComponentAppID(),
//            WeixinManager.getInstance().getComponentAppSecret(),
//            auth_code,
//            Integer.parseInt(expiresIn) );
//
//    AuthorizerAccount authorizerAccount = new AuthorizerAccount(
//            WeixinManager.getInstance().getComponentAppID(),
//            authorizerInfo.getAuthorizerAppID() );
//
//    if( WeixinManager.getInstance().containsAuthorizerInfo(authorizerInfo.getAppID()) ){
//        log.info("authorizerInfo exist," + com.alibaba.fastjson.JSONObject.toJSONString(authorizerInfo));
//        WeixinManager.getInstance().removeAuthorizerInfo(authorizerInfo.getAppID());
//    }
//    WeixinManager.getInstance().addAuthorizerInfo(authorizerInfo);
//
//    if( WeixinManager.getInstance().containsAuthorizerAccount(authorizerAccount.getAppID()) ){
//        log.info("authorizerAccount exist,"+ com.alibaba.fastjson.JSONObject.toJSONString(authorizerInfo));
//        WeixinManager.getInstance().removeAuthorizerAccount(authorizerAccount.getAppID());
//
//    }
//    WeixinManager.getInstance().addAuthorizerAccount(authorizerAccount);
//
//    //记录账号信息
//    return "call_back/authorizer_access_token";
//}
//
//    @RequestMapping("testDealPush")
//    public String testDealPush(HttpServletRequest request,Model mode) throws AesException, DocumentException {
//        String miwen = "<xml>\n" +
//                "    <AppId><![CDATA[wx69722e5ef003da66]]></AppId>\n" +
//                "    <Encrypt><![CDATA[SmcfKLqAZ+egmkaPVsv2I4AkK9WOiHFgUWXjkrGmYU+Kf0WycBGNamRiBCde3R5iAxPh1vR/AP6Thb1/vPe76nue9VLRCgZIJWkr1/cjVn5f0b52ium1/Jwbp0x2lyCQ/0AeMo4uJC9ITUQfEmosFCHpvYnyrryyFgZQdQwKmirEoWpybVD2fpYuRHKJJPYiqnSHo8Uusay/eAmaDQS5qaCO02QqKDq3jbBpvj1QBoqFngdLsQwL4ky8C9iIEXyXF6HfUdPbAOg1fhg3DSDAtyKjnLctGzYrFmps26ddfHLlDRf3eofpXYS+XRSlRs0crdiVciBJOmL4sRvRUjPb5xIuTNk8PFt5xxxmcx8Zl6CwsV37k8kzgM9wBurqvldha/5ZNUoG/BOaAUg6hCQpwLAGvEEpDlx0oI1qHpZHvLGyK0szD0WOx+gSVrDMX1Pah77eTTtCSpV3xyvh/oX6cg==]]></Encrypt>\n" +
//                "</xml>";
//        String timestamp = "1439883614";
//        String nonce = "1963493115";
//        String msg_signature = "71b99f7d56109963730d1c1aaa8d8387d6a3ded1";
//        String decryptCode = WeixinManager.getInstance().decryptMessage(miwen, timestamp,nonce,msg_signature);
//        log.debug("decryptCode,"+decryptCode);
//        WeixinManager.getInstance().getComponentVerifyTicket().load(decryptCode);
//
//        log.debug(WeixinManager.getInstance().getComponentVerifyTicket().getVerifyTicket());
//        return "call_back/bind_gzh_event";
//    }
//
//    @RequestMapping("getComponentAccessToken")
//    public String getComponentAccessToken(HttpServletRequest request,Model mode) throws AesException, DocumentException {
//        log.debug("getComponentAccessToken,"+WeixinManager.getInstance().getComponentAccessToken().getComponentAccessToken());
//
//        return "call_back/bind_gzh_event";
//    }
//
//    @RequestMapping("getPreAuthCode")
//    public String getPreAuthCode(HttpServletRequest request,Model mode) throws AesException, DocumentException {
//        log.debug("getPreAuthCode,"+WeixinManager.getInstance().getPreAuthCode().getPreAuthCode());
//
//        return "call_back/bind_gzh_event";
//    }
//
//    @RequestMapping("testWeixinSave")
//    public String testWeixinSave(HttpServletRequest request,Model mode) throws AesException, DocumentException {
//        serializeService.weixinManagerSave();
//
//        return "call_back/bind_gzh_event";
//    }
//
//    @RequestMapping("testWeixinload")
//    public String testWeixinload(HttpServletRequest request,Model mode) throws AesException, DocumentException {
//        serializeService.weixinManagerLoad();
//
//        return "call_back/bind_gzh_event";
//    }
//    @RequestMapping("getArticleContent")
//    public String getArticleContent(HttpServletRequest request,Model mode) throws AesException, DocumentException {
//        GzhArticleExample gzhArticleExample = new GzhArticleExample();
//        gzhArticleExample.createCriteria().andLinkIsNotNull().andContentStatusEqualTo(0);
//        gzhArticleExample.setOrderByClause("id limit 10 ");
//        List<GzhArticle> gzhArticleList = gzhArticleMapper.selectByExampleWithBLOBs(gzhArticleExample);
//        for(GzhArticle gzhArticle : gzhArticleList){
//            GzhArticle article = weixinService.readArticleInfo(gzhArticle.getLink());
//            gzhArticle.setContent(article.getContent());
//            gzhArticle.setContentStatus(1);
//            gzhArticleMapper.updateByPrimaryKeyWithBLOBs(gzhArticle);
//        }
//
//        return "call_back/bind_gzh_event";
//    }
//    static int allStart = 0;
//    @RequestMapping("getArticleInfo")
//    public String getArticleInfo(HttpServletRequest request,Model mode) throws AesException, DocumentException, UnsupportedEncodingException {
//        int page = Integer.parseInt(request.getParameter("page"));
//
//        GzhArticleExample gzhArticleExample = new GzhArticleExample();
//
//
//        for(int i = 0; ;i++){
//            int start = (page-1)*20 + 100*i + 1;
//            gzhArticleExample.clear();
//            gzhArticleExample.createCriteria().andIdBetween(start, start+20);
//
//           // int end = allStart+ (page)*20 + 100*i;
//           // gzhArticleExample.setOrderByClause("id limit "+start+","+20);
//            List<GzhArticle> gzhArticleList = gzhArticleMapper.selectByExampleWithBLOBs(gzhArticleExample);
//            if( page == 1 && i == 0 ){
//                allStart = gzhArticleList.get(0).getId();
//            }
//            if( gzhArticleList.size() == 0 ) break;
//            for (GzhArticle gzhArticle : gzhArticleList) {
//                if(gzhArticle.getContentStatus() != 1) continue;
//                GzhArticle article = weixinService.readArticleInfo(gzhArticle);
//                article.setContentStatus(2);
//                System.out.println("article link," + article.getLink());
//                System.out.println("article title," + article.getTitle());
//                System.out.println("article thumbnail," + article.getThumbnail());
//                System.out.println("article thumbnail2," + article.getThumbnailOrigin());
//                System.out.println("article summmary," + article.getSummary());
//                gzhArticleMapper.updateByPrimaryKeyWithBLOBs(article);
//            }
//        }
//        return "call_back/bind_gzh_event";
//    }
//}
