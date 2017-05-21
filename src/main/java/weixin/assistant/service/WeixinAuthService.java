package weixin.assistant.service;

import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import org.dom4j.DocumentException;
import weixin.assistant.model.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by worgen on 2015/8/19.
 */
public interface WeixinAuthService {
    //获得平台ID componentAppID
    public String getComponentAppID();
    //获得平台secret componentAppSecret
    public String getComponentAppSecret();
    //获得消息确认token messageVerifyToken
    public String getMessageVerifyToken();
    //获得消息解密key messageCryptKey
    public String getMessageCryptKey();
    //获得公众号授权的跳转路径 redirectUrl
    public String getRedirectUrl();
    //获得公众号授权的跳转路径 redirectUrl
    public String getJSRedirectUrl();

    //获得预授权码,如果没有则http获取
    public MPreAuthCode getPreAuthCode();
    public int savePreAuthCode(MPreAuthCode mPreAuthCode);
    //获得微信后台推送的ticket
    public MComponentVerifyTicket getComponentVerifyTicket();
    public int saveComponentVerifyTicket(MComponentVerifyTicket mComponentVerifyTicket);
    //获得平台accessToken,包含刷新机制；如果没有，则http获取
    public MComponentAccessToken getComponentAccessToken();
    public int saveComponentAccessToken(MComponentAccessToken mComponentAccessToken);


    //微信授权
    //插入公众号信息
    public int insertWeixinGzh(WeixinGzh weixinGzh);
    //插入公众号授权信息
    public int insertWeixinGzhAuthorizer(WeixinGzhAuthorizer weixinGzhAuthorizer);
    //获得公众号信息
    public boolean hasWeixinGzh(int id);
    public WeixinGzh getWeixinGzh(int id);
    public WeixinGzh getWeixinGzh(int accountID, String appID);
    public WeixinGzh getWeixinGzh(String appID);
    public WeixinGzh getWeixinGzhByBusinessId(int businessId);
    //获得公众号授权信息
    //private boolean gzhAuthorizerExpired(WeixinGzhAuthorizer weixinGzhAuthorizer);
    public WeixinGzhAuthorizer checkGzhAuthorizerExpired(WeixinGzhAuthorizer weixinGzhAuthorizer);
    public boolean hasWeixinGzhAuthorizer(int weixinGzhID);
    public boolean hasWeixinGzhAuthorizer(int accountID, String appID);
    public boolean hasWeixinGzhAuthorizer(String appID);
    public WeixinGzhAuthorizer getWeixinGzhAuthorizer(int id);
    public WeixinGzhAuthorizer getWeixinGzhAuthorizerByGzhID(int weixinGzhID);
    public WeixinGzhAuthorizer getWeixinGzhAuthorizer(int businessID, String  appID);
    public WeixinGzhAuthorizer getWeixinGzhAuthorizerByBusinessId(int businessID);
    public WeixinGzhAuthorizer getWeixinGzhAuthorizer( String  appID);
    //取消授权
    public int unAuthorizeGzh(String appID);
    //进行授权
    public int authorizeGzh(int businessID, String authCode, long expiresIn);

    //插入用户信息
    public int insertWeixinUser(WeixinUser weixinUser);
    //插入用户授权信息
    public int insertWeixinUserAuthorizer(WeixinUserAuthorizer weixinUserAuthorizer);
    //获得用户信息
    public boolean hasWeixinUser(int id);
    public WeixinUser getWeixinUser(int id);
    public WeixinUser getWeixinUser(int weixinGzhID, String openID);
    //获得用户授权信息
    public boolean userAuthorizerExpired(WeixinUserAuthorizer weixinUserAuthorizer);
    public boolean hasWeixinUserAuthorizer(int weixinUserID);
    public boolean hasWeixinUserAuthorizer(int weixinGzhID, String openID);
    public WeixinUserAuthorizer getWeixinUserAuthorizer(int id);
    public WeixinUserAuthorizer getWeixinUserAuthorizerByUserID(int weixinUserID);
    public WeixinUserAuthorizer getWeixinUserAuthorizer(int weixinGzhID, String openID);
    //用户授权
    public int userAuthorize(int gzhID, String appID, String code, String state);
    //解密
    public String decryptMessage(String cryptCode, String timestamp, String nonce, String msgSignature) throws AesException;

    String cryptMessage(String content, String timestamp, String nonce) throws AesException;

    public String decryptMessage(HttpServletRequest request) throws IOException, AesException;
    //处理推送
    public int dealComponentPush(String xml) throws DocumentException;

    //
    public Map<String, String> sign(int weixinGzhID, String url);

    /****
     * 对外提供入口
     */
    //weixin_gzh_id信息

}
