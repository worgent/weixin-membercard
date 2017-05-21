package weixin.assistant.model;

import weixin.assistant.util.TimeUtil;
import weixin.assistant.util.WeixinUtil;
import weixin.base.html.HttpsUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 * Created by worgen on 2015/8/18.
 */
public class MComponentAccessToken {
    private static Logger log = Logger.getLogger("Businesslog");


    private String accessToken;
    private long requestTime;
    private int expiresIn;


    public boolean expired(){
        if( TimeUtil.now() > requestTime + expiresIn ){
            return true;
        }
        return false;
    }


    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
    public String getAccessToken() {
        return accessToken;
    }
}
