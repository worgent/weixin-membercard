package weixin.assistant.constants;

/**
 * Created by worgen on 2015/9/1.
 */
public class WeixinConstant {
    public static final String testAppID = "wx570bc396a51b8ff8";
    public static final String testUserName = "gh_3c884a361561";

    public static final String AppID = "wx18b46b1c1423c322";
    public static final String AppSecret = "f149baa9bea0192a8a3f777df01f23dd";

    public static final String domain = "xigua2.iwpoo.com";

    public static final String componentAppID = "wx9ccf67311a4e09c2";
    public static final String componentAppSecret = "e2658041f1469dd5d596f4ad48f61cbb";
    //测试
//    public static final String componentAppID = "wx3e5063965c625425";
//    public static final String componentAppSecret = "e8c6649127b908306f1ab77af53a2d6f";
    //公众号消息校验Token
    public static final String messageVerifyToken = "iwpoo";
    //公众号消息加解密Key
    public static final String messageCryptKey = "flashappflashappflashappflashappflashappapp";
    //
    public static final String redirectUrl = "http://"+domain+"/call_back/%businessID%/authorizer_access_token.do";
    //public static final String authorizeRedirectUrl = "http://iwpoo.tunnel.mobi/call_back/%account%/authorizer_access_token.do";
    //js授权返回接口
    /*
    https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     */
    public static final String jsRedirectUrl = "http://"+domain+"/call_back/%gzh_id%/js_authorizer_access_token.do";

    //数据库存储关键字
    public static final String dbComponentVerifyTicket = "ComponentVerifyTicket";
    public static final String dbPreAuthCode= "PreAuthCode";
    public static final String dbComponentAccessToken = "ComponentAccessToken";


}
