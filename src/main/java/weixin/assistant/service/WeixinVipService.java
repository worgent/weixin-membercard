package weixin.assistant.service;

import org.jeewx.api.core.exception.WexinReqException;
import weixin.assistant.model.WeixinGzhAuthorizer;

/**
 * Created by worgen on 2015/9/9.
 */
public interface WeixinVipService {
    //根据appid获得weixin_gzh_id
   // public String getVipUrl(String appID, String openID);

    //发送会员卡信息
    public void responseVip(String appID, String openID) throws WexinReqException;

}
