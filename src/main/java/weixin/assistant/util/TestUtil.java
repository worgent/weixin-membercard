package weixin.assistant.util;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weixin.assistant.constants.WeixinConstant;

/**
 * Created by worgen on 2015/9/15.
 */
public class TestUtil {
    private static Logger log = Logger.getLogger("Businesslog");

    public static void testDecrypt(String cryptCode) throws ParserConfigurationException, IOException, SAXException, AesException {


        String componentAppID = WeixinConstant.componentAppID;
        String messageCryptKey = WeixinConstant.messageCryptKey;
        String messageVerifyToken = WeixinConstant.messageVerifyToken;
        WXBizMsgCrypt pc = new WXBizMsgCrypt(messageVerifyToken, messageCryptKey, componentAppID);


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(cryptCode);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");
        NodeList nodelist2 = root.getElementsByTagName("MsgSignature");
        NodeList nodelist3 = root.getElementsByTagName("TimeStamp");
        NodeList nodelist4 = root.getElementsByTagName("Nonce");

        String encrypt = nodelist1.item(0).getTextContent();
        String msgSignature = nodelist2.item(0).getTextContent();
        String timestamp = nodelist3.item(0).getTextContent();
        String nonce = nodelist4.item(0).getTextContent();
        log.debug("encrypt,"+encrypt);
        log.debug("msgSignature,"+msgSignature);
        log.debug("timestamp,"+timestamp);
        log.debug("nonce,"+nonce);
//        String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
//        String fromXML = String.format(format, encrypt);

        //
        // 公众平台发送消息给第三方，第三方处理
        //

        // 第三方收到公众号平台发送的消息
        String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, cryptCode);
        log.debug("解密后明文: " + result2);
        return;
    }
}
