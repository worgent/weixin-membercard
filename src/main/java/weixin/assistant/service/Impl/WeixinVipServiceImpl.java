package weixin.assistant.service.Impl;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.model.kfaccount.KfcustomSend;
import org.jeewx.api.core.req.model.kfaccount.MsgArticles;
import org.jeewx.api.core.req.model.kfaccount.MsgNews;
import org.jeewx.api.wxsendmsg.JwKfaccountAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.assistant.constants.WeixinConstant;
import weixin.assistant.model.VipBusiness;
import weixin.assistant.model.VipMember;
import weixin.assistant.model.WeixinGzh;
import weixin.assistant.model.WeixinGzhAuthorizer;
import weixin.assistant.service.WeixinAuthService;
import weixin.assistant.service.WeixinVipService;
import weixin.vip.service.MemberService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worgen on 2015/9/9.
 */
@Service
public class WeixinVipServiceImpl implements WeixinVipService {
    private static final Logger log = Logger.getLogger("BusinessLog");
    @Autowired
    private WeixinAuthService weixinAuthService;
    @Autowired
    private MemberService memberService;



    @Override
    public void responseVip(String appID, String openID) throws WexinReqException {
        WeixinGzhAuthorizer weixinGzhAuthorizer = weixinAuthService.getWeixinGzhAuthorizer(appID);
        if( weixinGzhAuthorizer == null ){
            log.error("weixinGzhAuthorizer null,"+appID);
            return;
        }
        WeixinGzh weixinGzh = weixinAuthService.getWeixinGzh(appID);
        if( weixinGzh == null ){
            log.error("weixinGzh null,"+appID);
            return;
        }
        VipBusiness vipBusiness = memberService.getVipBusinessByGzhID(weixinGzh.getId());
        int businessID = vipBusiness.getId();
        VipMember vipMember = memberService.getVipMember(openID);
        String url = "http://"+ WeixinConstant.domain+"/vip/";
        KfcustomSend kfcustomSend = new KfcustomSend();
        kfcustomSend.setAccess_token(weixinGzhAuthorizer.getAuthorizerAccessToken());
        kfcustomSend.setTouser(openID);
        kfcustomSend.setMsgtype("news");
        MsgNews msgNews = new MsgNews();
        MsgArticles msgArticles = new MsgArticles();
        //是否已经是会员
        if(vipMember == null ){
            url += "joinVipPage.do?";
            url += "business_id="+businessID;
            url += "&open_id="+openID;

            msgArticles.setTitle("会员卡");
            msgArticles.setDescription("欢迎关注"+vipBusiness.getName()+"，会员尊享特权请点击进入查询");
            msgArticles.setPicurl("http://weixin.iwpoo.com:4869/cf64d43ad4e4db90a35b187c65b2adbf");
            msgArticles.setUrl(url);
        }else {
            url += "vipHomePage.do?";
            url += "business_id="+businessID;
            url += "&member_id="+vipMember.getId();

            msgArticles.setTitle("会员卡");
            msgArticles.setDescription("尊敬的会员卡用户，您的会员卡号为" + vipMember.getTel()
                    + "，会员尊享特权请点击进入查询");
            msgArticles.setPicurl("http://weixin.iwpoo.com:4869/cf64d43ad4e4db90a35b187c65b2adbf");
            msgArticles.setUrl(url);
        }

        List<MsgArticles> msgArticlesList = new ArrayList<MsgArticles>();
        msgArticlesList.add(msgArticles);
        msgNews.setArticles(msgArticlesList);
        kfcustomSend.setNews(msgNews);
        String strRet = JwKfaccountAPI.sendKfMessage(kfcustomSend);
        log.debug("sendKfMessage," + JSON.toJSONString(kfcustomSend) + "," + strRet);
    }
}
