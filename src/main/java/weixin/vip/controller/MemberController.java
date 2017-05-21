package weixin.vip.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.WxPayDto;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.assistant.model.*;
import weixin.assistant.service.WeixinAuthService;
import weixin.assistant.service.WeixinPayService;
import weixin.assistant.util.TimeUtil;
import weixin.guanjia.core.util.SignUtil;
import weixin.vip.Util.SessionUtil;
import weixin.vip.constants.VipConstants;
import weixin.vip.enums.SessionAttributeEnum;
import weixin.vip.service.MemberService;
import weixin.wxpay.util.WxpayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by worgen on 2015/9/9.
 */
@Controller
@RequestMapping("/vip")
public class MemberController {
    private static final Logger log = Logger.getLogger("BusinessLog");
    @Autowired
    private MemberService memberService;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private WeixinAuthService weixinAuthService;


    //申请加入页面
    //?business_id=100&open_id=abcdefghijklmn
    @RequestMapping("joinVipPage")
    public String joinVipPage(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String openID = request.getParameter("open_id");
        int businessID = Integer.parseInt(paraBusinessID);
        //商家信息，电话，地址，
        // 门店信息 门店
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        model.addAttribute("vipBusiness", vipBusiness);
        model.addAttribute("businessID", businessID);
        model.addAttribute("openID", openID);
        return "vip/joinVipPage";
    }
    //注册填写信息
    @RequestMapping("registVip")
    public String registVip(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String openID = request.getParameter("open_id");
        int businessID = Integer.parseInt(paraBusinessID);

       // model.addAttribute("vipBusiness", vipBusiness);
        model.addAttribute("businessID", businessID);
        model.addAttribute("openID", openID);
        return "vip/registVip";
    }
    //注册信息ajax接口
    @RequestMapping("ajaxRegist")
    public @ResponseBody JSONObject
    ajaxRegist(HttpServletRequest request){
        String paraBusinessID = request.getParameter("business_id");
        String openID = request.getParameter("open_id");
        int businessID = Integer.parseInt(paraBusinessID);
        //request
        String name = request.getParameter("fname");
        String tel = request.getParameter("lname");
        String sex = request.getParameter("sex");
        String bday = request.getParameter("bday");
        String verficationCode = request.getParameter("verfication_code");

        JSONObject jsonRet = new JSONObject();
        jsonRet.put("errno", 0);
        //校验验证码
        HttpSession session = request.getSession(true);
        String code = SessionUtil.getString(session, SessionAttributeEnum.REGIST_VIP_CODE.getKey());
        long codeTime = SessionUtil.getLong(session, SessionAttributeEnum.REGIST_VIP_CODE_TIME.getKey());
        String lastPhone = SessionUtil.getString(session, SessionAttributeEnum.REGIST_VIP_PHONE.getKey());

        if( codeTime + VipConstants.registVipCodeExpireTime < TimeUtil.now() ){
            log.error("code expired");
            jsonRet.put("errno", 1);
            jsonRet.put("error", "验证码超时，请重新获取");
            return jsonRet;
        }
        if( lastPhone.equals(tel) == false || code.equals(verficationCode) == false ){
            log.error("code not same,"+verficationCode+","+code+","+lastPhone+","+tel);
            jsonRet.put("errno", 2);
            jsonRet.put("error", "验证码错误");
            return jsonRet;
        }
        VipMember vipMember = new VipMember();
        vipMember.setOpenId(openID);
        vipMember.setBusinessId(businessID);
        vipMember.setName(name);
        vipMember.setTel(tel);
        vipMember.setSex(Integer.parseInt(sex));
        vipMember.setBirthday(bday);
        log.debug("doRegist,"+ JSON.toJSONString(vipMember));
        int ret = memberService.insertVipMember(vipMember);
        log.info("ajaxRegist," + JSON.toJSONString(vipMember) + "," + ret);
        if( ret <= 0 ){
            log.error("regist error,"+ret);
            jsonRet.put("errno", 3);
            jsonRet.put("error", "注册失败");
            return jsonRet;
        }
        jsonRet.put("member_id", ret);
        //发送通知邮件
        memberService.sendMessage(businessID, ret, "恭喜您注册成功", "恭喜您成为我们的会员，请您多关注我们的优惠活动。", 1);

        return jsonRet;
    }
    //完善会员信息页面
    @RequestMapping("improveVipInfo")
    public String improveVipInfo(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        //获得现有数据
        VipMember vipMember = memberService.getVipMember(memberID);
        if( vipMember == null ){
            log.error("vipMember null,"+memberID);
            return "";
        }
        model.addAttribute("vipMember", vipMember);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/improveVipInfo";
    }

    //完善信息ajax接口
    @RequestMapping("ajaxImproveVipInfo")
    public @ResponseBody int ajaxImproveVipInfo(HttpServletRequest request){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        //request
        String name = request.getParameter("name");
        String tel = request.getParameter("tel");
        String sex = request.getParameter("sex");
        String bday = request.getParameter("birthday");
        String addrProvince = request.getParameter("addrProvince");
        String addrCity = request.getParameter("addrCity");
        String addrArea = request.getParameter("addrArea");
        String addrDetail = request.getParameter("addrDetail");

        VipMember vipMember = new VipMember();
        vipMember.setId(memberID);
        vipMember.setName(name);
        vipMember.setTel(tel);
        vipMember.setSex(Integer.parseInt(sex));
        vipMember.setBirthday(bday);
        vipMember.setAddrProvince(addrProvince);
        vipMember.setAddrCity(addrCity);
        vipMember.setAddrArea(addrArea);
        vipMember.setAddrDetail(addrDetail);
        log.debug("ajaxImproveVipInfo,"+ JSON.toJSONString(vipMember));
        int ret = memberService.updateVipMember(vipMember);

        log.info("ajaxImproveVipInfo,"+ JSON.toJSONString(vipMember) + "," + ret);
        return 0;
    }

    private void weixinShare(HttpServletRequest request, Model model, VipBusiness vipBusiness){
        int businessId = vipBusiness.getId();

        //生成充值url
        WeixinGzh weixinGzh = weixinAuthService.getWeixinGzh(businessId);
        String url = request.getRequestURL()+"?"+request.getQueryString();
        /*Map<String, String> signs = weixinAuthService.sign(vipBusiness.getWeixinGzhId(), url);*/
        Map<String, String> signs = weixinAuthService.sign(businessId, url);
        //生成消费url
        model.addAttribute("signs", signs);
        model.addAttribute("weixinGzh", weixinGzh);
    }
    //会员首页
    //?business_id=100&member_id=1001
    @RequestMapping("vipHomePage")
    public String vipHomePage(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }

        weixinShare(request, model, vipBusiness);
        model.addAttribute("vipBusiness", vipBusiness);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipHomePage";
    }
    //会员个人页面
    @RequestMapping("vipPersonalPage")
    public String vipPersonalPage(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        VipMember vipMember = memberService.getVipMember(memberID);
        if( vipMember == null ){
            log.error("vipMember null,"+memberID);
            return "vip/error";
        }
        int balance = memberService.getBalance(memberID);
        weixinShare(request, model, vipBusiness);

        model.addAttribute("vipBusiness", vipBusiness);
        model.addAttribute("vipMember", vipMember);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        model.addAttribute("balance", (float)balance/100);
        return "vip/vipPersonalPage";
    }
    //我的账单页面，收入支出
    @RequestMapping("vipBill")
    public String vipBill(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        String timeMark = request.getParameter("time");
        //String 月份
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        if(timeMark == null || timeMark.isEmpty()){
            //获得当前
            DateTime dateTime = new DateTime();
            timeMark = dateTime.getYear() + "-" + dateTime.getMonthOfYear();
        }
        VipRechargeOrderExample vipRechargeOrderExample = new VipRechargeOrderExample();
        vipRechargeOrderExample.createCriteria().
                andMemberIdEqualTo(memberID).
                andStatusEqualTo((short) 1);
        List<VipRechargeOrder> vipRechargeOrders = memberService.getVipRechargeOrders(vipRechargeOrderExample);
        VipPayOrderExample vipPayOrderExample = new VipPayOrderExample();
        vipPayOrderExample.createCriteria().
                andMemberIdEqualTo(memberID).
                andStatusEqualTo((short) 1);
        List<VipPayOrder> vipPayOrders = memberService.getVipPayOrders(vipPayOrderExample);

        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        int feeInput = 0;
        int feeOutput = 0;
        for(VipRechargeOrder vipRechargeOrder: vipRechargeOrders){
            JSONObject json = new JSONObject();
            json.put("type", "recharge");
            json.put("time", vipRechargeOrder.getFinishTime());
            json.put("fee", (float)vipRechargeOrder.getFee()/100);
            feeInput+=vipRechargeOrder.getFee();
            jsonList.add(json);
        }
        for(VipPayOrder vipPayOrder: vipPayOrders){
            JSONObject json = new JSONObject();
            json.put("type", "pay");
            json.put("time", vipPayOrder.getFinishTime());
            json.put("fee", (float)vipPayOrder.getFee()/100);
            feeOutput+=vipPayOrder.getFee();
            jsonList.add(json);
        }
        Collections.sort(jsonList, new Comparator<JSONObject>(){
            public int compare(JSONObject json1, JSONObject json2){
                if( json1 == null ) return -1;
                if( json2 == null ) return 1;
                return ((Date)json1.get("time")).compareTo(
                        ((Date) json2.get("time")));
            }
        });
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        weixinShare(request, model, vipBusiness);
        model.addAttribute("feeInput", (float)feeInput/100);
        model.addAttribute("feeOutput", (float)feeOutput/100);
        model.addAttribute("jsonList", jsonList);
        model.addAttribute("timeMark", timeMark);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipBill";
    }

    //我的消息页面
    @RequestMapping("vipMessages")
    public String vipMessages(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        List<VipMessage> vipMessageList = memberService.getVipMessages(memberID);
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        weixinShare(request, model, vipBusiness);
        model.addAttribute("vipMessageList", vipMessageList);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipMessages";
    }

    //修改密码页面--暂时没有
    @RequestMapping("vipChangePassword")
    public String vipChangePassword(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        List<VipMessage> vipMessageList = memberService.getVipMessages(memberID);
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        weixinShare(request, model, vipBusiness);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipChagePassword";
    }
    @RequestMapping("ajaxVipChangePassword")
    public @ResponseBody int ajaxVipChangePassword(HttpServletRequest request){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        String newPassword = request.getParameter("new_password");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        memberService.changeVipPassword(memberID, newPassword);
        return 0;
    }

    //其他页面
    //?business_id=100&member_id=1001
    //门店信息页面
    @RequestMapping("storeInfo")
    public String storeInfo(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        weixinShare(request, model, vipBusiness);
        model.addAttribute("vipBusiness", vipBusiness);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/storeInfo";
    }

    //充值确认界面
    public String vipRechargeConfirm(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);

        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        if( vipBusiness == null ){
            log.error("vipBusiness null,"+businessID);
            return "vip/error";
        }
        weixinShare(request, model, vipBusiness);

        model.addAttribute("vipBusiness", vipBusiness);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);

        return "vip/vipRechargeConfirm";
    }

    @RequestMapping("paySuccess")
    public String paySuccess(HttpServletRequest request, Model model){
        String paraFee = request.getParameter("fee");
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);
        int fee = Integer.parseInt(paraFee);

        model.addAttribute("fee", (float)fee/100);
        model.addAttribute("balance", (float)memberService.getBalance(memberID)/100);
        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/paySuccess";
    }

    //我的代金券
    @RequestMapping("vipCoupon")
    public String vipCoupon(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);


        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipCoupon";
    }
    //我的礼品券
    @RequestMapping("vipGift")
    public String vipGift(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);


        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipGift";
    }
    //我的活动
    @RequestMapping("vipActivity")
    public String vipActivity(HttpServletRequest request, Model model){
        String paraBusinessID = request.getParameter("business_id");
        String paraMemberID = request.getParameter("member_id");
        int businessID = Integer.parseInt(paraBusinessID);
        int memberID = Integer.parseInt(paraMemberID);


        model.addAttribute("businessID", businessID);
        model.addAttribute("memberID", memberID);
        return "vip/vipActivity";
    }
}
