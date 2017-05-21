package weixin.assistant.util;

import net.sf.json.JSONObject;

/**
 * Created by worgen on 2015/9/7.
 */
public class JsonUtil {
    public static String getString(JSONObject json, String key){
        return json.has(key) ? json.getString(key) : "";
    }
}
