package weixin.assistant.model;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by worgen on 2015/8/18.
 */
public class MComponentVerifyTicket {
    private String appID;

    private int createTime;
    private String infoType;
    private String verifyTicket;

    public String getVerifyTicket(){
        return verifyTicket;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public void setVerifyTicket(String verifyTicket) {
        this.verifyTicket = verifyTicket;
    }

}
