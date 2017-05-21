package weixin.assistant.util;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import weixin.assistant.model.*;
import weixin.base.html.HttpsUtil;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by worgen on 2015/8/17.
 */
public class WeixinUtil {
    private static final Logger log = Logger.getLogger("BusinessLog");


    //解密
    public static String decryptMessage(String messageVerifyToken, String messageCryptKey, String componentAppID,
                                        String cryptCode, String timestamp, String nonce, String msgSignature) throws AesException {
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(messageVerifyToken, messageCryptKey, componentAppID);
        return wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, cryptCode);
    }
    //解密
    public static String cryptMessage(String messageVerifyToken, String messageCryptKey, String componentAppID,
                                        String content, String timestamp, String nonce) throws AesException {
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(messageVerifyToken, messageCryptKey, componentAppID);
        return wxBizMsgCrypt.encryptMsg(content, timestamp, nonce);
    }
    public static MComponentAccessToken httpGetComponentAccessToken(String componentAppID,
                                                     String componentAppSecret,
                                                     String componentVerifyTicket){

       // if( accessToken == null || expired() ){
           // String verifyTicket = WeixinUtil.getInstance().getComponentVerifyTicket().getVerifyTicket();

            String content = "{\"component_appid\":\""
                    +componentAppID+"\",\"component_appsecret\":\""
                    +componentAppSecret+"\",\"component_verify_ticket\":\""
                    +componentVerifyTicket + "\"}";

            String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
            //{"component_access_token":"kjbPGfu5O8cjN9MLPv8QX8qDiwz2mH4Voc2IlpnRLv4RwwXkD1Lnhn8Dp_vBQzG8nUEIpeTVpesfimNCPTDxtuT3K7Ov5k73nVkxKABqV5M","expires_in":7200}
            log.debug("before post,"+content+","+url);
            String component_access_token_Json = HttpsUtil.post(url, content);
            log.debug("after post,"+content+","+url+","+component_access_token_Json);

            JSONObject jsonObject = JSONObject.fromObject(component_access_token_Json);
            String accessToken = jsonObject.getString("component_access_token");
            int expiresIn = jsonObject.getInt("expires_in");
            long requestTime = TimeUtil.now();

        MComponentAccessToken mComponentAccessToken = new MComponentAccessToken();
        mComponentAccessToken.setAccessToken(accessToken);
        mComponentAccessToken.setExpiresIn(expiresIn);
        mComponentAccessToken.setRequestTime(requestTime);
        //}
        return mComponentAccessToken;
    }

    public static MPreAuthCode httpGetPreAuthCode(String componentAppID, String componentAccessToken){
       // if( code == null || expired() ){
            String content = "{\"component_appid\":\""+componentAppID+"\"}";
            String url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="
                    + componentAccessToken;
            log.debug("before post,"+content+","+url);
            String pre_auth_code_Json = HttpsUtil.post(url, content);
            log.debug("after post,"+content+","+url+","+pre_auth_code_Json);

            JSONObject jsonObject = JSONObject.fromObject(pre_auth_code_Json);
            String code = jsonObject.getString("pre_auth_code");
            int expiresIn = jsonObject.getInt("expires_in");
            long requestTime = TimeUtil.now();

        MPreAuthCode mPreAuthCode = new MPreAuthCode();

        mPreAuthCode.setCode(code);
        mPreAuthCode.setExpiresIn(expiresIn);
        mPreAuthCode.setRequestTime(requestTime);

        return mPreAuthCode;
    }
    /*
该API用于获取授权方的公众号基本信息，包括头像、昵称、帐号类型、认证类型、微信号、原始ID和二维码图片URL。
需要特别记录授权方的帐号类型，在消息及事件推送时，对于不具备客服接口的公众号，需要在5秒内立即响应；而若有客服接口，则可以选择暂时不响应，而选择后续通过客服接口来发送消息触达粉丝。
     */
    public static WeixinGzh httpGetWeixinGzh(String componentAppID, String componentAccessToken, String authorizerAppID) {
        //获取信息
        String url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="
                + componentAccessToken;
        String content = "{\"component_appid\":\""
                + componentAppID
                + "\",\"authorizer_appid\":\""
                + authorizerAppID + "\"}";
        log.debug("before post,"+content+","+url);
        String authorizer_info_Json = HttpsUtil.post(url, content);
        log.debug("after post,"+content+","+url+","+authorizer_info_Json);

        JSONObject jsonObject = JSONObject.fromObject(authorizer_info_Json);
        JSONObject jsonObjectInfo = jsonObject.getJSONObject("authorizer_info");
        String userName = jsonObjectInfo.getString("nick_name");
        String alias = jsonObjectInfo.getString("alias");
        String headImg = jsonObjectInfo.has("head_img") ? jsonObjectInfo.getString("head_img") : "";
        int serviceTypeInfo = jsonObjectInfo.getJSONObject("service_type_info").getInt("id");
        int verifyTypeInfo = jsonObjectInfo.getJSONObject("verify_type_info").getInt("id");
        String qrcodeUrl = jsonObjectInfo.getString("qrcode_url");
        JSONObject jsonObject2 = jsonObject.getJSONObject("authorization_info");

        Set<Integer> funcInfo = new HashSet<Integer>();
        net.sf.json.JSONArray funJson = jsonObject2.getJSONArray("func_info");
        for (int i = 0; i < funJson.size(); i++) {
            JSONObject j = funJson.getJSONObject(i);
            funcInfo.add(j.getJSONObject("funcscope_category").getInt("id"));
        }
        long requestTime = TimeUtil.now();

        WeixinGzh weixinGzh = new WeixinGzh();
        weixinGzh.setAppId(authorizerAppID);
        weixinGzh.setName(userName);
        weixinGzh.setAlias(alias);
        weixinGzh.setHeadImg(headImg);
        weixinGzh.setServiceTypeInfo(serviceTypeInfo);
        weixinGzh.setVerifyTypeInfo(verifyTypeInfo);
        weixinGzh.setQrcodeUrl(qrcodeUrl);
        weixinGzh.setFuncInfo(JSON.toJSONString(funcInfo));

        return weixinGzh;
    }
/*
该API用于在授权方令牌（authorizer_access_token）失效时，可用刷新令牌（authorizer_refresh_token）获取新的令牌。
 */
    public static WeixinGzhAuthorizer httpRefreshAuthorozerAccessToken(String componentAppID, String componentAccessToken,
                                                  String authorizerAppID, String authorizerRefreshToken){
            String authCode = "";
            String content = "{\"component_appid\":\""
                    + componentAppID
                    + "\",\"authorizer_appid\":\""
                    + authorizerAppID
                    + "\",\"authorizer_refresh_token\":\""
                    + authorizerRefreshToken+"\"}";

            String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="
                    + componentAccessToken;
            log.debug("before post,"+content+","+url);
            String authorization_info_Json = HttpsUtil.post(url, content);
            log.debug("after post,"+content+","+url+","+authorization_info_Json);

            JSONObject jsonObject = JSONObject.fromObject(authorization_info_Json);

            if (jsonObject == null) {
                log.error("jsonObject null,"+url+","+authorization_info_Json);
                return null;
            }

            String authorizerAccessToken = jsonObject.getString("authorizer_access_token");
            long tokenExpiresIn = jsonObject.getLong("expires_in");
            String newAuthorizerRefreshToken = jsonObject.getString("authorizer_refresh_token");
            long requestTime = TimeUtil.now();

        WeixinGzhAuthorizer weixinGzhAuthorizer = new WeixinGzhAuthorizer();
        weixinGzhAuthorizer.setAuthorizerAccessToken(authorizerAccessToken);
        weixinGzhAuthorizer.setTokenExpireTime(TimeUtil.getDate(requestTime + tokenExpiresIn));
        weixinGzhAuthorizer.setAuthorizerRefreshToken(newAuthorizerRefreshToken);

        log.info("refresh AuthorozerAccessToken," + com.alibaba.fastjson.JSONObject.toJSONString(weixinGzhAuthorizer));

        return weixinGzhAuthorizer;
    }

/*
该API用于使用授权码换取授权公众号的授权信息，并换取authorizer_access_token和authorizer_refresh_token。 授权码的获取，需要在用户在第三方平台授权页中完成授权流程后，在回调URI中通过URL参数提供给第三方平台方。
 */
    public static WeixinGzhAuthorizer httpGetWeixinGzhAuthorizer(String componentAppID, String componentAccessToken,
                                                              String authCode){

        //第一次请求
        String content = "{\"component_appid\":\""
                + componentAppID + "\",\"authorization_code\":\""
                + authCode + "\"}";

        String url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="
                + componentAccessToken;

        log.debug("before post,"+content+","+url);
        String authorization_info_Json = HttpsUtil.post(url, content);
        log.debug("after post,"+content+","+url+","+authorization_info_Json);
        JSONObject jsonObject = JSONObject.fromObject(authorization_info_Json);
        if (jsonObject == null) {
            log.error("jsonObject null,"+url+","+authorization_info_Json);
            return null;
        }
        if (jsonObject.containsKey("authorization_info") == false) {
            log.error("authorization_info null,"+url+","+authorization_info_Json);
            return null;
        }
        JSONObject jsonObject2 = jsonObject.getJSONObject("authorization_info");
        String authorizerAppID = jsonObject2.getString("authorizer_appid");
        String authorozerAccessToken = jsonObject2.getString("authorizer_access_token");
        long tokenExpiresIn = jsonObject2.getLong("expires_in");
        String authorizerRefreshToken = jsonObject2.getString("authorizer_refresh_token");
        long requestTime = TimeUtil.now();

        Set<Integer> funcInfo = new HashSet<Integer>();
        net.sf.json.JSONArray funJson = jsonObject2.getJSONArray("func_info");
        for(int i=0; i < funJson.size(); i++){
            JSONObject j = funJson.getJSONObject(i);
            funcInfo.add(j.getJSONObject("funcscope_category").getInt("id"));
        }

        WeixinGzhAuthorizer weixinGzhAuthorizer = new WeixinGzhAuthorizer();
        weixinGzhAuthorizer.setAppId(authorizerAppID);
        weixinGzhAuthorizer.setAuthorizationCode(authCode);
        weixinGzhAuthorizer.setAuthorizerRefreshToken(authorizerRefreshToken);
        weixinGzhAuthorizer.setTokenExpireTime(TimeUtil.getDate(requestTime + tokenExpiresIn));
        weixinGzhAuthorizer.setAuthorizerAccessToken(authorozerAccessToken);


        log.info("first AuthorozerAccessToken,"+ com.alibaba.fastjson.JSONObject.toJSONString(weixinGzhAuthorizer));
        return weixinGzhAuthorizer;
    }


    public static WeixinGzhAuthorizer httpGetJSApiTicket(String gzhAccessToken){


        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+gzhAccessToken+"&type=jsapi";

        log.debug("before post,"+url);
        String authorization_info_Json = HttpsUtil.post(url, "");
        log.debug("after post,"+url+","+authorization_info_Json);
        JSONObject jsonObject = JSONObject.fromObject(authorization_info_Json);
        if (jsonObject == null) {
            log.error("jsonObject null,"+url+","+authorization_info_Json);
            return null;
        }
        int errorCode = jsonObject.getInt("errcode");

        if (errorCode != 0) {
            String errMsg = jsonObject.getString("errmsg");
            log.error("httpGetJSApiTicket error,"+errorCode+","+errMsg);
            return null;
        }
        String ticket = jsonObject.getString("ticket");
        long ticketExpiresIn = jsonObject.getLong("expires_in");
        long requestTime = TimeUtil.now();

        WeixinGzhAuthorizer weixinGzhAuthorizer = new WeixinGzhAuthorizer();
        weixinGzhAuthorizer.setJsapiTicket(ticket);
        weixinGzhAuthorizer.setTicketExpireTime(TimeUtil.getDate(requestTime + ticketExpiresIn));

        log.info("httpGetJSApiTicket,"+ com.alibaba.fastjson.JSONObject.toJSONString(weixinGzhAuthorizer));
        return weixinGzhAuthorizer;
    }


    /*
    通过code换取网页授权access_token
    首先请注意，这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token（该access_token用于调用其他接口）不同。公众号可通过下述接口来获取网页授权access_token。如果网页授权的作用域为snsapi_base，则本步骤中获取到网页授权access_token的同时，也获取到了openid，snsapi_base式的网页授权流程即到此为止。
     */
    public static WeixinUserAuthorizer httpGetJSAccessToken(String componentAppID, String componentAccessToken, String appID,
                                                                 String code){

        String url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid="
                    + appID
                    + "&code="
                    + code
                    + "&grant_type=authorization_code&component_appid="
                    + componentAppID
                    + "&component_access_token="
                    + componentAccessToken;

        log.debug("before post,"+","+url);
        String authorization_info_Json = HttpsUtil.post(url, "");
        log.debug("after post,"+","+url+","+authorization_info_Json);
        JSONObject jsonObject = JSONObject.fromObject(authorization_info_Json);
        if (jsonObject == null) {
            log.error("jsonObject null,"+url+","+authorization_info_Json);
            return null;
        }
        String accessToken = jsonObject.getString("access_token");
        long expiresIn = jsonObject.getLong("expires_in");
        String refreshToken = jsonObject.getString("refresh_token");
        String openid = jsonObject.getString("openid");
        String scope = jsonObject.getString("scope");
        String unionid = JsonUtil.getString(jsonObject, "unionid");
        long requestTime = TimeUtil.now();

        WeixinUserAuthorizer weixinUserAuthorizer = new WeixinUserAuthorizer();
        weixinUserAuthorizer.setCode(code);
        weixinUserAuthorizer.setAccessToken(accessToken);
        weixinUserAuthorizer.setTokenExpireTime(TimeUtil.getDate(requestTime+expiresIn));
        weixinUserAuthorizer.setRefreshToken(refreshToken);
        weixinUserAuthorizer.setScope(scope);
        weixinUserAuthorizer.setOpenId(openid);
        weixinUserAuthorizer.setUnionId(unionid);

        log.info("first httpGetJSAccessToken,"+ com.alibaba.fastjson.JSONObject.toJSONString(weixinUserAuthorizer));
        return weixinUserAuthorizer;
    }

    /*
刷新access_token（如果需要）
由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，refresh_token拥有较长的有效期（7天、30天、60天、90天），当refresh_token失效的后，需要用户重新授权。
*/
    public static WeixinUserAuthorizer httpRefreshJSAccessToken(String componentAppID, String componentAccessToken,
                                                                String appID, String refreshToken){

        String url = "https://api.weixin.qq.com/sns/oauth2/component/refresh_token?appid="
                +appID+"&grant_type=refresh_token&component_appid="
                +componentAppID+"&component_access_token="
                +componentAccessToken+"&refresh_token="
                +refreshToken;

        log.debug("before post,"+","+url);
        String authorization_info_Json = HttpsUtil.post(url, "");
        log.debug("after post,"+","+url+","+authorization_info_Json);
        JSONObject jsonObject = JSONObject.fromObject(authorization_info_Json);
        if (jsonObject == null) {
            log.error("jsonObject null,"+url+","+authorization_info_Json);
            return null;
        }
        String accessToken = jsonObject.getString("access_token");
        long expiresIn = jsonObject.getLong("expires_in");
        String newRefreshToken = jsonObject.getString("refresh_token");
        String openid = jsonObject.getString("openid");
        String scope = jsonObject.getString("scope");
        long requestTime = TimeUtil.now();

        WeixinUserAuthorizer weixinUserAuthorizer = new WeixinUserAuthorizer();
        weixinUserAuthorizer.setAccessToken(accessToken);
        weixinUserAuthorizer.setTokenExpireTime(TimeUtil.getDate(requestTime + expiresIn));
        weixinUserAuthorizer.setRefreshToken(newRefreshToken);
        weixinUserAuthorizer.setScope(scope);
        weixinUserAuthorizer.setOpenId(openid);

        log.info("httpRefreshJSAccessToken,"+ com.alibaba.fastjson.JSONObject.toJSONString(weixinUserAuthorizer));
        return weixinUserAuthorizer;
    }


}
