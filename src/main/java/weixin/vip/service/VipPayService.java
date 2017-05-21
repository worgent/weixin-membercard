package weixin.vip.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by worgen on 2015/9/10.
 */
public interface VipPayService {
    //p
    public JSONObject getPayUrl(int businessID, int memberID, int fee,
                                String outTradeNo, String title, String baseUrl,
                                String attach);
}
