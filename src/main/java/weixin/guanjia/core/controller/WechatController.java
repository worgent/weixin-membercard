package weixin.guanjia.core.controller;

import com.qq.weixin.mp.aes.AesException;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import weixin.guanjia.core.service.impl.WechatService;
import weixin.guanjia.core.util.SignUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/call_back2")
public class WechatController {
	private static final Logger log = Logger.getLogger("BusinessLog");
	@Autowired
	private WechatService wechatService;


	@RequestMapping("getComponentAccessToken")
	public String getComponentAccessToken(HttpServletRequest request,Model mode) throws AesException, DocumentException {
		log.debug("tet");

		return "call_back/bind_gzh_event";
	}
	//@RequestMapping(value = "/wechat", params="wechat", method = RequestMethod.GET)
	@RequestMapping( "/wechat")
	public void wechatGet(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "signature") String signature,
			@RequestParam(value = "timestamp") String timestamp,
			@RequestParam(value = "nonce") String nonce,
			@RequestParam(value = "echostr") String echostr) {

		log.debug("signature,"+signature);
		log.debug("timestamp,"+timestamp);
		log.debug("nonce,"+nonce);
		log.debug("echostr,"+echostr);
		String token = "77044_m";
		//List<WeixinAccountEntity> weixinAccountEntities = weixinAccountService.getList(WeixinAccountEntity.class);
		//for (WeixinAccountEntity account : weixinAccountEntities) {
			if (SignUtil.checkSignature(token, signature,
					timestamp, nonce)) {
				try {
					response.getWriter().print(echostr);
					//break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		//}
	}



}
