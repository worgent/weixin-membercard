package weixin.vip.Util;

import weixin.vip.enums.SessionAttributeEnum;

import javax.servlet.http.HttpSession;

/**
 * Created by worgen on 2015/9/11.
 */
public class SessionUtil {
    //
    public static String getString(HttpSession session, String key){
        if( session == null ) return "";
        Object value = session.getAttribute(key);
        if( value == null ) return "";
        return value.toString();
    }

    public static long getLong(HttpSession session, String key){
        String value = getString(session, key);
        if( value.isEmpty() ) return 0L;
        return Long.parseLong(value);
    }
}
