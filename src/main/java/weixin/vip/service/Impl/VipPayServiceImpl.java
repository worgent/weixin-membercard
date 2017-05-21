package weixin.vip.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.demo.WxPayDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.assistant.dao.WeixinGzhMapper;
import weixin.assistant.model.*;
import weixin.assistant.service.WeixinAuthService;
import weixin.assistant.service.WeixinPayService;
import weixin.vip.service.MemberService;
import weixin.vip.service.VipPayService;
import weixin.wxpay.util.WxpayUtil;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by worgen on 2015/9/10.
 */
@Service
public class VipPayServiceImpl implements VipPayService{
    private static final Logger log = Logger.getLogger("BussinessLog");

    @Autowired
    private MemberService memberService;

    @Autowired
    private WeixinAuthService weixinAuthService;

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private WeixinGzhMapper weixinGzhMapper;

    @Override
    public JSONObject getPayUrl(int businessID, int memberID, int fee,
                                String outTradeNo, String title, String baseUrl,
                                String attach) {
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);

        JSONObject jsonRet = new JSONObject();
        jsonRet.put("errno", 0);
        if( fee <= 0 ){
            log.error("fee,zero,"+fee);
            jsonRet.put("errno", -1);
            return jsonRet;
        }
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            jsonRet.put("errno", 1);
            return jsonRet;
        }
        VipMember vipMember = memberService.getVipMember(memberID);
        if( vipMember == null ){
            log.error("vipMember null,"+memberID);
            jsonRet.put("errno", 2);
            return jsonRet;
        }

        /*修改前
        int gzhID = vipBusiness.getWeixinGzhId();*/
        //修改後
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().andBusinessIdEqualTo(businessID);
        List<WeixinGzh> weixinGzhList = weixinGzhMapper.selectByExample(weixinGzhExample);
        int gzhID = weixinGzhList.get(0).getId();

        WeixinGzh weixinGzh = weixinAuthService.getWeixinGzh(gzhID);
        if( weixinGzh == null ){
            log.error("weixinGzh null,"+gzhID);
            jsonRet.put("errno", 3);
            return jsonRet;
        }
        String appID = weixinGzh.getAppId();
        String openID = vipMember.getOpenId();

        WeixinGzhPay weixinGzhPay = weixinPayService.getWeixinGzhPay(gzhID);
        if( weixinGzhPay == null ){
            log.error( "weixinGzhPay null,"+gzhID);
            jsonRet.put("errno", 4);
            return jsonRet;
        }

        // appID = "wxd52a218ec4c652c4";
        //  openID = "oHo1mtzPRw1fFCt_d8pLgQtcdr04";
        //微信支付jsApi
        WxPayDto tpWxPay = new WxPayDto();
        tpWxPay.setNotifyUrl(weixinGzhPay.getNotifyUrl());
        tpWxPay.setPartner(weixinGzhPay.getMchId());
        tpWxPay.setPartnerKey(weixinGzhPay.getMchKey());
        tpWxPay.setAppId(appID);
        tpWxPay.setOpenId(openID);
        tpWxPay.setOrderId(outTradeNo);
        tpWxPay.setSpbillCreateIp("127.0.0.1");
        tpWxPay.setBody(title);
        tpWxPay.setTotalFee(String.valueOf(fee));
        tpWxPay.setAttach(attach);

        String finaPackage = WxpayUtil.getPackage(tpWxPay);



        String url = baseUrl
                + "?showwxpaytitle=1&business_id="
                + businessID
                + "&member_id="
                + memberID
                + "&finaPackage="
                + URLEncoder.encode(finaPackage);

        jsonRet.put("pay_url", url);

        return jsonRet;
    }
}
