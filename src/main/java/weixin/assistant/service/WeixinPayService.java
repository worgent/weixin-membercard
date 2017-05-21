package weixin.assistant.service;

import weixin.assistant.model.WeixinGzhPay;

/**
 * Created by worgen on 2015/9/10.
 */
public interface WeixinPayService {

    //获得支付信息
    public WeixinGzhPay getWeixinGzhPay(int weixinGzhID);
}
