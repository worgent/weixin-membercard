package weixin.assistant.controller;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import weixin.assistant.model.WeixinGzh;
import weixin.assistant.service.WeixinAuthService;
import weixin.vip.service.MemberService;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by worgen on 2015/9/2.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    private static final Logger log = Logger.getLogger("BusinnessLog");

    @Autowired
    private WeixinAuthService weixinAuthService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("action")
    public String testAction(HttpServletRequest request, Model model){
//        int accountID = Integer.parseInt(request.getParameter("account"));
//        String appID = request.getParameter("app");
//        WeixinGzh weixinGzh = new WeixinGzh();
//        weixinGzh.setAccountId(accountID);
//        weixinGzh.setAppId(appID);
//        int ret = weixinAuthService.insertWeixinGzh(weixinGzh);
//        log.debug(ret);
        log.debug("getServletPath,"+request.getServletPath());
        log.debug("getRequestURL,"+request.getRequestURL());
        log.debug("getContextPath,"+request.getContextPath());
        log.debug("getRequestURI,"+request.getRequestURI());
        //log.debug("getQueryString,"+request.());
        log.debug("getHeaderNames,"+ JSON.toJSONString(request.getHeaderNames()));
        log.debug("getHeaderNames,"+ JSON.toJSONString(request.getHeaderNames()));
        memberService.incVipMoneyInput(1, 10);
        memberService.incVipMoneyOutput(1, 10);
        //memberService.incVipMoneyInput(1, 10);
        //model.addAttribute("message", ret+","+weixinGzh.getId());
        return "weixin/message";
    }


}
