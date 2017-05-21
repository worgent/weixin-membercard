package third.pingpp.controller;

import com.alibaba.fastjson.JSONObject;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by worgen on 2015/9/29.
 */
@Controller
@RequestMapping("PingPP/")
public class PingPPController {
    /**
     * pingpp 管理平台对应的 API key
     */
    public static String apiKey = "sk_test_Kqz1WDKGaXz5PSGeTKWbb588";
    /**
     * pingpp 管理平台对应的应用 ID
     */
    public static String appId = "app_S8iXT0SmfDS0mjzj";
    //进入支付页
    @RequestMapping("toChargePage")
    public String toChargePage(HttpServletRequest request, Model model){
        return "pingpp/charge";
    }
    //
    @ResponseBody
    @RequestMapping("ajaxGetCharge")
    public Charge ajaxGetCharge(){
        Pingpp.apiKey = apiKey;

        JSONObject jsonRet = new JSONObject();
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", 100);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "Your Subject");
        chargeMap.put("body", "Your Body");
        chargeMap.put("order_no", "123456789");
        chargeMap.put("channel", "alipay");
        chargeMap.put("client_ip", "127.0.0.1");
        Map<String, String> app = new HashMap<String, String>();
        app.put("id",appId);
        chargeMap.put("app", app);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
            System.out.println(charge);
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return charge;
    }
    //查询支付状态

    //接收PingPP推送消息
    //push/charge_success.do
    //支付

}
