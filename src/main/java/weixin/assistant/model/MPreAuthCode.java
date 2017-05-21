package weixin.assistant.model;

import weixin.assistant.util.TimeUtil;
import weixin.assistant.util.WeixinUtil;
import weixin.base.html.HttpsUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 * Created by worgen on 2015/8/18.
 */
//预授权码用于公众号授权时的第三方平台方安全验证。
public class MPreAuthCode {
    private static Logger log = Logger.getLogger("Businesslog");

    private String code;
    private long requestTime;
    private int expiresIn;

    public boolean expired(){
        if( TimeUtil.now() > requestTime + expiresIn ){
            return true;
        }
        return false;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
    public String getCode() {
        return code;
    }
}
